package me.dio.copa.catar.domain.model

typealias TeamDomain = Team

data class Team(
    val id: String,
    val name: String,
    val group: String,
    val flag_url: String,
    val ranking: Int
)
