package me.dio.copa.catar.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.repositories.MatchesRepository
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val repository: MatchesRepository
) {
    operator fun invoke(): Flow<List<MatchDomain>> = repository.getMatches()
}
