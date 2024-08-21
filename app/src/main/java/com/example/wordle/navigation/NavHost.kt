package com.example.wordle.navigation


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wordle.screens.ProfileScreen
import com.example.wordle.screens.WordleScreen

@Composable
fun NavHostComposable(innerPadding: PaddingValues, navController: NavHostController) {
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
                    .fillMaxSize()
            )
        }
        composable(route = WordleScreen.Stats.name) {
            ProfileScreen(
                onNavigateToGame = { navController.navigate(WordleScreen.Home.name) },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

    }
}