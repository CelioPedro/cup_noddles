package me.dio.copa.catar.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.dio.copa.catar.local.model.MatchEntity

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches")
    fun getMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :id")
    suspend fun getMatch(id: Int): MatchEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matches: List<MatchEntity>)

    @Query("UPDATE matches SET notificationEnabled = :enabled WHERE id = :id")
    suspend fun setNotificationEnabled(id: Int, enabled: Boolean)
}
