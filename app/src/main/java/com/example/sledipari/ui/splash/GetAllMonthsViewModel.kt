package com.example.sledipari.ui.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sledipari.data.MonthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAllMonthsViewModel
@Inject constructor(private val repo: MonthRepository): ViewModel() {

    var isLoading = mutableStateOf(false)

    fun restoreAllMonths(completion: (() -> Unit)) {
        isLoading.value = true

        viewModelScope.launch {

            repo.restoreAllMonths()
            isLoading.value = false
            completion()
        }
    }
}