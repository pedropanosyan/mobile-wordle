package com.example.wordle.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Game::class], version = 1)
abstract class WordleDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao

    companion object {
        @Volatile
        private var INSTANCE: WordleDatabase? = null
        fun getDatabase(context: Context): WordleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordleDatabase::class.java,
                    "wordle_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}