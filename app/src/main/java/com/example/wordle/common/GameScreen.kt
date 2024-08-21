package com.example.wordle.common

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wordle.R
import com.example.wordle.screens.createInitialGuessesMatrix
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.Normalizer


@Composable
fun GameScreen (
    solution: String,
    quitGame: () -> Unit,
    startAnotherGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    var guesses by remember { mutableStateOf(createInitialGuessesMatrix()) }
    var currentColumn by remember { mutableIntStateOf(0) }
    var currentRow by remember { mutableIntStateOf(0) }
    var result by remember { mutableStateOf("") }

    val context = LocalContext.current
    val wordsSet by remember { mutableStateOf(readWordsFromFile(context, "diccionario_espanol.txt")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WordleGrid(guesses = guesses, solution = solution, currentRow = currentRow)
        if (result.isEmpty()) {
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
                        if (!wordExists(wordsSet, guesses[currentRow].joinToString(""))) {
                            return@WordleKeyboard
                        }
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
        }
        if (result.isNotEmpty()) {
            if (result == "won") {
                Text(
                    text = stringResource(id = R.string.congratulations_you_won),
                    style = MaterialTheme.typography.headlineMedium
                )
            } else {
                val formattedSolution = solution.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                Column {
                    Text(
                        text = stringResource(id = R.string.you_lost),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = stringResource(id = R.string.the_word_was, formattedSolution),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
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
                        text = stringResource(id = R.string.play_again),
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
                    text = stringResource(id = R.string.quit_game),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }

    }
}

fun checkIfGuessIsCorrect(guess: List<String>, solution: String): Boolean {
    return guess.joinToString("").lowercase() == solution
}

fun normalize(word: String): String {
    return Normalizer.normalize(word, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .uppercase()
}


fun readWordsFromFile(context: Context, fileName: String): Set<String> {
    val wordsSet = mutableSetOf<String>()
    context.assets.open(fileName).use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            reader.forEachLine { line ->
                line.split("\\s+".toRegex()).forEach { word ->
                    wordsSet.add(normalize(word.trim()))
                }
            }
        }
    }
    return wordsSet
}

fun wordExists(wordsSet: Set<String>, word: String): Boolean {
    return wordsSet.contains(normalize(word))
}

