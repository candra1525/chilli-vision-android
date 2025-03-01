package com.candra.chillivision.ui.pages.home

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository

class HomeScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    fun getPreferences() = repository.getPreferences()
}