package com.example.assignment02.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.assignment02.data.repository.WordDatabase
import com.example.assignment02.data.model.Word

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val wordDatabase: WordDatabase = WordDatabase(application)
    private var wordList: List<Word> = emptyList()

    fun addWord(word: Word) {
        val updatedList = wordList.toMutableList()
        updatedList.add(word)
        wordDatabase.saveWords(updatedList)
        wordList = updatedList
    }

    init {
        loadWords()
    }

    fun loadWords() {
        wordList = wordDatabase.getWords()
    }

    fun getWords(): List<Word> {
        return wordList
    }

    fun removeWord(word: Word) {
        val updatedList = wordList.toMutableList()
        updatedList.remove(word)
        wordDatabase.saveWords(updatedList)
        wordList = updatedList
    }
}