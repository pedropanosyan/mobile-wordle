package com.example.wordle.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordle.R
import com.example.wordleViewModel.WordleViewModel


@Composable
fun GameScreen (
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<WordleViewModel>()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.sm)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WordleGrid(
            guesses = viewModel.guesses,
            solution = viewModel.solution,
            currentRow = viewModel.currentRow
        )
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
        if (viewModel.result.isNotEmpty()) {
            if (viewModel.hasWon()) {
                Text(
                    text = stringResource(id = R.string.congratulations_you_won),
                    style = MaterialTheme.typography.headlineMedium
                )
            } else {
                Column {
                    Text(
                        text = stringResource(id = R.string.you_lost),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = stringResource(id = R.string.the_word_was, viewModel.getFormattedSolution()),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
        Row (
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.lg))
        ) {
            if (viewModel.result.isNotEmpty()) {
                Button(onClick = { viewModel.playGame("Rematch") }) {
                    Text(
                        text = stringResource(id = R.string.play_again),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
            Button(onClick = { viewModel.quitGame() }) {
                Text(
                    text = stringResource(id = R.string.quit_game),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }

    }
}


