package com.candra.chillivision.ui.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.candra.chillivision.data.repository.ChilliVisionRepository

class HomeScreenViewModel (private val repository : ChilliVisionRepository) : ViewModel(){
    fun getToken() = repository.getToken()
}