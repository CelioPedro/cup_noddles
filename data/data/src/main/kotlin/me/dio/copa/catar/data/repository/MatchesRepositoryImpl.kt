package me.dio.copa.catar.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.domain.source.MatchesDataSource
import javax.inject.Inject

class MatchesRepositoryImpl @Inject constructor(
    private val localDataSource: MatchesDataSource.Local,
    private val remoteDataSource: MatchesDataSource.Remote,
) : MatchesRepository {
    override fun getMatches(): Flow<List<MatchDomain>> {
        val remoteMatchesFlow = flow { emit(remoteDataSource.getMatches()) }

        return remoteMatchesFlow
            .combine(localDataSource.getActiveNotificationIds()) { matches, ids ->
                matches.map { match ->
                    match.copy(notificationEnabled = ids.contains(match.id.toString()))
                }
            }
    }

    override suspend fun enableNotificationFor(id: String) {
        localDataSource.enableNotificationFor(id)
    }

    override suspend fun disableNotificationFor(id: String) {
        localDataSource.disableNotificationFor(id)
    }
}
