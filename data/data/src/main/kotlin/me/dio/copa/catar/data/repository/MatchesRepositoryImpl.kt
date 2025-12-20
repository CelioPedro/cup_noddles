package me.dio.copa.catar.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.domain.source.MatchesDataSource
import javax.inject.Inject

class MatchesRepositoryImpl @Inject constructor(
    private val localDataSource: MatchesDataSource.Local,
    private val remoteDataSource: MatchesDataSource.Remote,
) : MatchesRepository {

    override fun getMatches(): Flow<List<Match>> {
        return localDataSource.getMatches().combine(localDataSource.getActiveNotificationIds()) { matches, activeIds ->
            matches.map {
                it.copy(notificationEnabled = activeIds.contains(it.id.toString()))
            }
        }
    }

    override suspend fun fetchAndSaveMatches() {
        val isCacheEmpty = localDataSource.getMatches().first().isEmpty()
        if (isCacheEmpty) {
            val remoteMatches = remoteDataSource.getMatches()
            localDataSource.save(remoteMatches)
        }
    }

    override suspend fun enableNotificationFor(id: String) {
        localDataSource.enableNotificationFor(id)
    }

    override suspend fun disableNotificationFor(id: String) {
        localDataSource.disableNotificationFor(id)
    }
}