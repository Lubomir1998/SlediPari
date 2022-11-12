package com.example.sledipari.ui.settings.history

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sledipari.R
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.data.models.Transaction
import com.example.sledipari.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
@SuppressLint("StaticFieldLeak")
@Inject constructor(
    private val repo: MonthRepository,
    private val context: Context
): ViewModel() {

    var history = mutableStateOf(listOf<Transaction>())

    var error = mutableStateOf<String?>(null)

    var isLoading = mutableStateOf(false)

    fun getHistory() {

        isLoading.value = true

        viewModelScope.launch {

            try {
                history.value = repo.getHistory()
            } catch (e: Exception) {
                error.value = e.localizedMessage ?: context.getString(R.string.something_went_wrong)
            } finally {
                isLoading.value = false
            }
        }
    }
}