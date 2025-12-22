package me.dio.copa.catar.local.fake

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.repositories.MatchesRepository
import kotlin.random.Random

class FakeMatchesRepository(
    private val context: Context,
    private val moshi: Moshi
) : MatchesRepository {
    private val _matches = MutableStateFlow<List<Match>>(emptyList())

    init {
        val json = context.assets
            .open("matches.json")
            .bufferedReader()
            .use { it.readText() }

        val type = Types.newParameterizedType(List::class.java, Match::class.java)
        val adapter = moshi.adapter<List<Match>>(type)

        _matches.value = adapter.fromJson(json)!!
    }

    override fun getMatches(): Flow<List<Match>> = _matches

    override suspend fun fetchAndSaveMatches() {
        // No-op for fake repository
    }

    override suspend fun enableNotificationFor(id: String) {
        updateMatch(id) { it.copy(notificationEnabled = true) }
    }

    override suspend fun disableNotificationFor(id: String) {
        updateMatch(id) { it.copy(notificationEnabled = false) }
    }

    suspend fun updateScore(id: String) {
        updateMatch(id) { it.copy(score1 = Random.nextInt(5), score2 = Random.nextInt(5)) }
    }

    private suspend fun updateMatch(id: String, apply: (match: Match) -> Match) {
        val matchId = id.toIntOrNull() ?: return

        _matches.value
            .find { it.id == matchId }?.let { match ->
                val index = _matches.value.indexOf(match)
                val mutableMatches = _matches.value.toMutableList()
                mutableMatches[index] = apply(match)
                _matches.value = mutableMatches
            }
    }
}
