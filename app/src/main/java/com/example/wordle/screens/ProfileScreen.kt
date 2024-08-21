package com.example.wordle.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle.common.Chip

data class ChipData(val label: String, val value: String, val color: Color? = null)

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToGame: () -> Unit
) {
    val chips = listOf(
        ChipData(label = "Total Games Played", value = "78"),
        ChipData(label = "Total Wins", value = "45", color = MaterialTheme.colorScheme.primary),
        ChipData(label = "Total Losses", value = "33", color = MaterialTheme.colorScheme.error)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espaciado entre los elementos
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
                    value = "3:45",
                    color = MaterialTheme.colorScheme.primary
                )
                Chip(
                    label = "Worst winning time",
                    value = "2:30",
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
                    value = "5",
                    color = MaterialTheme.colorScheme.primary
                )
                Chip(
                    label = "Worst strike ❌",
                    value = "2",
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
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "3" + "\uD83D\uDD25",
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
                    value = "5",
                    color = MaterialTheme.colorScheme.tertiary
                )
                Chip(
                    label = "Average match time",
                    value = "2",
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}
