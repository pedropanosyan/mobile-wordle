package com.example.wordle.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val difficulties = listOf("Easy", "Medium", "Hard", "Expert")

@Composable
fun DifficultySelector(
    selectedDifficulty: String,
    onDifficultySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = selectedDifficulty,
            onValueChange = {},
            label = { Text("Select Difficulty") },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    Modifier.clickable { expanded = !expanded }
                )
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            difficulties.forEach { difficulty ->
                DropdownMenuItem(
                    text = { Text(difficulty) },
                    onClick = {
                        onDifficultySelected(difficulty)
                        expanded = false
                    }
                )
            }
        }

    }
}

@Preview
@Composable
fun DifficultySelectorPreview() {
    DifficultySelector("Easy", {})
}