package me.dio.copa.catar.remote.source

import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.source.MatchesDataSource
import me.dio.copa.catar.domain.source.TeamsDataSource
import me.dio.copa.catar.remote.CopaApi
import me.dio.copa.catar.remote.extensions.getOrThrowDomainError
import me.dio.copa.catar.remote.mapper.toDomain
import javax.inject.Inject

class MatchDataSourceRemote @Inject constructor(
    private val api: CopaApi
) : MatchesDataSource.Remote, TeamsDataSource.Remote {

    override suspend fun getMatches(): List<MatchDomain> {
        return runCatching {
            api.getMatches().toDomain()
        }.getOrThrowDomainError()
    }

    override suspend fun getTeams(): List<TeamDomain> {
        return runCatching {
            api.getTeams().toDomain()
        }.getOrThrowDomainError()
    }
}
