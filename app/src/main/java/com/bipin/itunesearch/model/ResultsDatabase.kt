package com.example.itunesearch.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Results::class], version = 1, exportSchema = false)
abstract class ResultsDatabase : RoomDatabase() {

    abstract fun resDao(): ResultsDao

    companion object {
        @Volatile
        private var INSTANCE: ResultsDatabase? = null

        fun getDatabase(context: Context): ResultsDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ResultsDatabase::class.java,
                    "music_database"
                ).build()
                INSTANCE = instance
                return instance
            }


        }

    }

}