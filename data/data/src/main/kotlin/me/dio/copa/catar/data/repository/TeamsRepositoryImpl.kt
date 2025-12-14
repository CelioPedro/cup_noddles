package me.dio.copa.catar.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.repositories.TeamsRepository
import me.dio.copa.catar.domain.source.MatchesDataSource
import javax.inject.Inject

class TeamsRepositoryImpl @Inject constructor(
    private val remoteDataSource: MatchesDataSource.Remote
) : TeamsRepository {
    override fun getTeams(): Flow<List<TeamDomain>> = flow {
        emit(remoteDataSource.getTeams())
    }
}
