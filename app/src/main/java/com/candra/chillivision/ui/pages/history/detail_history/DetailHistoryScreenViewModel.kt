package com.candra.chillivision.ui.pages.history.detail_history

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository

class DetailHistoryScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    fun deleteHistory(idHistory: String) = repository.setDeteleHistory(idHistory)
}