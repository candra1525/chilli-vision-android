package com.candra.chillivision.ui.pages.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.chillivision.data.repository.ChilliVisionRepository
import com.candra.chillivision.data.response.LoginResponse
import kotlinx.coroutines.launch
import com.candra.chillivision.data.common.Result

class LoginScreenViewModel(private val repository: ChilliVisionRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun loginUser(noHandphone: String, password: String) {
        repository.setLogin(noHandphone, password).observeForever { result ->
            _loginResult.postValue(result)

            if (result is Result.Success) {
                val dataLogin = result.data.data
                val token = dataLogin?.token
                val id = dataLogin?.id.orEmpty()
                val fullname = dataLogin?.fullname.orEmpty()
                val noHp = dataLogin?.noHandphone.orEmpty()
                val image = dataLogin?.imageUrl?.takeIf { it != "null" } ?: ""

                if (token != null) {
                    savePreferences(token, id, fullname, noHp, image)
                    Log.d("Token Berhasil Disimpan", "login: $token")
                }
            }
        }
    }

    private fun savePreferences(token: String, id: String, fullname: String, noHandphone: String, image: String) {
        viewModelScope.launch {
            repository.savePreferences(token, id, fullname, noHandphone, image)
        }
    }
}
