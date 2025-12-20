package me.dio.copa.catar.local.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.source.MatchesDataSource
import javax.inject.Inject

class MatchDataSourceLocal @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) : MatchesDataSource.Local {

    private val matchesKey = stringPreferencesKey("matches")
    private val notificationIdsKey = stringSetPreferencesKey("notification_ids")

    override fun getMatches(): Flow<List<Match>> = dataStore.data.map {
        val json = it[matchesKey]
        if (json != null) {
            val type = object : TypeToken<List<Match>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    override suspend fun save(matches: List<Match>) {
        dataStore.edit {
            it[matchesKey] = gson.toJson(matches)
        }
    }

    override fun getActiveNotificationIds(): Flow<Set<String>> = dataStore.data.map {
        it[notificationIdsKey] ?: emptySet()
    }

    override suspend fun enableNotificationFor(id: String) {
        dataStore.edit {
            val currentIds = it[notificationIdsKey] ?: emptySet()
            it[notificationIdsKey] = currentIds + id
        }
    }

    override suspend fun disableNotificationFor(id: String) {
        dataStore.edit {
            val currentIds = it[notificationIdsKey] ?: emptySet()
            it[notificationIdsKey] = currentIds - id
        }
    }
}