package me.dio.copa.catar.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.dio.copa.catar.data.mapper.toDomain
import me.dio.copa.catar.data.mapper.toEntity
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.local.dao.MatchDao
import me.dio.copa.catar.remote.CopaApi
import javax.inject.Inject

class MatchesRepositoryImpl @Inject constructor(
    private val dao: MatchDao,
    private val api: CopaApi
) : MatchesRepository {
    override fun getMatches(): Flow<List<MatchDomain>> = dao.getMatches().map { it.toDomain() }

    override suspend fun toggleNotification(matchId: Int) {
        val match = dao.getMatch(matchId)
        dao.setNotificationEnabled(matchId, !match.notificationEnabled)
    }

    override suspend fun sync() {
        runCatching {
            val remoteMatches = api.getMatches()
            dao.insertAll(remoteMatches.map { it.toEntity() })
        }
    }
}
