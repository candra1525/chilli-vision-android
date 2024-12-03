package com.candra.chillivision.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.network.ApiService
import com.candra.chillivision.data.preferences.UserPreferences
import com.candra.chillivision.data.response.LoginResponse
import com.candra.chillivision.data.response.LogoutResponse
import com.candra.chillivision.data.response.RegisterResponse

class ChilliVisionRepository constructor(
    private val preferences: UserPreferences,
    private val apiService: ApiService
) {
    // Preferences
    suspend fun savePreferences(token: String, id: String, fullname: String, no_handphone: String, email: String) = preferences.savePreferences(token, id, fullname, no_handphone, email)
    suspend fun clearToken() = preferences.clearPreferences()

    fun getToken() = preferences.getPreferences()

    // Login User
    fun setLogin(no_handphone: String, password: String): LiveData<Result<LoginResponse>> =
        liveData {
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
        fullname: String,
        email: String,
        no_handphone: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.registerUser(fullname, email, no_handphone, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }

    // Logout User
    fun setLogout(): LiveData<Result<LogoutResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.logoutUser()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }
}