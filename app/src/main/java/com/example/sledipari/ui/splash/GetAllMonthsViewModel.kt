package com.example.sledipari.ui.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.utility.Constants.DELETE_HISTORY_INTERVAL
import com.example.sledipari.utility.Constants.HISTORY_TIMESTAMP
import com.example.sledipari.utility.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAllMonthsViewModel
@Inject constructor(
    private val repo: MonthRepository,
    private val sharedPrefs: SharedPreferences
): ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _completed = MutableStateFlow(false)
    val completed: StateFlow<Boolean> = _completed

    fun restoreAllMonths() {
        _loading.value = true

        viewModelScope.launch {

            val gettingMonths = async { repo.getAllMonths() }
            var deletingHistory: Deferred<Unit>? = null

            if (System.currentTimeMillis() >= sharedPrefs.getLong(HISTORY_TIMESTAMP, 0L)) {
                deletingHistory = async {
                    repo.deleteSomeHistory()
                    sharedPrefs.edit().putLong(
                        HISTORY_TIMESTAMP,
                        System.currentTimeMillis() + DELETE_HISTORY_INTERVAL
                    ).apply()
                }
            }

            gettingMonths.await()
            deletingHistory?.await()

            _loading.value = false
            _completed.value = true
        }
    }
}