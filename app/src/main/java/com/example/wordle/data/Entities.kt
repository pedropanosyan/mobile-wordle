package com.example.wordle.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hasWon: Boolean,
    val timePlayed: Int,
)
