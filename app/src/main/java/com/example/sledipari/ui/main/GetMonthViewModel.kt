package com.example.sledipari.ui.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sledipari.R
import com.example.sledipari.api.FirebasePushNotificationsApi
import com.example.sledipari.api.models.PostSpendingRequest
import com.example.sledipari.api.models.pushnotifications.NotificationData
import com.example.sledipari.api.models.pushnotifications.PushNotification
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.data.models.Month
import com.example.sledipari.data.models.Transaction
import com.example.sledipari.utility.Constants.SLEDI_PARI_TOPIC
import com.example.sledipari.utility.extensions.formatPrice
import com.example.sledipari.utility.extensions.toList
import com.example.sledipari.utility.extensions.totalSum
import com.example.sledipari.utility.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetMonthViewModel
@Inject constructor(
    private val repo: MonthRepository,
    private val firebaseApi: FirebasePushNotificationsApi,
    @SuppressLint("StaticFieldLeak") private val context: Context
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessageMonthScreen = MutableStateFlow<String?>(null)
    val errorMessageMonthScreen = _errorMessageMonthScreen.asStateFlow()

    private val _errorMessageBottomSheet = MutableStateFlow<String?>(null)
    val errorMessageBottomSheet = _errorMessageBottomSheet.asStateFlow()

    private val _month = MutableStateFlow<Month?>(null)
    val month = _month.asStateFlow()

    private val _monthId = MutableStateFlow(System.currentTimeMillis().formatDate("yyyy-MM"))
    val monthId = _monthId.asStateFlow()

    private val _allMonths = MutableStateFlow(listOf<Month>())
    val allMonths = _allMonths.asStateFlow()

    private val _isSpendingSuccessful = MutableStateFlow(false)
    val isSpendingSuccessful = _isSpendingSuccessful.asStateFlow()

    private val _hasCompleted = MutableStateFlow(false)
    val hasCompleted = _hasCompleted.asStateFlow()

    private val _currentCategory = MutableStateFlow(context.getString(R.string.all))
    val currentCategory = _currentCategory.asStateFlow()

    private val _totalSum = MutableStateFlow(_month.value?.totalSum() ?: 0f)
    val totalSum: StateFlow<Float> = _totalSum

    private val _currentList = MutableStateFlow(_month.value?.toList() ?: listOf())
    val currentList = _currentList.asStateFlow()

    fun getMonthLocal(monthId: String) {

        _isLoading.value = true

        viewModelScope.launch {
            _month.value = repo.getMonthLocal(monthId)
            _isLoading.value = false
        }
    }

    fun getMonth(timestamp: String) {
        _isLoading.value = true

        viewModelScope.launch {

            try {

                _month.value = repo.getMonth(timestamp)
                _monthId.value = _month.value!!.id
            } catch (e: Exception) {

                _errorMessageMonthScreen.value = e.localizedMessage
            } finally {

                _isLoading.value = false
            }
        }
    }

    fun getAllMonths() {

        viewModelScope.launch {

            try {

                _allMonths.value = repo.getAllMonthsLocal()

                if (!(_allMonths.value.contains(_month.value))) {

                    val newMonthId = System.currentTimeMillis().formatDate("yyyy-MM")
                    _allMonths.value += Month(id = newMonthId)
                    _monthId.value = newMonthId
                }
            } catch (e: Exception) {

                _errorMessageMonthScreen.value = e.localizedMessage
            }
        }
    }

    private suspend fun sendPushNotification(title: String, price: Float) {

        try {
            val pushNotification = PushNotification(
                data = NotificationData(
                    title = title,
                    message = price.formatPrice()
                ),
                to = "/topics/$SLEDI_PARI_TOPIC"
            )

            firebaseApi.sendPushNotification(pushNotification)
        } catch (e: Exception) { }
    }

    fun addSpending(title: Pair<String, String>, price: Float, rgbColor: Triple<Float, Float, Float>, sendNotification: Boolean = false, post: Boolean = true) {
        _isLoading.value = true

        viewModelScope.launch {

            try {

                val request = PostSpendingRequest(
                    monthId = System.currentTimeMillis().formatDate("yyyy-MM"),
                    title = title.second,
                    price = price
                )

                val addSpendingResult = repo.postSpending(request, post)
                _isSpendingSuccessful.value = addSpendingResult

                if (addSpendingResult) {
                    repo.addTransactionInHistory(
                        Transaction(
                            price = price,
                            title = title.second,
                            red = rgbColor.first,
                            green = rgbColor.second,
                            blue = rgbColor.third,
                            undo = !post,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                    if (sendNotification) {
                        sendPushNotification(title.first, price)
                    }
                }
            } catch (e: Exception) {

                _errorMessageMonthScreen.value = e.localizedMessage
            } finally {

                _isLoading.value = false
                _hasCompleted.value = true
            }
        }
    }

    fun changeCategory(name: String) {

        _currentCategory.value = name
    }

    fun changeTotalSum(sum: Float) {

        _totalSum.value = sum
    }

    fun changeList(list: List<Pair<Pair<Float, String>, Color>>) {

        _currentList.value = list
    }

    fun updateMonthId(monthId: String) {

        _monthId.value = monthId
    }

    fun resetHasCompleted() {

        _hasCompleted.value = false
    }

    fun resetErrorState() {

        _errorMessageMonthScreen.value = null
    }
}