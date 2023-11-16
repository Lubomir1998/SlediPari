package com.example.sledipari.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.utility.Constants.DELETE_HISTORY_INTERVAL
import com.example.sledipari.utility.Constants.GET_RATES_INTERVAL
import com.example.sledipari.utility.Constants.HISTORY_TIMESTAMP
import com.example.sledipari.utility.Constants.USE_LOCALHOST
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

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.SPLASH)
    val state: StateFlow<State> = _state

    private val _ratesTimestamp = MutableStateFlow(0L)
    val ratesTimestamp = _ratesTimestamp.asStateFlow()

    private val _getRatesException = MutableStateFlow<Exception?>(null)
    val getRatesException = _getRatesException.asStateFlow()

    private val _getMonthsException = MutableStateFlow<Exception?>(null)
    val getMonthsException = _getMonthsException.asStateFlow()

    private fun getState(): String? {

        return sharedPrefs.getString("token", null)
    }

    fun applicationStartOperation() {
        _loading.value = true

        viewModelScope.launch {

            if (getState() == null) {
                _state.value = State.LOGIN
                _loading.value = false
                return@launch
            }

            _ratesTimestamp.value = repo.getRates()?.timestamp ?: 0L

            var deletingHistory: Deferred<Unit>? = null
            val gettingMonths: Deferred<Unit>?
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

            gettingMonths = async {
                try {
                    repo.getMonthsOnStart()
                } catch (e: Exception) {
                    _getMonthsException.value = e
                }
            }

            if (System.currentTimeMillis() >= (repo.getRates()?.timestamp ?: 0L) + GET_RATES_INTERVAL) {
                if (!USE_LOCALHOST) {
                    getRates = async {
                        try {
                            repo.saveCurrencyRates()
                        } catch (e: Exception) {
                            _getRatesException.value = e
                        }
                    }
                }
            }

            gettingMonths.await()
            deletingHistory?.await()
            getRates?.await()

            _loading.value = false
            _state.value = State.MAIN
        }
    }

    enum class State {
        SPLASH,
        LOGIN,
        MAIN
    }
}