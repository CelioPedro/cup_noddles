package me.dio.copa.catar.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.domain.repositories.TeamsRepository
import javax.inject.Inject

class GetTeamsUseCase @Inject constructor(
    private val repository: TeamsRepository
) {
    operator fun invoke(): Flow<List<TeamDomain>> = repository.getTeams()
}
