package com.candra.chillivision.data.di

import android.content.Context
import com.candra.chillivision.data.network.ApiConfig
import com.candra.chillivision.data.preferences.TokenPreferences
import com.candra.chillivision.data.preferences.dataStore
import com.candra.chillivision.data.repository.ChilliVisionRepository

object Injection {
    fun provideRepository(context : Context) : ChilliVisionRepository{
        val pref = TokenPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getAPIService()
        return ChilliVisionRepository(pref, apiService)
    }
}