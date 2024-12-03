package com.candra.chillivision.ui.pages.login

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository

class LoginScreenViewModel (private val repository : ChilliVisionRepository) : ViewModel()
{
    fun setLogin(no_handphone : String, password : String) = repository.setLogin(no_handphone, password)

    suspend fun savePreferences(token: String, id : String, fullname : String, no_handphone: String, email : String) = repository.savePreferences(token, id, fullname, no_handphone, email)
}