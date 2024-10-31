package com.example.wordle.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.dimensionResource
import com.example.wordle.R


@Composable
fun Chip(label: String, value: String, color: Color = MaterialTheme.colorScheme.tertiary) {
    Column(
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(dimensionResource(id = R.dimen.lg)))
            .padding(
                vertical = dimensionResource(id = R.dimen.md),
                horizontal = dimensionResource(id = R.dimen.md2)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(color = color, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.sm)))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(color = color)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChipPreview() {
    Chip(label = "Score", value = "85", color = MaterialTheme.colorScheme.primary)
}