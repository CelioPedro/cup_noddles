package me.dio.copa.catar.remote

import me.dio.copa.catar.remote.model.MatchDto
import me.dio.copa.catar.remote.model.TeamDto
import retrofit2.http.GET

interface CopaApi {
    @GET("CelioPedro/16fcd07134e889044eb1b9a47ed6c842/raw/a3cae42749ae6f8e12698745f669aa56699a2222/matches-2026.json")
    suspend fun getMatches(): List<MatchDto>

    @GET("CelioPedro/16fcd07134e889044eb1b9a47ed6c842/raw/a3cae42749ae6f8e12698745f669aa56699a2222/teams-2026.json")
    suspend fun getTeams(): List<TeamDto>
}
