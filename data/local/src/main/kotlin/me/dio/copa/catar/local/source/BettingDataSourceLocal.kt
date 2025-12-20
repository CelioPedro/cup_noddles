package me.dio.copa.catar.local.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.dio.copa.catar.domain.source.BettingDataSource
import org.json.JSONObject
import javax.inject.Inject

class BettingDataSourceLocal @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : BettingDataSource.Local {

    private val betsKey = stringPreferencesKey("bets")

    override fun getBets(): Flow<Map<String, Pair<String, String>>> = dataStore.data.map {
        getBetsFromPrefs(it)
    }

    override suspend fun saveBet(matchId: String, score1: String, score2: String) {
        dataStore.edit {
            val currentBets = getBetsFromPrefs(it).toMutableMap()
            currentBets[matchId] = score1 to score2

            val json = JSONObject(currentBets.mapValues { "${it.value.first},${it.value.second}" }).toString()
            it[betsKey] = json
        }
    }

    private fun getBetsFromPrefs(prefs: Preferences): Map<String, Pair<String, String>> {
        val jsonString = prefs[betsKey] ?: return emptyMap()
        return try {
            val json = JSONObject(jsonString)
            val map = mutableMapOf<String, Pair<String, String>>()
            json.keys().forEach { key ->
                val scores = json.getString(key).split(",")
                if (scores.size == 2) {
                    map[key] = scores[0] to scores[1]
                }
            }
            map
        } catch (e: Throwable) {
            emptyMap()
        }
    }
}
