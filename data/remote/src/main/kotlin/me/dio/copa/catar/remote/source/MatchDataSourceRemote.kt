package me.dio.copa.catar.remote.source

import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.source.MatchesDataSource
import me.dio.copa.catar.domain.source.TeamsDataSource
import me.dio.copa.catar.remote.extensions.getOrThrowDomainError
import me.dio.copa.catar.remote.mapper.toMatchDomain
import me.dio.copa.catar.remote.mapper.toTeamDomain
import me.dio.copa.catar.remote.services.MatchesServices
import javax.inject.Inject

class MatchDataSourceRemote @Inject constructor(
    private val service: MatchesServices
) : MatchesDataSource.Remote, TeamsDataSource.Remote {

    override suspend fun getMatches(): List<MatchDomain> {
        return runCatching {
            service.getMatches().toMatchDomain()
        }.getOrThrowDomainError()
    }

    override suspend fun getTeams(): List<TeamDomain> {
        return runCatching {
            service.getTeams().toTeamDomain()
        }.getOrThrowDomainError()
    }
}
