package com.candra.chillivision.ui.pages.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.repository.ChilliVisionRepository
import com.candra.chillivision.data.response.historyAnalysis.HistoryAnalysisResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {


    private val _historyAnalisis = MutableStateFlow<Result<HistoryAnalysisResponse>>(
        Result.Loading
    )
    val historyAnalisis: StateFlow<Result<HistoryAnalysisResponse>> = _historyAnalisis.asStateFlow()

    fun fetchHistoryAnalisis(idUser: String) {
        viewModelScope.launch {
            repository.getHistoryAnalysis(idUser = idUser)
                .collect { result ->
                    _historyAnalisis.value = result
                }
        }
    }

    fun getPreferences() = repository.getPreferences()
}