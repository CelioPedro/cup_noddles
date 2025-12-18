package me.dio.copa.catar.remote.model

import com.google.gson.annotations.SerializedName

typealias MatchRemote = Match

data class Match(
    @SerializedName("id") val id: Int,
    @SerializedName("round") val round: Int,
    @SerializedName("stage") val stage: String,
    @SerializedName("date") val date: String,
    @SerializedName("venue") val venue: String,
    @SerializedName("venue_image_url") val venueImageUrl: String,
    @SerializedName("city") val city: String,
    @SerializedName("team1_id") val team1Id: String,
    @SerializedName("team2_id") val team2Id: String,
    @SerializedName("score1") val score1: Int,
    @SerializedName("score2") val score2: Int
)
