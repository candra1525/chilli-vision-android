package com.candra.chillivision.data.network

import com.candra.chillivision.data.response.LoginResponse
import com.candra.chillivision.data.response.LogoutResponse
import com.candra.chillivision.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

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
        @Field("email") email: String,
        @Field("no_handphone") no_handphone: String,
        @Field("password") password: String
    ): RegisterResponse

    // Logout User
    @FormUrlEncoded
    @POST("auth/logout")
    suspend fun logoutUser(
        @Header("Authorization") token: String
    ): LogoutResponse
}