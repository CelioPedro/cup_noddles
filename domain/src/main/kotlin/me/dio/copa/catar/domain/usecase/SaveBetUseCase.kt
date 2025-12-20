package me.dio.copa.catar.domain.usecase

import me.dio.copa.catar.domain.source.BettingDataSource
import javax.inject.Inject

class SaveBetUseCase @Inject constructor(
    private val repository: BettingDataSource.Local
) {
    suspend operator fun invoke(matchId: String, score1: String, score2: String) {
        repository.saveBet(matchId, score1, score2)
    }
}
