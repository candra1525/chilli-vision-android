package com.candra.chillivision.ui.pages.profile

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository

class ProfileScreenViewModel (private val repository : ChilliVisionRepository) : ViewModel(){
    fun setLogout() = repository.setLogout()
    suspend fun clearToken() = repository.clearToken()
}