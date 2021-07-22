package com.example.itunesearch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.itunesearch.model.Results
import com.example.itunesearch.model.ResultsDatabase
import com.example.itunesearch.model.ResultsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class resultViewModel(application: Application) : AndroidViewModel(application) {

    val readAlldata: List<Results>
    private val repository: ResultsRepository

    init {
        val resultDao = ResultsDatabase.getDatabase(application).resDao()
        repository = ResultsRepository(resultDao)
        readAlldata = repository.readAllData


    }


    fun addMusic(music: Results) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMusic(music)
        }
    }

}