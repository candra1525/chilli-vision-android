package com.candra.chillivision.data.di

import android.content.Context
import com.candra.chillivision.data.network.ApiConfig
import com.candra.chillivision.data.preferences.UserPreferences
import com.candra.chillivision.data.preferences.dataStore
import com.candra.chillivision.data.repository.ChilliVisionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context, apiType: String = "default"): ChilliVisionRepository {
        val pref = UserPreferences.getInstance(context.dataStore)

        // Pilih API service berdasarkan apiType
        val apiService = ApiConfig.getAPIService(pref, apiType)

        return ChilliVisionRepository(pref, apiService)
    }
}
