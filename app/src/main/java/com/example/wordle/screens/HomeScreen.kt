package com.example.wordle.screens

import DifficultySelector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordle.R
import com.example.wordle.common.GameScreen
import com.example.wordleViewModel.WordleViewModel


@Composable
fun WordleScreen(
    modifier: Modifier = Modifier,
    onNavigateToGame: () -> Unit
) {
    val initialDifficulty = stringResource(id = R.string.medium)
    var selectedDifficulty by remember { mutableStateOf(initialDifficulty) }
    val viewModel = hiltViewModel<WordleViewModel>()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.lg)),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.wordle),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.xl))
                .width(400.dp)
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
                    text = stringResource(id = R.string.play),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Box {}
        }

    }
}

@Preview(showBackground = true)
@Composable
fun WordleImagePreview() {
    Image(
        painter = painterResource(id = R.drawable.wordle),
        contentDescription = stringResource(id = R.string.app_name),
    )
}
