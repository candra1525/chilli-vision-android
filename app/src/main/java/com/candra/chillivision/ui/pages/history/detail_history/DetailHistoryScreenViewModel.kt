package com.candra.chillivision.ui.pages.history.detail_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.repository.ChilliVisionRepository
import com.candra.chillivision.data.response.historyAnalysis.DetailHistoryResponse
import com.candra.chillivision.data.response.notification.NotificationAllResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailHistoryScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    fun deleteHistory(idHistory: String) = repository.setDeteleHistory(idHistory)

    private val _detailHistory = MutableStateFlow<Result<DetailHistoryResponse>>(
        Result.Loading
    )
    val detailHistory: StateFlow<Result<DetailHistoryResponse>> = _detailHistory.asStateFlow()

    fun fetchDetailHistory(idHistory: String) {
        viewModelScope.launch {
            repository.getDetailHistory(idHistory = idHistory)
                .collect { result ->
                    _detailHistory.value = result
                }
        }
    }

}