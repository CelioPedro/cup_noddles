package me.dio.copa.catar.domain.source

import kotlinx.coroutines.flow.Flow
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain

sealed interface MatchesDataSource {
    interface Remote : MatchesDataSource {
        suspend fun getMatches(): List<MatchDomain>
        suspend fun getTeams(): List<TeamDomain>
    }

    interface Local : MatchesDataSource {
        fun getActiveNotificationIds(): Flow<Set<String>>
        suspend fun enableNotificationFor(id: String)
        suspend fun disableNotificationFor(id: String)
    }
}
        