package com.example.itunesearch.model

class ResultsRepository(private val resultsDao: ResultsDao) {


    val readAllData: List<Results> = resultsDao.readAllData()

    suspend fun addMusic(music: Results) {
        resultsDao.addMusic(music)
    }

}