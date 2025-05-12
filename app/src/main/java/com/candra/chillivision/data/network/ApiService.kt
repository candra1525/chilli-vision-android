package com.candra.chillivision.data.network

import com.candra.chillivision.data.chat.ChatRequest
import com.candra.chillivision.data.chat.ChatResponse
import com.candra.chillivision.data.response.CountHistoryUserResponse
import com.candra.chillivision.data.response.CreateSubscriptionUserResponse
import com.candra.chillivision.data.response.DeletePhotoProfileResponse
import com.candra.chillivision.data.response.historyAnalysis.HistoryDeleteResponse
import com.candra.chillivision.data.response.ListHistorySubscriptionActiveResponse
import com.candra.chillivision.data.response.ListHistorySubscriptionResponse
import com.candra.chillivision.data.response.LoginResponse
import com.candra.chillivision.data.response.LogoutResponse
import com.candra.chillivision.data.response.RegisterResponse
import com.candra.chillivision.data.response.UpdateAccountUserResponse
import com.candra.chillivision.data.response.UpdatePasswordUserResponse
import com.candra.chillivision.data.response.UpdatePhotoAccountUserResponse
import com.candra.chillivision.data.response.analysisResult.AnalisisResultResponse
import com.candra.chillivision.data.response.historyAnalysis.CreateHistoryRequest
import com.candra.chillivision.data.response.historyAnalysis.CreateHistoryResponse
import com.candra.chillivision.data.response.historyAnalysis.DetailHistoryResponse
import com.candra.chillivision.data.response.historyAnalysis.HistoryAnalysisResponse
import com.candra.chillivision.data.response.notification.NotificationAllResponse
import com.candra.chillivision.data.response.subscriptions.ActiveSubscrpitionUserResponse
import com.candra.chillivision.data.response.subscriptions.CreateHistorySubscriptionResponse
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetAllResponse
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetDetailResponse
import com.candra.chillivision.data.response.updateStatusSubsUser.UpdateStatusSubscriptionUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    // Login User
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginUser(
        @Field("no_handphone") no_handphone: String,
        @Field("password") password: String
    ): LoginResponse

    // Register User
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun registerUser(
        @Field("fullname") fullname: String,
        @Field("no_handphone") no_handphone: String,
        @Field("password") password: String
    ): RegisterResponse

    // Update Account User
    @FormUrlEncoded
    @POST("user/update-account/{id}")
    suspend fun updateAccountUser(
        @Path("id") id: String,
        @Field("fullname") fullname: String,
        @Field("no_handphone") no_handphone: String,
    ): UpdateAccountUserResponse

    // Update Photo Account User
    @Multipart
    @POST("user/update-photo-account/{id}")
    suspend fun updatePhotoAccountUser(
        @Path("id") id: String,
        @Part image: MultipartBody.Part? = null
    ): UpdatePhotoAccountUserResponse

    @DELETE("user/delete-photo-account/{id}")
    suspend fun deletePhotoProfile(
        @Path("id") id: String
    ): DeletePhotoProfileResponse

    // Update Password User
    @FormUrlEncoded
    @PUT("user/change-password/{id}")
    suspend fun updatePasswordUser(
        @Field("old_password") old_password: String,
        @Field("password") password: String,
        @Path("id") id: String
    ): UpdatePasswordUserResponse

    // Logout User
    @POST("user/logout")
    @Headers("Content-Type: application/json")
    suspend fun logoutUser(): LogoutResponse

    // Check Subscription Active
    @GET("active-subscriptions/{idUser}")
    suspend fun checkSubscriptionActive(
        @Path("idUser") idUser: String
    ): ActiveSubscrpitionUserResponse

    // Subscriptions
    // Get All Subscriptions
    @GET("subscriptions/all")
    suspend fun getAllSubscriptions(): SubscriptionsGetAllResponse

    // Get Detail Subscription
    @GET("subscriptions/detail/{id}")
    suspend fun getDetailSubscription(
        @Path("id") id: String
    ): SubscriptionsGetDetailResponse

    // History
    @GET("history/history-by-user/{idUser}")
    suspend fun getHistory(
        @Path("idUser") idUser: String
    ): HistoryAnalysisResponse

    // Create History
    @POST("history/create")
    suspend fun createHistory(
        @Body request: CreateHistoryRequest
    ): CreateHistoryResponse

    //  Delete History
    @DELETE("history/delete/{id}")
    suspend fun deleteHistory(
        @Path("id") id: String
    ): HistoryDeleteResponse

    //  Detail History
    @GET("history/detail/{id}")
    suspend fun getDetailHistory(
        @Path("id") id: String
    ): DetailHistoryResponse

    // Count History User
        @GET("history/count/{idUser}")
    suspend fun getCountHistoryUser(
        @Path("idUser") idUser: String
    ): CountHistoryUserResponse


    // History Subscription
    // Update Status Subs
    @FormUrlEncoded
    @PUT("history-subscriptions/update-status/{idSubscription}")
    suspend fun updateStatusSubscriptionUser(
        @Field("status") status: String,
        @Path("idSubscription") idSubscription: String
    ): UpdateStatusSubscriptionUserResponse


    // Get All History Subscription from User
    @GET("history-subscriptions/list-history/{idUser}")
    suspend fun getAllHistorySubscriptionUser(
        @Path("idUser") idUser: String
    ): ListHistorySubscriptionResponse

    // Get History Subscription Active from User
    @GET("history-subscriptions/list-active/{idUser}")
    suspend fun getHistorySubscriptionUserActive(
        @Path("idUser") idUser: String
    ): ListHistorySubscriptionActiveResponse

    @Multipart
    @POST("history-subscriptions/create")
    suspend fun createHistorySubscription(
        @Part("user_id") user_id: RequestBody,
        @Part("subscription_id") subscription_id: RequestBody,
        @Part("start_date") start_date: RequestBody,
        @Part("end_date") end_date: RequestBody,
        @Part image_transaction: MultipartBody.Part
    ): CreateHistorySubscriptionResponse

    // Notification
    @GET("notification/all")
    suspend fun getAllNotification(): NotificationAllResponse

    // Chat AI
    @POST("chat")
    suspend fun sendMessage(
        @Body request : ChatRequest
    ) : ChatResponse

    @Multipart
    @POST("detect")
    suspend fun sendPredict(
        @Part file: MultipartBody.Part,
    ) : AnalisisResultResponse
}

// Create History
//    @Multipart
//    @POST("history/create")
//    suspend fun createHistory(
//        @Part("title") title: String,
//        @Part("image") image: MultipartBody.Part,
//        @Part("description") description: String,
//        @Part("user_id") user_id: String
//    ): CreateHistoryUserResponse

// Count History
//    @GET("history/count/{idUser}")
//    suspend fun countHistory(
//        @Path("idUser") idUser: String
//    ): CountHistoryUserResponse