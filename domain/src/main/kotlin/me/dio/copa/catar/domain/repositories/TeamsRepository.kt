package me.dio.copa.catar.domain.repositories

import kotlinx.coroutines.flow.Flow
import me.dio.copa.catar.domain.model.TeamDomain

interface TeamsRepository {
    fun getTeams(): Flow<List<TeamDomain>>
}
