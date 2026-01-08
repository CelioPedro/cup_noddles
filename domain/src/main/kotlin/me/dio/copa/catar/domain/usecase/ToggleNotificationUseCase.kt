package me.dio.copa.catar.domain.usecase

import me.dio.copa.catar.domain.repositories.MatchesRepository
import javax.inject.Inject

class ToggleNotificationUseCase @Inject constructor(
    private val repository: MatchesRepository
) {
    suspend operator fun invoke(id: Int) {
        return repository.toggleNotification(id)
    }
}
