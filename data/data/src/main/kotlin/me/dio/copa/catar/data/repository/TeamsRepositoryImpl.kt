package me.dio.copa.catar.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.dio.copa.catar.data.mapper.toDomain
import me.dio.copa.catar.data.mapper.toEntity
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.repositories.TeamsRepository
import me.dio.copa.catar.local.dao.TeamDao
import me.dio.copa.catar.remote.CopaApi
import javax.inject.Inject

class TeamsRepositoryImpl @Inject constructor(
    private val dao: TeamDao,
    private val api: CopaApi
) : TeamsRepository {
    override fun getTeams(): Flow<List<TeamDomain>> = dao.getTeams().map { it.toDomain() }

    override suspend fun sync() {
        val remoteTeams = api.getTeams()
        dao.insertAll(remoteTeams.map { it.toEntity() })
    }
}
