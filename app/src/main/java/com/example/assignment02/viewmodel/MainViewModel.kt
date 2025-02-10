package com.example.assignment02.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.assignment02.data.model.Word
import com.example.assignment02.data.repository.WordDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val wordDao = WordDatabase.getInstance(application)?.wordDao()
    val wordList = wordDao?.getData()?.asLiveData()

    fun addWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            wordDao?.insert(word)
        }
    }

    fun removeWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            wordDao?.delete(word)
        }
    }
}
