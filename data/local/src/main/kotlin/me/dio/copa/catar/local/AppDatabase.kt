package me.dio.copa.catar.local

import androidx.room.Database
import androidx.room.RoomDatabase
import me.dio.copa.catar.local.dao.MatchDao
import me.dio.copa.catar.local.dao.TeamDao
import me.dio.copa.catar.local.model.MatchEntity
import me.dio.copa.catar.local.model.TeamEntity

@Database(entities = [MatchEntity::class, TeamEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao
    abstract fun teamDao(): TeamDao
}
