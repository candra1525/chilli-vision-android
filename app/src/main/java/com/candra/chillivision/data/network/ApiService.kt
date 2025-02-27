package com.candra.chillivision.data.network

import com.candra.chillivision.data.response.CountHistoryUserResponse
import com.candra.chillivision.data.response.CreateHistoryUserResponse
import com.candra.chillivision.data.response.CreateSubscriptionUserResponse
import com.candra.chillivision.data.response.DeletePhotoProfileResponse
import com.candra.chillivision.data.response.DetailHistoryUserResponse
import com.candra.chillivision.data.response.HistoryDeleteUserResponse
import com.candra.chillivision.data.response.HistoryUserResponse
import com.candra.chillivision.data.response.ListHistorySubscriptionActiveResponse
import com.candra.chillivision.data.response.ListHistorySubscriptionResponse
import com.candra.chillivision.data.response.LoginResponse
import com.candra.chillivision.data.response.LogoutResponse
import com.candra.chillivision.data.response.RegisterResponse
import com.candra.chillivision.data.response.SubscriptionUserResponse
import com.candra.chillivision.data.response.UpdateAccountUserResponse
import com.candra.chillivision.data.response.UpdatePasswordUserResponse
import com.candra.chillivision.data.response.UpdatePhotoAccountUserResponse
import com.candra.chillivision.data.response.historyAnalysis.HistoryAnalysisResponse
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetAllResponse
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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
    @FormUrlEncoded
    @POST("auth/logout")
    suspend fun logoutUser(): LogoutResponse


    // Subscriptions
    // Get All Subscriptions
    @GET("subscriptions/all")
    suspend fun getAllSubscriptions(): SubscriptionsGetAllResponse

    // Get History User
    @GET("history/history-by-user/{idUser}")
    suspend fun getHistory(
        @Path("idUser") idUser: String
    ): HistoryAnalysisResponse

    // Get Detail History
    @GET("history/detail{id}")
    suspend fun getDetailHistory(
        @Path("id") id: String
    ): DetailHistoryUserResponse

    // Create History
    @Multipart
    @POST("history/create")
    suspend fun createHistory(
        @Part("title") title: String,
        @Part("image") image: MultipartBody.Part,
        @Part("description") description: String,
        @Part("user_id") user_id: String
    ): CreateHistoryUserResponse

    // Count History
    @GET("history/count/{idUser}")
    suspend fun countHistory(
        @Path("idUser") idUser: String
    ): CountHistoryUserResponse

    // Delete History
    @DELETE("history/delete/{id}")
    suspend fun deleteHistory(
        @Path("id") id: String
    ): HistoryDeleteUserResponse


    // Get Subscription
    @GET("subscriptions/user/{idUser}")
    suspend fun getSubscription(
        @Path("idUser") idUser: String
    ): SubscriptionUserResponse

    // Get Detail Subscription
    @GET("subscriptions/detail/{id}")
    suspend fun getDetailSubscription(
        @Path("id") id: String
    ): SubscriptionsGetDetailResponse

    // Create Subscription
    @Multipart
    @POST("subscriptions/create")
    suspend fun createSubscription(
        @Part("title") title: String,
        @Part("image_transaction") image_transaction: MultipartBody.Part,
        @Part("start_date") start_date: String,
        @Part("end_date") end_date: String,
        @Part("user_id") user_id: String,
    ): CreateSubscriptionUserResponse

    // History Subscription
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
}