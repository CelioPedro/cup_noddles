package me.dio.copa.catar.domain.usecase

import me.dio.copa.catar.domain.repositories.TeamsRepository
import javax.inject.Inject

class SyncTeamsUseCase @Inject constructor(
    private val repository: TeamsRepository
) {
    suspend operator fun invoke() = repository.sync()
}
