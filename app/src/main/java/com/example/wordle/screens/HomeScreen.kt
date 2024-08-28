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
import com.example.wordle.common.GameScreen
import com.example.wordle.ui.theme.WordleTheme
import com.example.wordleViewModel.WordleViewModel
import java.text.Normalizer

@Composable
fun WordleScreen(
    modifier: Modifier = Modifier,
    onNavigateToGame: () -> Unit,
    viewModel: WordleViewModel
) {

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
            GameScreen(
                solution = viewModel.solution,
                quitGame = { viewModel.setIsPlaying(false) },
                startAnotherGame = {
                    viewModel.fetchRandomWord (
                        5,
                        "es",
                    )
                }
            )
        } else {
            Button(onClick = {
                viewModel.setIsPlaying(true)
                viewModel.fetchRandomWord(
                    5,
                    "es",
                )
            }) {
                Text(
                    text = "Start Game",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Box {}
        }

    }
}


@Preview(showBackground = true)
@Composable
fun WordleScreenPreview() {
    WordleTheme {
        GameScreen(solution = "CRANE", quitGame = {}, startAnotherGame = {})
    }
}
