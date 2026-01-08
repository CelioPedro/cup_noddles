package me.dio.copa.catar.remote.model

import com.squareup.moshi.Json

data class MatchDto(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "round") val round: Int?,
    @field:Json(name = "stage") val stage: String?,
    @field:Json(name = "date") val date: String?,
    @field:Json(name = "venue") val venue: String?,
    @field:Json(name = "venue_image_url") val venueImageUrl: String?,
    @field:Json(name = "city") val city: String?,
    @field:Json(name = "team1_id") val team1Id: String, // Voltando para não-nulo
    @field:Json(name = "team2_id") val team2Id: String, // Voltando para não-nulo
    @field:Json(name = "score1") val score1: Int?,
    @field:Json(name = "score2") val score2: Int?
)
