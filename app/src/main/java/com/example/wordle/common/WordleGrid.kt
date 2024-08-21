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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun WordleGrid(
    guesses: List<List<String>>,
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
                for ((index, char) in guess.withIndex()) {
                    LetterBox(
                        char = if (char.isEmpty()) ' ' else char[0],
                        delayMs = index * 300L,
                        backgroundColor = getBoxColor(
                            char,
                            solution,
                            colIndex = index,
                            rowIndex,
                            currentRow,
                            paintedCounts
                        )
                    )
                }
            }
        }
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

@Composable
fun getBoxColor(
    char: String,
    solution: String,
    colIndex: Int,
    rowIndex: Int,
    currentRow: Int,
    paintedCounts: MutableMap<Char, Int>
): Color {

    val solutionCharCounts = solution.groupingBy { it }.eachCount()

    return when {
        rowIndex >= currentRow -> MaterialTheme.colorScheme.outline
        char.isNotEmpty() && solution[colIndex] == char.first() -> {
            paintedCounts[char[0]] = (paintedCounts[char[0]] ?: 0) + 1
            MaterialTheme.colorScheme.primary
        }
        char.isNotEmpty() && solution.contains(char.first()) -> {
            val currentChar = char[0]
            val solutionCount = solutionCharCounts[currentChar] ?: 0
            val paintedCount = paintedCounts[currentChar] ?: 0

            if (paintedCount < solutionCount) {
                paintedCounts[currentChar] = paintedCount + 1
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.tertiary
            }
        }
        else -> MaterialTheme.colorScheme.tertiary
    }
}
