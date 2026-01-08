package me.dio.copa.catar.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey
    val id: Int,
    val round: Int,
    val stage: String,
    val date: String,
    val venue: String,
    val venueImageUrl: String,
    val city: String,
    val team1Id: String,
    val team2Id: String,
    val score1: Int,
    val score2: Int,
    val notificationEnabled: Boolean = false
)
