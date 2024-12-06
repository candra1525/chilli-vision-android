package com.candra.chillivision.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.network.ApiService
import com.candra.chillivision.data.preferences.UserPreferences
import com.candra.chillivision.data.response.LoginResponse
import com.candra.chillivision.data.response.LogoutResponse
import com.candra.chillivision.data.response.RegisterResponse
import com.candra.chillivision.data.response.UpdateAccountUserResponse
import com.candra.chillivision.data.response.UpdatePasswordUserResponse

class ChilliVisionRepository constructor(
    private val preferences: UserPreferences, private val apiService: ApiService
) {
    // Preferences
    suspend fun savePreferences(
        token: String, id: String, fullname: String, no_handphone: String, email: String
    ) = preferences.savePreferences(token, id, fullname, no_handphone, email)

    suspend fun clearToken() = preferences.clearPreferences()

    fun getPreferences() = preferences.getPreferences()

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
        fullname: String, email: String = null.toString(), no_handphone: String, password: String
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

    // Ubah Profile
    fun setUbahProfile(
        id: String, fullname: String, email: String, no_handphone: String
    ): LiveData<Result<UpdateAccountUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.updateAccountUser(
                fullname, email, no_handphone, id
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }

    // Ubah Password
    fun setUbahPassword(
        id: String, old_password: String, password: String
    ): LiveData<Result<UpdatePasswordUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.updatePasswordUser(
                old_password, password, id
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }
}