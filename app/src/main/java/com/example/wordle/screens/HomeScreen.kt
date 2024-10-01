package com.example.wordle.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordle.R
import com.example.wordle.common.DifficultySelector
import com.example.wordle.common.GameScreen
import com.example.wordleViewModel.WordleViewModel


@Composable
fun WordleScreen(
    modifier: Modifier = Modifier,
    onNavigateToGame: () -> Unit
) {

    var selectedDifficulty by remember { mutableStateOf("Medium") }
    val viewModel = hiltViewModel<WordleViewModel>()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.lg)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )

        )
        if (viewModel.isPlaying) {
            GameScreen()
        } else {
            Column {
                DifficultySelector(
                    selectedDifficulty = selectedDifficulty,
                    onDifficultySelected = { difficulty ->
                        selectedDifficulty = difficulty
                    }
                )
            }

            Button(onClick = { viewModel.playGame(selectedDifficulty) }) {
                Text(
                    text = "Start Game",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Box {}
        }

    }
}