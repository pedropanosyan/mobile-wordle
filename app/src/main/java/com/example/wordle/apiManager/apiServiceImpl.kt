package com.example.wordle.apiManager

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.wordle.R
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit
import javax.inject.Inject

class ApiServiceImpl @Inject constructor() {

    fun getRandomWord(context: Context, onSuccess: (String) -> Unit, onFail: () -> Unit, loadingFinished: () -> Unit) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.word_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ApiService = retrofit.create(ApiService::class.java)

        val call: Call<List<String>> = service.getRandomWord()

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(response: Response<List<String>>?, retrofit: Retrofit?) {
                loadingFinished()
                if (response?.isSuccess == true) {
                    val words: List<String>? = response.body()
                    if (!words.isNullOrEmpty()) {
                        onSuccess(words[0])
                    } else {
                        Log.d("ApiServiceImpl", "Response body is null or empty")
                        onFail()
                    }
                } else {
                    Log.d("ApiServiceImpl", "Request failed with code: ${response?.code()}")
                    onFail()
                }
            }

            override fun onFailure(t: Throwable?) {
                Log.e("ApiServiceImpl", "API call failed: ${t?.message}")
                Toast.makeText(context, "Failed to fetch word", Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }
}