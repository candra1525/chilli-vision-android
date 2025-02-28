package com.candra.chillivision.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.candra.chillivision.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    val tokenFlow: Flow<String> = dataStore.data.map { pref ->
        pref[TOKEN_KEY] ?: ""
    }.distinctUntilChanged()

    suspend fun savePreferences(token: String, id: String, fullname: String, noHandphone: String, image: String) {
        dataStore.edit { pref ->
            pref[TOKEN_KEY] = token
            pref[ID_KEY] = id
            pref[FULLNAME_KEY] = fullname
            pref[NOHANDPHONE_KEY] = noHandphone
            pref[IMAGE_KEY] = image
        }
    }


    fun getPreferences(): Flow<UserModel> {
        return dataStore.data.map { pref ->
            UserModel(
                pref[TOKEN_KEY] ?: "",
                pref[ID_KEY] ?: "",
                pref[FULLNAME_KEY] ?: "",
                pref[NOHANDPHONE_KEY] ?: "",
                pref[IMAGE_KEY] ?: ""
            )
        }
    }

    suspend fun clearPreferences() {
        dataStore.edit { pref ->
            pref.remove(TOKEN_KEY)
            pref.remove(ID_KEY)
            pref.remove(FULLNAME_KEY)
            pref.remove(NOHANDPHONE_KEY)
            pref.remove(IMAGE_KEY)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val ID_KEY = stringPreferencesKey("id")
        private val FULLNAME_KEY = stringPreferencesKey("fullname")
        private val NOHANDPHONE_KEY = stringPreferencesKey("noHandphone")
        private val IMAGE_KEY = stringPreferencesKey("image")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}