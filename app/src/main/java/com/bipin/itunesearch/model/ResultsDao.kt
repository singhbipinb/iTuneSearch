package com.example.itunesearch.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ResultsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMusic(music: Results)

    @Query("select * from music_stored")
    fun readAllData(): List<Results>


}