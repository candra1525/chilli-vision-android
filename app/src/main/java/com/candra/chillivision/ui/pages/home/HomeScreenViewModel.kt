package com.candra.chillivision.ui.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.repository.ChilliVisionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    fun getPreferences() = repository.getPreferences()
}