package com.candra.chillivision.data.di

import android.content.Context
import com.candra.chillivision.data.network.ApiConfig
import com.candra.chillivision.data.preferences.UserPreferences
import com.candra.chillivision.data.preferences.dataStore
import com.candra.chillivision.data.repository.ChilliVisionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): ChilliVisionRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val token = runBlocking {
            pref.getPreferences().first().token
        }
        val apiService = ApiConfig.getAPIService(token)
        return ChilliVisionRepository(pref, apiService)
    }
}