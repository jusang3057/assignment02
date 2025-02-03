package com.example.assignment02.data.repository

import android.content.Context
import com.example.assignment02.data.model.Word
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WordDatabase(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("word_db", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveWords(wordList: List<Word>) {
        val json = gson.toJson(wordList)
        sharedPreferences.edit().putString("word_list", json).apply()
    }

    fun getWords(): List<Word> {
        val json = sharedPreferences.getString("word_list", "[]") ?: "[]"
        val type = object : TypeToken<List<Word>>() {}.type
        return gson.fromJson(json, type)
    }
}