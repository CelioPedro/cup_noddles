package me.dio.copa.catar.domain.source

import me.dio.copa.catar.domain.model.TeamDomain

interface TeamsDataSource {
    suspend fun getTeams(): List<TeamDomain>

    interface Remote {
        suspend fun getTeams(): List<TeamDomain>
    }
}
