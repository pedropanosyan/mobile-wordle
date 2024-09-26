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

    @Query("SELECT COUNT(*) FROM game")
    fun getTotalGamesPlayed(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM game WHERE hasWon = 1")
    fun getTotalWins(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM game WHERE hasWon = 0")
    fun getTotalLosses(): LiveData<Int>

    @Query("SELECT MIN(timePlayed) FROM game WHERE hasWon = 1")
    fun getBestWinningTime(): LiveData<Int>

    @Query("SELECT MAX(timePlayed) FROM game WHERE hasWon = 1")
    fun getWorstWinningTime(): LiveData<Int>

    @Query("SELECT AVG(timePlayed) FROM game")
    fun getAverageMatchTime(): LiveData<Int>

    @Query("SELECT SUM(timePlayed) FROM game")
    fun getTotalTimePlayed(): LiveData<Int>
}
