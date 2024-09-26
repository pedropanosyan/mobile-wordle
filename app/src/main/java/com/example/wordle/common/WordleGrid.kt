package com.example.wordle.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordleViewModel.BoxColor
import com.example.wordleViewModel.WordleViewModel
import kotlinx.coroutines.delay

@Composable
fun WordleGrid(
    guesses: List<String>,
    solution: String,
    currentRow: Int
) {

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for ((rowIndex, guess) in guesses.withIndex()) {
            val paintedCounts = mutableMapOf<Char, Int>()
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (index in 0 until 5) {
                    val char = if (index < guess.length) guess[index] else ' '
                    val boxColor = getBoxColor(
                        char = char.toString(),
                        colIndex = index,
                        rowIndex = rowIndex,
                        paintedCounts = paintedCounts,
                        currentRow = currentRow,
                        solution = solution
                    )
                    LetterBox(
                        char = if (char == ' ') ' ' else char,
                        delayMs = index * 300L,
                        backgroundColor = getColorFromBoxColor(boxColor)
                    )
                }
            }
        }
    }
}

fun getBoxColor(
    char: String,
    colIndex: Int,
    rowIndex: Int,
    paintedCounts: MutableMap<Char, Int>,
    currentRow: Int,
    solution: String
): BoxColor {
    val solutionCharCounts = solution.groupingBy { it }.eachCount()
    return when {
        rowIndex >= currentRow -> BoxColor.OUTLINE
        char.isNotEmpty() && solution[colIndex] == char.first() -> {
            paintedCounts[char[0]] = (paintedCounts[char[0]] ?: 0) + 1
            BoxColor.PRIMARY
        }
        char.isNotEmpty() && solution.contains(char.first()) -> {
            val currentChar = char[0]
            val solutionCount = solutionCharCounts[currentChar] ?: 0
            val paintedCount = paintedCounts[currentChar] ?: 0

            if (paintedCount < solutionCount) {
                paintedCounts[currentChar] = paintedCount + 1
                BoxColor.SECONDARY
            } else {
                BoxColor.TERTIARY
            }
        }
        else -> BoxColor.TERTIARY
    }
}

@Composable
fun getColorFromBoxColor(boxColor: BoxColor): Color {
    return when (boxColor) {
        BoxColor.OUTLINE -> MaterialTheme.colorScheme.outline
        BoxColor.PRIMARY -> MaterialTheme.colorScheme.primary
        BoxColor.SECONDARY -> MaterialTheme.colorScheme.secondary
        BoxColor.TERTIARY -> MaterialTheme.colorScheme.tertiary
    }
}

@Composable
fun LetterBox(char: Char, backgroundColor: Color, delayMs: Long) {

    var shouldAnimate by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delayMs)
        shouldAnimate = true
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (shouldAnimate) backgroundColor else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(animatedBackgroundColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char.toString(),
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WordleGridPreview() {
    WordleGrid(
        guesses = listOf("HELLO", "WORLD", "OTHER"),
        solution = "OTHER",
        currentRow = 3
    )
}

@Preview(showBackground = true)
@Composable
fun LetterBoxPreview() {
    LetterBox(
        char = 'H',
        backgroundColor = MaterialTheme.colorScheme.primary,
        delayMs = 0L
    )
}
