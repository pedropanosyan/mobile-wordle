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
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.text.Normalizer
import javax.inject.Inject
import androidx.lifecycle.AndroidViewModel
import com.example.wordle.apiManager.ApiServiceImpl

enum class BoxColor {
    OUTLINE,
    PRIMARY,
    SECONDARY,
    TERTIARY
}

@HiltViewModel
class WordleViewModel @Inject constructor(
    application: Application,
    private val apiServiceImpl: ApiServiceImpl
) : AndroidViewModel(application) {

    var solution by mutableStateOf("")
    var isPlaying by mutableStateOf(false)
    var guesses by mutableStateOf(createInitialGuessesList())
    var currentColumn by mutableIntStateOf(0)
    var currentRow by mutableIntStateOf(0)
    var result by mutableStateOf("")
    private var wordSet by mutableStateOf<Set<String>>(emptySet())

    private val context: Context
        get() = getApplication<Application>().applicationContext

    init {
        loadWordsFromFile("diccionario_espanol.txt")
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

    fun playGame() {
        guesses = createInitialGuessesList()
        currentRow = 0
        currentColumn = 0
        result = ""
        isPlaying = true
        fetchRandomWord()
    }

    fun quitGame() {
        isPlaying = false
        guesses = createInitialGuessesList()
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
            if (!wordExists(wordSet, guesses[currentRow])) return
            if (checkIfGuessIsCorrect(guesses[currentRow], solution)) result = "won"
            if (currentRow == 5) result = "lost"
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

private fun createInitialGuessesList(): List<String> {
    return MutableList(6) { "" }
}

private fun wordExists(wordsSet: Set<String>, word: String): Boolean {
    return wordsSet.contains(normalize(word))
}

fun checkIfGuessIsCorrect(guess: String, solution: String): Boolean {
    return guess.lowercase() == solution
}

fun normalize(word: String): String {
    return Normalizer.normalize(word, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .uppercase()
}
