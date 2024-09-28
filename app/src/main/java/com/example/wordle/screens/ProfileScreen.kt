package com.example.wordle.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordle.common.Chip
import com.example.wordle.data.Game
import com.example.wordleViewModel.StatsViewModel

data class ChipData(val label: String, val value: String, val color: Color? = null)

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToGame: () -> Unit,
) {
    val viewModel = hiltViewModel<StatsViewModel>()
    val allGames = viewModel.getAllGames.collectAsState(initial = emptyList())
    val totalGamesPlayed = viewModel.totalGamesPlayed.collectAsState(initial = 0)
    val totalWins = viewModel.totalWins.collectAsState(initial = 0)
    val totalLosses = viewModel.totalLosses.collectAsState(initial = 0)
    val bestWinningTime = viewModel.bestWinningTime.collectAsState(initial = 0)
    val worstWinningTime = viewModel.worstWinningTime.collectAsState(initial = 0)
    val averageMatchTime = viewModel.averageMatchTime.collectAsState(initial = 0)
    val totalPlayTime = viewModel.totalPlayTime.collectAsState(initial = 0)

    val chips = listOf(
        ChipData(
            label = "Total Games",
            value = totalGamesPlayed.value.toString(),
            color = MaterialTheme.colorScheme.secondary
        ),
        ChipData(
            label = "Total Wins",
            value = totalWins.value.toString(),
            color = MaterialTheme.colorScheme.primary
        ),
        ChipData(
            label = "Total Losses",
            value = totalLosses.value.toString(),
            color = MaterialTheme.colorScheme.error
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Profile Screen",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                text = "Overall Performance",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
        }
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chips) { chip ->
                    Chip(label = chip.label, value = chip.value, color = chip.color ?: MaterialTheme.colorScheme.tertiary)
                }
            }
        }
        item {
            Text(
                text = "Time Scores",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Chip(
                    label = "Best winning time",
                    value = formatTime(bestWinningTime.value),
                    color = MaterialTheme.colorScheme.primary
                )
                Chip(
                    label = "Worst winning time",
                    value = formatTime(worstWinningTime.value),
                    color = MaterialTheme.colorScheme.error
                )
            }

        }
        item {
            Text(
                text = "Strike Scores",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Chip(
                    label = "Best strike ✅",
                    value = getBestStrike(games = allGames),
                    color = MaterialTheme.colorScheme.primary
                )
                Chip(
                    label = "Worst strike ❌",
                    value = getWorstStrike(games = allGames),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        item {
            Text(
                text = "Current Strike",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(getLastGameColor(games = allGames.value), RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = getCurrentStrike(games = allGames).toString() + getLastGameIcon(games = allGames.value),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        item {
            Text(
                text = "Extra stats",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Chip(
                    label = "Total time played",
                    value = formatTime(totalPlayTime.value),
                    color = MaterialTheme.colorScheme.tertiary
                )
                Chip(
                    label = "Average match time",
                    value = formatTime(averageMatchTime.value),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

fun getBestStrike(games: State<List<Game>>): String {
    var max = 0
    var current = 0

    games.value.forEach { game ->
        if (game.hasWon) {
            current++
            if (current > max) {
                max = current
            }
        } else {
            current = 0
        }
    }
    return max.toString()
}

fun getWorstStrike(games: State<List<Game>>): String {
    var max = 0
    var current = 0

    games.value.forEach { game ->
        if (!game.hasWon) {
            current++
            if (current > max) {
                max = current
            }
        } else {
            current = 0
        }
    }
    return max.toString()
}

fun getCurrentStrike(games: State<List<Game>>): Int {
    val reversedGames = games.value.toMutableList().reversed()

    if (reversedGames.isEmpty()) return 0

    var current = 0
    val resultToCompare = reversedGames.first().hasWon
    reversedGames.forEach { game ->
        if (game.hasWon == resultToCompare) {
            current++
        } else {
            return current
        }
    }

    return current
}

@Composable
fun getLastGameColor(games: List<Game>): Color {
    if (games.isEmpty()) {
        return MaterialTheme.colorScheme.tertiary
    }

    return if (!games.last().hasWon) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }
}

fun getLastGameIcon(games: List<Game>): String {
    if (games.isEmpty()) {
        return "❔"
    }

    return if (!games.last().hasWon) {
        "\uD83D\uDD3B"
    } else {
        "\uD83D\uDD25"
    }
}