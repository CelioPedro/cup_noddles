package me.dio.copa.catar.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val group: String,
    val flagUrl: String,
    val ranking: Int
)
