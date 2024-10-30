package com.example.wordle.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle.R
import com.example.wordle.common.Chip
import com.example.wordle.data.Game
import kotlinx.coroutines.flow.Flow

data class ChipData(val label: String, val value: String, val color: Color? = null)

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    totalGamesPlayed: Int,
    totalWins: Int,
    totalLosses: Int,
    bestWinningTime: Int,
    worstWinningTime: Int,
    averageMatchTime: Int,
    totalPlayTime: Int,
    allGames: List<Game>,
    onNavigateToGame: () -> Unit,
) {
    val chips = listOf(
        ChipData(
            label = stringResource(id = R.string.total_games),
            value = totalGamesPlayed.toString(),
            color = MaterialTheme.colorScheme.secondary
        ),
        ChipData(
            label = stringResource(id = R.string.total_wins),
            value = totalWins.toString(),
            color = MaterialTheme.colorScheme.primary
        ),
        ChipData(
            label = stringResource(id = R.string.total_losses),
            value = totalLosses.toString(),
            color = MaterialTheme.colorScheme.error
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.lg)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.lg))
    ) {
        item {
            Text(
                text = stringResource(id = R.string.profile),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.xxl).value.sp
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.md)))
        }
        item {
            Text(
                text = stringResource(id = R.string.performance),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.xl).value.sp
                )
            )
        }
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.md))
            ) {
                items(chips) { chip ->
                    Chip(label = chip.label, value = chip.value, color = chip.color ?: MaterialTheme.colorScheme.tertiary)
                }
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.time_scores),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.xl).value.sp
                )
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.lg))) {
                Chip(
                    label = stringResource(id = R.string.best_winning_time),
                    value = formatTime(bestWinningTime),
                    color = MaterialTheme.colorScheme.primary
                )
                Chip(
                    label = stringResource(id = R.string.worst_winning_time),
                    value = formatTime(worstWinningTime),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.strike_scores),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.xl).value.sp
                )
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.lg))) {
                Chip(
                    label = stringResource(id = R.string.best_strike_score),
                    value = getBestStrike(games = allGames),
                    color = MaterialTheme.colorScheme.primary
                )
                Chip(
                    label = stringResource(id = R.string.worst_strike_score),
                    value = getWorstStrike(games = allGames),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.current_strike_score),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.xl).value.sp
                )
            )
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.eighty))
                    .background(
                        getLastGameColor(games = allGames),
                        RoundedCornerShape(dimensionResource(id = R.dimen.lg))
                    )
            ) {
                Text(
                    text = getCurrentStrike(games = allGames).toString() + getLastGameIcon(games = allGames),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.xxxl).value.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.extra_stats),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.xl).value.sp
                )
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.lg))) {
                Chip(
                    label = stringResource(id = R.string.total_time_played),
                    value = formatTime(totalPlayTime),
                    color = MaterialTheme.colorScheme.tertiary
                )
                Chip(
                    label = stringResource(id = R.string.average_match_time),
                    value = formatTime(averageMatchTime),
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

fun getBestStrike(games: List<Game>): String {
    var max = 0
    var current = 0

    games.forEach { game ->
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

fun getWorstStrike(games: List<Game>): String {
    var max = 0
    var current = 0

    games.forEach { game ->
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

fun getCurrentStrike(games: List<Game>): Int {
    val reversedGames = games.toMutableList().reversed()

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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        totalGamesPlayed = 50,
        totalWins = 30,
        totalLosses = 20,
        bestWinningTime = 120,
        worstWinningTime = 300,
        averageMatchTime = 180,
        totalPlayTime = 9000,
        allGames = sampleGames(),
        onNavigateToGame = {}
    )
}

fun sampleGames(): List<Game> {
    return listOf(
        Game(id = 1, hasWon = true, timePlayed = 120),
        Game(id = 2, hasWon = false, timePlayed = 300),
        Game(id = 3, hasWon = true, timePlayed = 150)
    )
}
