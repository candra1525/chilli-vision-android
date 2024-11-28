package com.candra.chillivision.ui.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.candra.chillivision.data.model.TokenModel
import com.candra.chillivision.data.repository.ChilliVisionRepository

class NavigationViewModel(private val repository : ChilliVisionRepository) : ViewModel() {
    fun getSessionToken() :LiveData<TokenModel> = repository.getToken().asLiveData()
}