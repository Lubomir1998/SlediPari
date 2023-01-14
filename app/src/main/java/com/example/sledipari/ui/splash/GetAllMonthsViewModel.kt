package com.example.sledipari.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.utility.Constants.DELETE_HISTORY_INTERVAL
import com.example.sledipari.utility.Constants.GET_RATES_INTERVAL
import com.example.sledipari.utility.Constants.HISTORY_TIMESTAMP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val _getRatesException = MutableStateFlow<Exception?>(null)
    val getRatesException = _getRatesException.asStateFlow()

    private val _getMonthsException = MutableStateFlow<Exception?>(null)
    val getMonthsException = _getMonthsException.asStateFlow()

    fun restoreAllMonths() {
        _loading.value = true

        viewModelScope.launch {

            var deletingHistory: Deferred<Unit>? = null
            var gettingMonths: Deferred<Unit>? = null
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

            try {
                gettingMonths = async { repo.getAllMonths() }
            }  catch (e: Exception) {
                _getMonthsException.value = e
            }

            if (System.currentTimeMillis() >= (repo.getRates()?.timestamp ?: 0L) + GET_RATES_INTERVAL) {
                try {
                    getRates = async {
                        repo.saveCurrencyRates()
                    }
                } catch (e: Exception) {
                    _getRatesException.value = e
                }
            }

            gettingMonths?.await()
            deletingHistory?.await()
            getRates?.await()

            _loading.value = false
            _completed.value = true
        }
    }
}