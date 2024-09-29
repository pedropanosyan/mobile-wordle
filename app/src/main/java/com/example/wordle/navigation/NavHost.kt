package com.example.wordle.navigation


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wordle.screens.ProfileScreen
import com.example.wordle.screens.WordleScreen
import com.example.wordleViewModel.StatsViewModel

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
    val profileViewModel = hiltViewModel<StatsViewModel>()
    NavHost(
        navController = navController,
        startDestination = WordleScreen.Home.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 10.dp)
    ) {
        composable(route = WordleScreen.Home.name) {
            WordleScreen(
                onNavigateToGame = { navController.navigate(WordleScreen.Home.name) },
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        composable(route = WordleScreen.Stats.name) {
            ProfileScreen(
                onNavigateToGame = { navController.navigate(WordleScreen.Home.name) },
                modifier = Modifier
                    .fillMaxSize(),
                totalGamesPlayed = profileViewModel.totalGamesPlayed.collectAsState(initial = 0).value,
                totalWins = profileViewModel.totalWins.collectAsState(initial = 0).value,
                totalLosses = profileViewModel.totalLosses.collectAsState(initial = 0).value,
                bestWinningTime = profileViewModel.bestWinningTime.collectAsState(initial = 0).value,
                worstWinningTime = profileViewModel.worstWinningTime.collectAsState(initial = 0).value,
                averageMatchTime = profileViewModel.averageMatchTime.collectAsState(initial = 0).value,
                totalPlayTime = profileViewModel.totalPlayTime.collectAsState(initial = 0).value,
                allGames = profileViewModel.getAllGames.collectAsState(initial = emptyList()).value


            )
        }

    }
}