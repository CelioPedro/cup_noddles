package me.dio.copa.catar.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.domain.source.MatchesDataSource
import javax.inject.Inject

class MatchesRepositoryImpl @Inject constructor(
    private val local: MatchesDataSource.Local,
    private val remote: MatchesDataSource.Remote
) : MatchesRepository {

    override fun getMatches(): Flow<List<MatchDomain>> {
        return local.getMatches().combine(local.getActiveNotificationIds()) { matches, activeIds ->
            matches.map { match ->
                match.copy(notificationEnabled = activeIds.contains(match.id.toString()))
            }
        }
    }

    override suspend fun fetchAndSaveMatches() {
        // Lógica corrigida: busca e salva SEMPRE.
        val remoteMatches = remote.getMatches()
        local.save(remoteMatches)
    }

    override suspend fun enableNotificationFor(id: String) {
        local.enableNotificationFor(id)
    }

    override suspend fun disableNotificationFor(id: String) {
        local.disableNotificationFor(id)
    }
}