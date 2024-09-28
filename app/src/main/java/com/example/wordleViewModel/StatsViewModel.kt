package com.example.wordleViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.wordle.data.GamesDao
import com.example.wordle.data.WordleDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {

    private val database = WordleDatabase.getDatabase(context)

    private val gamesDao: GamesDao = database.gamesDao()

    val getAllGames = gamesDao.getAllGames().asFlow()
    val totalGamesPlayed = gamesDao.getTotalGamesPlayed().asFlow()
    val totalWins = gamesDao.getTotalWins().asFlow()
    val totalLosses = gamesDao.getTotalLosses().asFlow()
    val bestWinningTime = gamesDao.getBestWinningTime().asFlow()
    val worstWinningTime = gamesDao.getWorstWinningTime().asFlow()
    val averageMatchTime = gamesDao.getAverageMatchTime().asFlow()
    val totalPlayTime = gamesDao.getTotalTimePlayed().asFlow()

}
