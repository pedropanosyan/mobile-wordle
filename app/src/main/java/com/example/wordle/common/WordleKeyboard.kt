package com.example.wordle.common

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WordleKeyboard(onKeyPress: (option: String) -> Unit, onEnter: () -> Unit, onDelete: () -> Unit) {
    val keyboardRows = listOf(
        "QWERTYUIO",
        "PASDFGHJK",
        "ZXCVBNÑML"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in keyboardRows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (option in row) {
                    KeyButton(option = option.toString(), onKeyPress = onKeyPress)
                }
            }
        }
        // Add the fourth row for Enter and Delete keys
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SpecialKeyButton(text = "Enter", onClick = onEnter)
            Spacer(modifier = Modifier.width(4.dp))
            SpecialKeyButton(text = "Delete", onClick = onDelete)
        }
    }
}

@Composable
fun KeyButton(option: String, onKeyPress: (option: String) -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(4.dp))
            .padding(2.dp)
            .clickable { onKeyPress(option) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = option,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SpecialKeyButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(56.dp, 28.dp)
            .background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(4.dp))
            .padding(2.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WordleKeyboardPreview() {
    MaterialTheme {
        WordleKeyboard(onKeyPress = {}, onEnter = {}, onDelete = {})
    }
}
