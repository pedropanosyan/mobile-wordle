package com.example.wordle.common

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordle.R
import com.example.wordleViewModel.WordleViewModel


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.LocalContext

@Composable
fun GameScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<WordleViewModel>()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.sm)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            WordleGrid(
                guesses = viewModel.guesses,
                solution = viewModel.solution,
                currentRow = viewModel.currentRow
            )
        }
        item {
            if (viewModel.result.isEmpty()) {
                WordleKeyboard(
                    solution = viewModel.solution,
                    guesses = viewModel.guesses,
                    currentRow = viewModel.currentRow,
                    onEnterPress = { viewModel.submitWord() },
                    onDeletePress = { viewModel.deleteLetter() },
                    onKeyPress = { option -> viewModel.enterLetter(option) }
                )
            }
        }
        item {
            if (viewModel.result.isNotEmpty()) {
                if (viewModel.hasWon()) {
                    Log.i("GameScreen", "You won. The word was: ${viewModel.getFormattedSolution()}")
                    Toast.makeText(
                        context,
                        stringResource(id = R.string.congratulations_you_won),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.i("GameScreen", "You lost. The word was: ${viewModel.getFormattedSolution()}")
                    Toast.makeText(
                        context,
                        stringResource(id = R.string.you_lost) + " " +
                                stringResource(id = R.string.the_word_was, viewModel.getFormattedSolution()),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.lg)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (viewModel.result.isNotEmpty()) {
                    val rematchText = stringResource(id = R.string.rematch)
                    Button(onClick = {
                        viewModel.playGame(rematchText)
                    }) {
                        Text(
                            text = stringResource(id = R.string.play_again),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Button(onClick = { viewModel.quitGame() }) {
                    Text(
                        text = stringResource(id = R.string.quit_game),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
