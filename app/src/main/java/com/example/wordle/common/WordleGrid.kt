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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordleViewModel.BoxColor
import com.example.wordleViewModel.WordleViewModel
import kotlinx.coroutines.delay

@Composable
fun WordleGrid() {

    val viewModel = hiltViewModel<WordleViewModel>()
    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for ((rowIndex, guess) in viewModel.guesses.withIndex()) {
            val paintedCounts = mutableMapOf<Char, Int>()
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (index in 0 until 5) {
                    val char = if (index < guess.length) guess[index] else ' ' // Default to space if guess is shorter than 5
                    val boxColor = viewModel.getBoxColor(
                        char = char.toString(),
                        colIndex = index,
                        rowIndex = rowIndex,
                        paintedCounts = paintedCounts
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
