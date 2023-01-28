package com.example.sledipari.ui.settings.currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.data.models.toMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel
@Inject constructor(private val repo: MonthRepository): ViewModel() {

    private val _rates = MutableStateFlow(mapOf<String, Double>())
    val rates = _rates.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val currencyLoading = _isLoading.asStateFlow()

    fun getRates() {

        _isLoading.value = true

        viewModelScope.launch {
            repo.getRates()?.rates?.toMap()?.let {
                _rates.value = it
            }

            _isLoading.value = false
        }
    }
}