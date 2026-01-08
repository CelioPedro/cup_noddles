package me.dio.copa.catar.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.dio.copa.catar.local.model.TeamEntity

@Dao
interface TeamDao {
    @Query("SELECT * FROM teams")
    fun getTeams(): Flow<List<TeamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<TeamEntity>)
}
