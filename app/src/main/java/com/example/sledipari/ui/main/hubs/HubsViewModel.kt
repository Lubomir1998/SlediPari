package com.example.sledipari.ui.main.hubs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sledipari.data.MonthRepository
import com.example.sledipari.data.models.Hub
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HubsViewModel
@Inject constructor(
    private val repo: MonthRepository
): ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _hubs: MutableStateFlow<List<Hub>> = MutableStateFlow(listOf())
    val hubs = _hubs.asStateFlow()

    private val _getHubsException = MutableStateFlow<Exception?>(null)
    val getHubsException = _getHubsException.asStateFlow()

    fun getHubs() {

        _loading.value = true
        viewModelScope.launch {

            try {

                _hubs.value = repo.getAllHubsForUser()
            } catch (e: Exception) {

                _getHubsException.value = e
            } finally {

                _loading.value = false
            }

        }
    }
}