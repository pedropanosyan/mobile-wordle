import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import java.net.URI

fun getRandomWord(length: Int, lang: String, onSuccess: (word: String) -> Unit) {
    Thread {
        val apiUrl = "https://random-word-api.herokuapp.com/word?length=$length&lang=$lang"
        Log.d("getRandomWord", "API URL: $apiUrl")

        try {
            val url: URL = URI.create(apiUrl).toURL()
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connectTimeout = 5000 // Optional: Set a timeout value for the connection
            connection.readTimeout = 5000    // Optional: Set a timeout value for reading the response

            val responseCode = connection.responseCode
            Log.d("getRandomWord", "Response Code: $responseCode")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                onSuccess(response.trim('[', ']', '"'))
            } else {
                Log.e("getRandomWord", "Failed to fetch word. Response Code: $responseCode")
            }

            connection.disconnect()
        } catch (e: Exception) {
            Log.e("getRandomWord", "Exception: ${e.message}", e)
        }
    }.start()
}
