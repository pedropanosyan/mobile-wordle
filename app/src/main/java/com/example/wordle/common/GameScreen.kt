package com.example.wordle.common

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wordle.screens.createInitialGuessesMatrix

@Composable
fun GameScreen (solution: String, quitGame: () -> Unit, startAnotherGame: () -> Unit) {
    var guesses by remember { mutableStateOf(createInitialGuessesMatrix()) }
    var currentColumn by remember { mutableIntStateOf(0) }
    var currentRow by remember { mutableIntStateOf(0) }
    var result by remember { mutableStateOf("") }

    Log.d("GameScreen", "Solution: $solution")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WordleGrid(guesses = guesses, solution = solution, currentRow = currentRow)
        Spacer(modifier = Modifier.height(16.dp))
        WordleKeyboard(
            onKeyPress = { char ->
                if (currentColumn < 5) {
                    guesses = guesses.toMutableList().apply {
                        this[currentRow] = this[currentRow].toMutableList().apply {
                            this[currentColumn] = char
                        }
                    }
                    currentColumn++
                }
            },
            onEnter = {
                if (currentColumn == 5) {
                    if (checkIfGuessIsCorrect(guesses[currentRow], solution)) {
                        result = "won"
                    }
                    if (currentRow == 5) {
                        result = "lost"
                    }
                    currentRow++
                    currentColumn = 0
                }
            },
            onDelete = {
                if (currentColumn > 0) {
                    currentColumn--
                    guesses = guesses.toMutableList().apply {
                        this[currentRow] = this[currentRow].toMutableList().apply {
                            this[currentColumn] = ""
                        }
                    }
                }
            }
        )
        if (result.isNotEmpty()) {
            Text(
                text = "You $result!",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Row (
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (result.isNotEmpty()) {
                Button(onClick = {
                    startAnotherGame()
                    guesses = createInitialGuessesMatrix()
                    currentRow = 0
                    currentColumn = 0
                    result = ""
                }) {
                    Text(
                        text = "Play again",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
            Button(onClick = {
                quitGame()
                guesses = createInitialGuessesMatrix()
                currentRow = 0
                currentColumn = 0
                result = ""
            }) {
                Text(
                    text = "Quit Game",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }

    }
}

fun checkIfGuessIsCorrect(guess: List<String>, solution: String): Boolean {
    return guess.joinToString("") == solution
}
