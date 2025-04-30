package com.candra.chillivision.ui.pages.scan.confirm_scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.repository.ChilliVisionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ConfirmScanScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    private val _countUsageDetect = MutableStateFlow(0)
    val countUsageDetect: StateFlow<Int> get() = _countUsageDetect

    init {
        // Ambil nilai awal countUsageDetect dari preferences (Flow<String>)
        viewModelScope.launch {
            repository.getCountUsageDetect().collect { stringCount ->
                val intCount = stringCount.toIntOrNull() ?: 0
                _countUsageDetect.emit(intCount)
            }
        }
    }

    fun sendPrediction(
        file: MultipartBody.Part
    ) = repository.sendPrediction(
        file = file
    )

    fun getPreferences() = repository.getPreferences()

    fun incrementCountUsageDetect() {
        viewModelScope.launch {
            val currentCount = _countUsageDetect.value
            val newCount = currentCount + 1
            repository.setCountUsageDetect(newCount.toString())
            _countUsageDetect.emit(newCount)
        }
    }

}


