package com.candra.chillivision.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.candra.chillivision.data.chat.ChatRequest
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.network.ApiService
import com.candra.chillivision.data.preferences.UserPreferences
import com.candra.chillivision.data.response.DeletePhotoProfileResponse
import com.candra.chillivision.data.response.ListHistorySubscriptionActiveResponse
import com.candra.chillivision.data.response.ListHistorySubscriptionResponse
import com.candra.chillivision.data.response.LoginResponse
import com.candra.chillivision.data.response.LogoutResponse
import com.candra.chillivision.data.response.RegisterResponse
import com.candra.chillivision.data.response.UpdateAccountUserResponse
import com.candra.chillivision.data.response.UpdatePasswordUserResponse
import com.candra.chillivision.data.response.UpdatePhotoAccountUserResponse
import com.candra.chillivision.data.response.historyAnalysis.HistoryAnalysisResponse
import com.candra.chillivision.data.response.historyAnalysis.HistoryDeleteResponse
import com.candra.chillivision.data.response.notification.NotificationAllResponse
import com.candra.chillivision.data.response.subscriptions.ActiveSubscrpitionUserResponse
import com.candra.chillivision.data.response.subscriptions.CreateHistorySubscriptionResponse
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetAllResponse
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ChilliVisionRepository constructor(
    private val preferences: UserPreferences, private val apiService: ApiService
) {
    // Preferences
    suspend fun savePreferences(
        token: String, id: String, fullname: String, no_handphone: String, image: String
    ) = preferences.savePreferences(token, id, fullname, no_handphone, image)

    suspend fun clearPreferences() = preferences.clearPreferences()

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
            Log.d("Logout", "Calling API logout...")
            val response = apiService.logoutUser()

            Log.d("Logout", "Logout success: $response")
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
        id: String,
        image: MultipartBody.Part
    ): LiveData<Result<UpdatePhotoAccountUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.updatePhotoAccountUser(id, image)
            emit(Result.Success(response))
        } catch (e: Exception) {
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

    fun getAllSubscriptions(): Flow<Result<SubscriptionsGetAllResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getAllSubscriptions()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }.flowOn(Dispatchers.IO)

    // Get Detail Subscription
    fun getDetailSubscription(id: String): Flow<Result<SubscriptionsGetDetailResponse>> = flow {
        emit(Result.Loading)

        try {
            val response = apiService.getDetailSubscription(id = id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }.flowOn(Dispatchers.IO)

    // History Subscription
    // Get All History Subscription from User
    fun getAllHistorySubscription(idUser: String): Flow<Result<ListHistorySubscriptionResponse>> =
        flow {
            emit(Result.Loading)
            try {
                val response = apiService.getAllHistorySubscriptionUser(idUser = idUser)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Error Occurred!"))
            }
        }.flowOn(Dispatchers.IO)

    // Get All History Subscription Active from User
    fun getHistorySubscriptionUserActive(idUser: String): Flow<Result<ListHistorySubscriptionActiveResponse>> =
        flow {
            emit(Result.Loading)
            try {
                val response = apiService.getHistorySubscriptionUserActive(idUser = idUser)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Error Occurred!"))
            }
        }.flowOn(Dispatchers.IO)

    // Get History
    fun getHistoryAnalysis(idUser: String): Flow<Result<HistoryAnalysisResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getHistory(idUser = idUser)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }.flowOn(Dispatchers.IO)

    // Create History
//    fun createHistory(
//        title : String, image : MultipartBody.Part, description : String, user_id : String
//    ) : LiveData<Result<CreateHistoryUserResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.createHistory(
//                title = title,
//                image = image,
//                description = description,
//                user_id = user_id
//            )
//            emit(Result.Success(response))
//        } catch (e: Exception) {
//            emit(Result.Error(e.message ?: "Error Occurred!"))
//        }
//    }


    // Create Subscription
    fun setCreateHistorySubscription(
        user_id: String,
        subscription_id: String,
        start_date: String,
        end_date: String,
        image_transaction: MultipartBody.Part
    ): LiveData<Result<CreateHistorySubscriptionResponse>> = liveData {
        emit(Result.Loading)
        try {
            val userIdBody = user_id.toRequestBody("text/plain".toMediaTypeOrNull())
            val subscriptionIdBody = subscription_id.toRequestBody("text/plain".toMediaTypeOrNull())
            val startDateBody = start_date.toRequestBody("text/plain".toMediaTypeOrNull())
            val endDateBody = end_date.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.createHistorySubscription(
                user_id = userIdBody,
                subscription_id = subscriptionIdBody,
                start_date = startDateBody,
                end_date = endDateBody,
                image_transaction = image_transaction
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }

    // Delete History
    fun setDeteleHistory(idHistory: String): LiveData<Result<HistoryDeleteResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.deleteHistory(idHistory)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }

    // Notification
    fun getAllNotification(): Flow<Result<NotificationAllResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getAllNotification()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error Occurred!"))
        }
    }.flowOn(Dispatchers.IO)

    // Check Subscription User
    fun checkSubscriptionActive(idUser: String): LiveData<Result<ActiveSubscrpitionUserResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.checkSubscriptionActive(idUser = idUser)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Error Occurred!"))
            }
        }

    // Chat AI
    suspend fun getChatResponse(msg: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.sendMessage(ChatRequest(msg))
                Log.d("ChatAPI", "Response: ${response.response}")
                response.response
            } catch (e: Exception) {
                Log.e("ChatAPI", "Error fetching response", e)
                "Gagal mendapatkan respons dari server."
            }
        }
    }
}