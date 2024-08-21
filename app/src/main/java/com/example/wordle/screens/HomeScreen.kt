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
import getRandomWord
import java.text.Normalizer

@Composable
fun WordleScreen(
    modifier: Modifier = Modifier,
    onNavigateToGame: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var solution by remember { mutableStateOf("") }

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
        if (isPlaying) {
            GameScreen(
                solution = solution,
                quitGame = { isPlaying = false },
                startAnotherGame = {
                    getRandomWord(
                        5,
                        "es",
                        onSuccess = { word -> solution = normalizeWord(word) }
                    )
                }
            )
        } else {
            Button(onClick = {
                isPlaying = true
                getRandomWord(
                    5,
                    "es",
                    onSuccess = { word -> solution = word.uppercase() }
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


fun createInitialGuessesMatrix(): List<MutableList<String>> {
    return MutableList(6) { MutableList(5) { "" } }
}


fun normalizeWord(word: String): String {
    return Normalizer.normalize(word, Normalizer.Form.NFD)
        .replace(Regex("\\p{M}"), "")
        .uppercase()
}


@Preview(showBackground = true)
@Composable
fun WordleScreenPreview() {
    WordleTheme {
        GameScreen(solution = "CRANE", quitGame = {}, startAnotherGame = {})
    }
}
