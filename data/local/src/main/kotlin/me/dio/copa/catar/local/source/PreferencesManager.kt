package me.dio.copa.catar.local.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferencesManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val favoriteTeamIdKey = stringPreferencesKey("favorite_team_id")

    suspend fun getFavoriteTeamId(): String? {
        val prefs = dataStore.data.first()
        return prefs[favoriteTeamIdKey]
    }

    suspend fun setFavoriteTeamId(id: String) {
        dataStore.edit {
            it[favoriteTeamIdKey] = id
        }
    }
}
