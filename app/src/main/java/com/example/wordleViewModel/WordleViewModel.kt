package com.example.wordleViewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.common.normalize
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
import androidx.compose.ui.platform.LocalContext


@HiltViewModel
class WordleViewModel @Inject constructor() : ViewModel() {

    val context = Act

    var solution: String by mutableStateOf("")
    var isPlaying: Boolean = false
    var guesses = createInitialGuessesMatrix()
    var currentColumn = 0
    var currentRow = 0
    var result = ""
    var worldSet = mutableStateOf(readWordsFromFile(context, "diccionario_espanol.txt"))

    fun fetchRandomWord(length: Int, lang: String) {
        viewModelScope.launch {
            val word = getRandomWord(length, lang)
            solution = normalizeWord(word)
        }
    }

    private suspend fun getRandomWord(length: Int, lang: String): String {
        return withContext(Dispatchers.IO) {
            val apiUrl = "https://random-word-api.herokuapp.com/word?length=$length&lang=$lang"
            try {
                val url: URL = URI.create(apiUrl).toURL()
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    return@withContext response.trim('[', ']', '"')
                } else {
                    throw Exception("Failed to fetch word. Response Code: $responseCode")
                }
            } catch (e: Exception) {
                throw Exception("Exception: ${e.message}", e)
            }
        }
    }

    private fun normalizeWord(word: String): String {
        return Normalizer.normalize(word, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}"), "")
            .uppercase()
    }

    public fun setIsPlaying(value: Boolean) {
        isPlaying = value
    }
}

fun createInitialGuessesMatrix(): List<MutableList<String>> {
    return MutableList(6) { MutableList(5) { "" } }
}

fun readWordsFromFile(context: Context, fileName: String): Set<String> {
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