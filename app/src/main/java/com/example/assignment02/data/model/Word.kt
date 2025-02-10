package com.example.assignment02.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word")
data class Word(
    @PrimaryKey var word: String = "",
    var definition: String = ""
) {
    constructor() : this("", "")
}