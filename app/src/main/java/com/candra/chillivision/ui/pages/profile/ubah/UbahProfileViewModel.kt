package com.candra.chillivision.ui.pages.profile.ubah

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository

class UbahProfileViewModel(private val repository: ChilliVisionRepository) : ViewModel() {
    fun updateAccountUser(fullname: String, email: String, no_handphone: String, id: String) =
        repository.setUbahProfile(fullname, email, no_handphone, id)

    // get pref
    fun getPreferences() = repository.getPreferences()

    // Set Pref
    suspend fun savePreferences(token: String, id : String, fullname : String, no_handphone: String, email : String) = repository.savePreferences(token, id, fullname, no_handphone, email)
}