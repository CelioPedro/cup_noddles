package me.dio.copa.catar.domain.source

import kotlinx.coroutines.flow.Flow
import me.dio.copa.catar.domain.model.Match

interface MatchesDataSource {
    fun getMatches(): Flow<List<Match>>

    interface Local {
        fun getMatches(): Flow<List<Match>>
        suspend fun save(matches: List<Match>)
        fun getActiveNotificationIds(): Flow<Set<String>>
        suspend fun enableNotificationFor(id: String)
        suspend fun disableNotificationFor(id: String)
    }

    interface Remote {
        suspend fun getMatches(): List<Match>
    }
}
