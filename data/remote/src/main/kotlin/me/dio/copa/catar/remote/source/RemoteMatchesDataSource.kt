package me.dio.copa.catar.remote.source

import kotlinx.coroutines.flow.Flow
import me.dio.copa.catar.remote.model.MatchRemote
import me.dio.copa.catar.remote.model.TeamRemote

interface RemoteMatchesDataSource {
    suspend fun getMatches(): List<MatchRemote>
    suspend fun getTeams(): List<TeamRemote>
}
