package com.example.sledipari.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.utility.Constants.DELETE_HISTORY_INTERVAL
import com.example.sledipari.utility.Constants.GET_RATES_INTERVAL
import com.example.sledipari.utility.Constants.HISTORY_TIMESTAMP
import com.example.sledipari.utility.Constants.RATES_TIMESTAMP
import com.example.sledipari.utility.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAllMonthsViewModel
@Inject constructor(
    private val repo: MonthRepository,
    private val sharedPrefs: SharedPreferences
): ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _completed = MutableStateFlow(false)
    val completed = _completed.asStateFlow()

    fun restoreAllMonths() {
        _loading.value = true

        viewModelScope.launch {

            val gettingMonths = async { repo.getAllMonths() }
            var deletingHistory: Deferred<Unit>? = null
            var getRates: Deferred<Unit>? = null

            if (System.currentTimeMillis() >= sharedPrefs.getLong(HISTORY_TIMESTAMP, 0L)) {
                deletingHistory = async {
                    repo.deleteSomeHistory()
                    sharedPrefs.edit().putLong(
                        HISTORY_TIMESTAMP,
                        System.currentTimeMillis() + DELETE_HISTORY_INTERVAL
                    ).apply()
                }
            }

            if (System.currentTimeMillis() >= sharedPrefs.getLong(RATES_TIMESTAMP, 0L)) {
                getRates = async {
                    repo.saveCurrencyRates()
                    sharedPrefs.edit().putLong(
                        RATES_TIMESTAMP,
                        System.currentTimeMillis() + GET_RATES_INTERVAL
                    ).apply()
                }
            }

            gettingMonths.await()
            deletingHistory?.await()
            getRates?.await()

            _loading.value = false
            _completed.value = true
        }
    }
}