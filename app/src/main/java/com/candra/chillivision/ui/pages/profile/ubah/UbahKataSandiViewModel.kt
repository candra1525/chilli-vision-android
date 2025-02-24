package com.candra.chillivision.ui.pages.profile.ubah

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository

class UbahKataSandiViewModel (private val repository : ChilliVisionRepository) : ViewModel()
{
    fun updatePasswordUser(old_password : String, password: String, id : String) = repository.setUbahPassword(old_password, password, id)

    fun getPreferences() = repository.getPreferences()

}