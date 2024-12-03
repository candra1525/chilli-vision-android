package com.candra.chillivision.ui.pages.register

import androidx.lifecycle.ViewModel
import com.candra.chillivision.data.repository.ChilliVisionRepository

class RegisterScreenViewModel (private val repository : ChilliVisionRepository) : ViewModel(){
    fun setRegister (fullname: String, email: String, no_handphone: String, password: String) = repository.setRegister(fullname, email, no_handphone, password)
}