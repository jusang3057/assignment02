package com.example.assignment02.ui.addword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment02.R

class AddWordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addword)

        val wordText = findViewById<EditText>(R.id.wordText)
        val definitionText = findViewById<EditText>(R.id.definitionText)
        val button = findViewById<Button>(R.id.button)
        val picture = findViewById<ImageView>(R.id.picture)
        val pictureBtn = findViewById<Button>(R.id.pictureBtn)

        val mode = intent.getStringExtra("mode") ?: "add"
        if (mode == "edit") {
            val word = intent.getStringExtra("word") ?: ""
            val definition = intent.getStringExtra("definition") ?: ""
            wordText.setText(word)
            definitionText.setText(definition)
        }

        button.setOnClickListener {
            val word = wordText.text.toString().trim()
            val definition = definitionText.text.toString().trim()

            if (word.isNotEmpty() && definition.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra("word", word)
                    putExtra("definition", definition)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
