package me.dio.copa.catar.domain.model

typealias MatchDomain = Match

data class Match(
    val id: Int,
    val round: Int,
    val stage: String,
    val date: String,
    val venue: String,
    val venue_image_url: String,
    val city: String,
    val team1_id: String,
    val team2_id: String,
    val score1: Int,
    val score2: Int,
    val notificationEnabled: Boolean = false
)
