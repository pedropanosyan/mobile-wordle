package com.example.wordle.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GamesDao {
    @Insert
    suspend fun insert(game: Game)

    @Update
    suspend fun update(game: Game)

    @Delete
    suspend fun delete(game: Game)

    @Query("SELECT * FROM game")
    fun getAllGames(): LiveData<List<Game>>

    @Query("SELECT IFNULL(COUNT(*), 0) FROM game")
    fun getTotalGamesPlayed(): LiveData<Int>

    @Query("SELECT IFNULL(COUNT(*), 0) FROM game WHERE hasWon = 1")
    fun getTotalWins(): LiveData<Int>

    @Query("SELECT IFNULL(COUNT(*), 0) FROM game WHERE hasWon = 0")
    fun getTotalLosses(): LiveData<Int>

    @Query("SELECT IFNULL(MIN(timePlayed), 0) FROM game WHERE hasWon = 1")
    fun getBestWinningTime(): LiveData<Int>

    @Query("SELECT IFNULL(MAX(timePlayed), 0) FROM game WHERE hasWon = 1")
    fun getWorstWinningTime(): LiveData<Int>

    @Query("SELECT IFNULL(AVG(timePlayed), 0) FROM game")
    fun getAverageMatchTime(): LiveData<Int>

    @Query("SELECT IFNULL(SUM(timePlayed), 0) FROM game")
    fun getTotalTimePlayed(): LiveData<Int>
}
