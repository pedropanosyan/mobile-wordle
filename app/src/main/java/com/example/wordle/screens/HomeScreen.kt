package com.example.wordle.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordle.common.GameScreen
import com.example.wordle.ui.theme.WordleTheme
import com.example.wordleViewModel.WordleViewModel


@Composable
fun WordleScreen(
    modifier: Modifier = Modifier,
    onNavigateToGame: () -> Unit
) {

    val viewModel = hiltViewModel<WordleViewModel>()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wordle",
            style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )

        )
        if (viewModel.isPlaying) {
            GameScreen()
        } else {
            Button(onClick = { viewModel.playGame() }) {
                Text(
                    text = "Start Game",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Box {}
        }

    }
}