package me.dio.copa.catar.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.dio.copa.catar.domain.source.BettingDataSource
import javax.inject.Inject

class GetBetsUseCase @Inject constructor(
    private val repository: BettingDataSource.Local
) {
    operator fun invoke(): Flow<Map<String, Pair<String, String>>> = repository.getBets()
}
