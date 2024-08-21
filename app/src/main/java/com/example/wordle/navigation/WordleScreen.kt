package com.example.wordle.navigation

enum class WordleScreen {
    Home,
    Stats,
}

val basePages = listOf(
    WordleScreen.Home.name,
    WordleScreen.Stats.name,
)
