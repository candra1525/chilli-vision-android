package com.candra.chillivision.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.network.ApiService
import com.candra.chillivision.data.preferences.UserPreferences
import com.candra.chillivision.data.response.DeletePhotoProfileResponse
import com.candra.chillivision.data.response.LoginResponse
import com.candra.chillivision.data.response.LogoutResponse
import com.candra.chillivision.data.response.RegisterResponse
import com.candra.chillivision.data.response.UpdateAccountUserResponse
import com.candra.chillivision.data.response.UpdatePasswordUserResponse
import com.candra.chillivision.data.response.UpdatePhotoAccountUserResponse
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetAllResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ChilliVisionRepository constructor(
    private val preferences: UserPreferences, private val apiService: ApiService
) {
    // Preferences
    suspend fun savePreferences(
        token: String, id: String, fullname: String, no_handphone: String, image : String
    ) = preferences.savePreferences(token, id, fullname, no_handphone, image)

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
        fullname: String, no_handphone: String, password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.registerUser(fullname, no_handphone, password)
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

    // Update Account User
    fun setUpdateAccountUser(
        id: String,
        fullname: String,
        no_handphone: String,
    ): LiveData<Result<UpdateAccountUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.updateAccountUser(id, fullname, no_handphone)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }

    // Update Photo Account User
    fun setUpdatePhotoAccountUser(
        id : String,
        image : MultipartBody.Part
    ) : LiveData<Result<UpdatePhotoAccountUserResponse>> = liveData {
        emit(Result.Loading)
        try{
            val response = apiService.updatePhotoAccountUser(id, image)
            emit(Result.Success(response))
        }
        catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }

    }

    // Delete Photo Profile
    fun setDeletePhotoProfile(id: String): LiveData<Result<DeletePhotoProfileResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.deletePhotoProfile(id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }

    // Ubah Password
    fun setUbahPassword(
        old_password: String, password: String, id: String
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

    // Get All Subscriptions
    fun getAllSubscriptions() : LiveData<Result<SubscriptionsGetAllResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllSubscriptions()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }

}