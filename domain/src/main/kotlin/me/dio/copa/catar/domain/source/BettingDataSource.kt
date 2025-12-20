package me.dio.copa.catar.domain.source

import kotlinx.coroutines.flow.Flow

interface BettingDataSource {
    fun getBets(): Flow<Map<String, Pair<String, String>>>
    suspend fun saveBet(matchId: String, score1: String, score2: String)

    interface Local: BettingDataSource
    interface Remote: BettingDataSource
}
