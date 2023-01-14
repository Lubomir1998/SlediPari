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
import com.example.sledipari.utility.Resource
import com.example.sledipari.utility.extensions.formatPrice
import com.example.sledipari.utility.extensions.toList
import com.example.sledipari.utility.extensions.totalSum
import com.example.sledipari.utility.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

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

        viewModelScope.launch {
            _month.value = repo.getMonthLocal(monthId)
        }
    }

    fun getMonth(timestamp: String) {
        _isLoading.value = true

        viewModelScope.launch {

            when (val monthResult = repo.getMonth(timestamp)) {
                is Resource.Success -> {

                    // we force unwrap which is not a good practice
                    // but here since we are in success state
                    // it is guaranteed the data is not null
                    _month.value = monthResult.data
                    _monthId.value = _month.value!!.id
                }

                is Resource.Error -> {

                    _errorMessage.value = monthResult.message
                }
            }

            _isLoading.value = false
        }
    }

    fun getAllMonths() {

        viewModelScope.launch {

            when (val allMonthsResult = repo.getAllMonthsLocal()) {
                is Resource.Success -> {

                    // we force unwrap which is not a good practice
                    // but here since we are in success state
                    // it is guaranteed the data is not null
                    _allMonths.value = allMonthsResult.data!!
                }

                is Resource.Error -> {

                    _errorMessage.value = allMonthsResult.message
                }
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

            val request = PostSpendingRequest(
                monthId = System.currentTimeMillis().formatDate("yyyy-MM"),
                title = title.second,
                price = price
            )

            when (val addSpendingResult = repo.postSpending(request, post)) {
                is Resource.Success -> {

                    _isSpendingSuccessful.value = addSpendingResult.data ?: false

                    if (addSpendingResult.data!!) {
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
                }

                is Resource.Error -> {

                    _errorMessage.value = addSpendingResult.message
                }
            }

            _isLoading.value = false
            _hasCompleted.value = true
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
}