package com.example.wordleViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.Normalizer
import javax.inject.Inject
import androidx.lifecycle.AndroidViewModel
import com.example.wordle.apiManager.ApiServiceImpl
import com.example.wordle.data.Game
import com.example.wordle.data.WordleDatabase

enum class BoxColor {
    OUTLINE,
    PRIMARY,
    SECONDARY,
    TERTIARY
}

@HiltViewModel
class WordleViewModel @Inject constructor(
    application: Application,
    private val apiServiceImpl: ApiServiceImpl,
) : AndroidViewModel(application) {
    private var startTime = 0L
    var solution by mutableStateOf("")
    var isPlaying by mutableStateOf(false)
    var guesses by mutableStateOf(emptyList<String>())
    var currentColumn by mutableIntStateOf(0)
    var currentRow by mutableIntStateOf(0)
    var result by mutableStateOf("")
    private var wordSet by mutableStateOf<Set<String>>(emptySet())

    private val context: Context
        get() = getApplication<Application>().applicationContext

    private val gamesDao = WordleDatabase.getDatabase(context).gamesDao()

    init {
        loadWordsFromFile("diccionario_espanol.txt")
    }

    fun addGame(hasWon: Boolean, timePlayed: Int) {
        val newGame = Game(hasWon = hasWon, timePlayed = timePlayed)
        viewModelScope.launch(Dispatchers.IO) {
            gamesDao.insert(newGame)
        }
    }


    private fun loadWordsFromFile(fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val words = readWordsFromFile(fileName)
            wordSet = words
        }
    }

    fun fetchRandomWord() {
        apiServiceImpl.getRandomWord(
            context,
            onSuccess = { word ->
                solution = normalizeWord(word)
                Log.d("WordleViewModel", "API Success: Fetched word: $word")
                isPlaying = true
                startTime = System.currentTimeMillis()
            },
            onFail = {
                Log.d("WordleViewModel", "API Failure: Failed to fetch word")
            },
            loadingFinished = {
                Log.d("WordleViewModel", "API call finished")
            }
        )
    }

    private fun normalizeWord(word: String): String {
        return Normalizer.normalize(word, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}"), "")
            .uppercase()
    }

    fun playGame(mode: String) {
        val size = getSizeFromMode(mode, guesses.size)
        guesses = createInitialGuessesList(size)
        currentRow = 0
        currentColumn = 0
        result = ""
        isPlaying = true
        fetchRandomWord()
    }

    fun quitGame() {
        isPlaying = false
        currentRow = 0
        currentColumn = 0
        result = ""
    }


    fun enterLetter(letter: String) {
        if (currentColumn < 5) {
            guesses = guesses.toMutableList().apply {
                this[currentRow] = this[currentRow] + letter
            }
            currentColumn++
        }
    }

    fun submitWord() {
        if (currentColumn == 5) {
            if (wordSet.isEmpty()) return
            if (!wordExists(wordSet, guesses[currentRow], solution)) return
            if (checkIfGuessIsCorrect(guesses[currentRow], solution)) {
                result = "won"
                addGame(true, ((System.currentTimeMillis() - startTime) / 1000).toInt())
            }
            if (currentRow == guesses.size - 1) {
                result = "lost"
                addGame(false, ((System.currentTimeMillis() - startTime) / 1000).toInt())
            }
            currentRow++
            currentColumn = 0
        }
    }

    fun deleteLetter() {
        if (currentColumn > 0) {
            currentColumn--
            guesses = guesses.toMutableList().apply {
                this[currentRow] = this[currentRow].dropLast(1)
            }
        }
    }

    fun getFormattedSolution(): String {
        return solution.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    fun hasWon(): Boolean {
        return result == "won"
    }


    private fun readWordsFromFile(fileName: String): Set<String> {
        val wordsSet = mutableSetOf<String>()
        context.assets.open(fileName).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.forEachLine { line ->
                    line.split("\\s+".toRegex()).forEach { word ->
                        wordsSet.add(normalize(word.trim()))
                    }
                }
            }
        }
        return wordsSet
    }


}

private fun createInitialGuessesList(size: Int): List<String> {
    return MutableList(size) { "" }
}

private fun wordExists(wordsSet: Set<String>, word: String, solution: String): Boolean {
    return wordsSet.contains(normalize(word)) || word.lowercase() == solution.lowercase()
}

fun checkIfGuessIsCorrect(guess: String, solution: String): Boolean {
    return guess.lowercase() == solution.lowercase()
}

fun normalize(word: String): String {
    return Normalizer.normalize(word, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .uppercase()
}

fun getSizeFromMode(mode: String, previous: Int): Int {
    return when (mode) {
        "Facil" -> 8
        "Medio" -> 6
        "Dificil" -> 5
        "Experto" -> 3
        "Revancha" -> previous
        else -> 6
    }
}