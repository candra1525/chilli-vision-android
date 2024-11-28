package com.candra.chillivision.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.network.ApiService
import com.candra.chillivision.data.preferences.TokenPreferences
import com.candra.chillivision.data.response.LoginResponse
import com.candra.chillivision.data.response.RegisterResponse

class ChilliVisionRepository constructor(
    private val preferences: TokenPreferences,
    private val apiService : ApiService
) {
    // Preferences
    suspend fun saveToken(token: String) = preferences.saveToken(token)
    suspend fun clearToken() = preferences.clearToken()

    fun getToken() = preferences.getToken()

    // Login User
    fun setLogin(no_handphone : String, password : String) : LiveData<Result<LoginResponse>> = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.loginUser(no_handphone, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }

    // Register User
    fun setRegister(
        fullname : String,
        email : String,
        no_handphone : String,
        password : String
    ) : LiveData<Result<RegisterResponse>> = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.registerUser(fullname, email, no_handphone, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }


}