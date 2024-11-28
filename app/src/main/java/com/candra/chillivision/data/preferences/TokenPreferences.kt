package com.candra.chillivision.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.candra.chillivision.data.model.TokenModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "token")
class TokenPreferences private constructor(private val dataStore: DataStore<Preferences>)
{
    suspend fun saveToken(token: String){
        dataStore.edit {pref->
            pref[TOKEN_KEY] = token
        }
    }

    fun getToken() : Flow<TokenModel>{
        return dataStore.data.map { pref ->
            TokenModel(pref[TOKEN_KEY] ?: "")
        }
    }

    suspend fun clearToken() {
        dataStore.edit { pref ->
            pref.remove(TOKEN_KEY)
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: TokenPreferences? = null
        private val TOKEN_KEY = stringPreferencesKey("token")
        fun getInstance(dataStore: DataStore<Preferences>) : TokenPreferences{
            return INSTANCE ?: synchronized(this){
               val instance = TokenPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}