package me.dio.copa.catar.remote.services

import me.dio.copa.catar.remote.model.MatchRemote
import me.dio.copa.catar.remote.model.TeamRemote
import retrofit2.http.GET

interface MatchesServices {
    @GET("teams.json")
    suspend fun getTeams(): List<TeamRemote>

    @GET("matches.json")
    suspend fun getMatches(): List<MatchRemote>
}
