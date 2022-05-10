package com.example.sledipari.ui.main

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sledipari.R
import com.example.sledipari.api.models.PostSpendingRequest
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.data.models.Month
import com.example.sledipari.utility.Resource
import com.example.sledipari.utility.formatDate
import com.example.sledipari.utility.toList
import com.example.sledipari.utility.totalSum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetMonthViewModel
@Inject constructor(
    private val repo: MonthRepository,
    context: Context
): ViewModel() {

    //******
    val o = Month(food = 180f, home = 90f, restaurant = 90f, clothes = 10f, workout = 120f, machove = 5f, entertainment = 30f, tehnika = 12f, id = "05-2022")
    val b = listOf(Month(id = "05-2022"), Month(id = "04-2022"),Month(id = "03-2022"),Month(id = "02-2022"),Month(id = "01-2022"))
    //****

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    var month = mutableStateOf<Month?>(null)
    var monthId = mutableStateOf(System.currentTimeMillis().formatDate("MM-yyyy"))
    var allMonths = mutableStateOf(b)

    var isSpendingSuccessful = mutableStateOf(false)
    var hasCompleted = mutableStateOf(false)

    var currentCategory = mutableStateOf(context.getString(R.string.all))
    var totalSum = mutableStateOf(month.value?.totalSum() ?: 0f)
    var currentList = mutableStateOf(month.value?.toList() ?: listOf())

    fun getMonthLocal() {

        viewModelScope.launch {
            month.value = repo.getCurrentMonthLocal()
        }
    }

    fun getMonth(timestamp: String) {
        isLoading.value = true

        viewModelScope.launch {

            when (val monthResult = repo.getMonth(timestamp)) {
                is Resource.Success -> {

                    // we force unwrap which is not a good practice
                    // but here since we are in success state
                    // it is guaranteed the data is not null
                    month.value = monthResult.data
                    monthId.value = month.value!!.id
                }

                is Resource.Error -> {

                    errorMessage.value = monthResult.message
                }
            }

            isLoading.value = false
        }
    }

    fun getAllMonths() {

        viewModelScope.launch {

            when (val allMonthsResult = repo.getAllMonths()) {
                is Resource.Success -> {

                    // we force unwrap which is not a good practice
                    // but here since we are in success state
                    // it is guaranteed the data is not null
                    allMonths.value = allMonthsResult.data!!
                }

                is Resource.Error -> {

                    errorMessage.value = allMonthsResult.message
                }
            }
        }
    }

    fun addSpending(title: String, price: Float) {
        isLoading.value = true

        viewModelScope.launch {

            val request = PostSpendingRequest(
                monthId = System.currentTimeMillis().formatDate("MM-yyyy"),
                title = title,
                price = price
            )

            when (val addSpendingResult = repo.postSpending(request)) {
                is Resource.Success -> {

                    isSpendingSuccessful.value = addSpendingResult.data ?: false
                }

                is Resource.Error -> {

                    errorMessage.value = addSpendingResult.message
                }
            }

            isLoading.value = false
            hasCompleted.value = true
        }
    }

    fun changeCategory(name: String) {

        currentCategory.value = name
    }

    fun changeTotalSum(sum: Float) {

        totalSum.value = sum
    }

    fun changeList(list: List<Pair<Pair<Float, String>, Color>>) {

        currentList.value = list
    }
}