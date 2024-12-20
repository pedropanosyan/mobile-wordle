package com.example.wordle.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordle.R
import com.example.wordleViewModel.WordleViewModel

val keyboardRows = listOf(
    "QWERTYUIO",
    "PASDFGHJK",
    "ZXCVBNÑML"
)

@Composable
fun WordleKeyboard(
    solution: String,
    guesses: List<String>,
    currentRow: Int,
    onEnterPress: () -> Unit,
    onDeletePress: () -> Unit,
    onKeyPress: (option: String) -> Unit
)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.lg)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.md)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in keyboardRows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.sm)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (option in row) {
                    KeyButton(
                        option = option.toString(),
                        onKeyPress = { char -> onKeyPress(char) },
                        solution = solution,
                        guesses = guesses,
                        currentRow = currentRow
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.sm)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SpecialKeyButton(text = stringResource(id = R.string.enter), onClick = { onEnterPress() })
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.sm)))
            SpecialKeyButton(text = stringResource(id = R.string.delete), onClick = { onDeletePress() })
        }
    }
}

@Composable
fun KeyButton(option: String, onKeyPress: (option: String) -> Unit, solution: String, guesses: List<String>, currentRow: Int) {
    // delete the first row of guesses
    val keyColor = getColour(solution, guesses, option, currentRow)

    Box(
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.xxl))
            .background(keyColor, RoundedCornerShape(dimensionResource(id = R.dimen.sm)))
            .padding(dimensionResource(id = R.dimen.sm))
            .clickable { onKeyPress(option) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = option,
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SpecialKeyButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.fiftyTwo), dimensionResource(id = R.dimen.twentyEight))
            .background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(dimensionResource(id = R.dimen.sm)))
            .padding(dimensionResource(id = R.dimen.sm))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun getColour(solution: String, guesses: List<String>, char: String, currentRow: Int): Color {
    if (currentRow == 0) return MaterialTheme.colorScheme.onSurface

    val guessesCopy = guesses.subList(0, currentRow).reversed()

    for (guess in guessesCopy) {
        if (guess.contains(char)) {
            return if (solution.contains(char)) {
                if (solution.indexOf(char) == guess.indexOf(char)) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                }
            } else {
                MaterialTheme.colorScheme.tertiary
            }
        }
    }
    return MaterialTheme.colorScheme.onSurface
}

@Preview(showBackground = true)
@Composable
fun WordleKeyboardPreview() {
    WordleKeyboard(
        solution = "APPLE",
        guesses = listOf("ARRAY", "AMPLE", "OTHER"),
        currentRow = 2,
        onEnterPress = { /* Mock Enter action */ },
        onDeletePress = { /* Mock Delete action */ },
        onKeyPress = { /* Mock Key press action */ }
    )
}

@Preview(showBackground = true)
@Composable
fun KeyButtonPreview() {
    KeyButton(
        option = "A",
        onKeyPress = { /* Mock Key press action */ },
        solution = "APPLE",
        guesses = listOf("ARRAY", "AMPLE"),
        currentRow = 2
    )
}

@Preview(showBackground = true)
@Composable
fun SpecialKeyButtonPreview() {
    SpecialKeyButton(text = "Enter", onClick = { /* Mock Enter action */ })
}
