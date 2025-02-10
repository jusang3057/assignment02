package com.example.assignment02.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment02.R
import com.example.assignment02.data.model.Word
import com.example.assignment02.ui.addword.AddWordActivity
import com.example.assignment02.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addWordButton: ImageButton
    private lateinit var editButton: ImageButton
    private lateinit var deleteButton: ImageButton
    private lateinit var selectedWordText: TextView
    private lateinit var selectedDefinitionText: TextView
    private lateinit var adapter: MainAdapter

    private var selectedWord: Word? = null

    private val wordViewModel: MainViewModel by viewModels()

    private val wordLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val word = data?.getStringExtra("word") ?: return@registerForActivityResult
                val definition = data.getStringExtra("definition") ?: return@registerForActivityResult
                val newWord = Word(word, definition)

                val mode = data.getStringExtra("mode")

                if (mode == "edit") {
                    selectedWord?.let { wordViewModel.removeWord(it) }
                }
                wordViewModel.addWord(newWord)

                selectedWordText.text = newWord.word
                selectedDefinitionText.text = newWord.definition
                editButton.visibility = View.VISIBLE
                deleteButton.visibility = View.VISIBLE
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        addWordButton = findViewById(R.id.addButton)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)
        selectedWordText = findViewById(R.id.selectedWordText)
        selectedDefinitionText = findViewById(R.id.selectedDefinitionText)

        adapter = MainAdapter { word ->
            selectedWord = word
            selectedWordText.text = word.word
            selectedDefinitionText.text = word.definition
            editButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        wordViewModel.wordList?.observe(this, Observer { words ->
            adapter.submitList(words)
        })

        addWordButton.setOnClickListener {
            val intent = Intent(this, AddWordActivity::class.java).apply {
                putExtra("mode", "add")
            }
            wordLauncher.launch(intent)
        }

        editButton.setOnClickListener {
            selectedWord?.let {
                val intent = Intent(this, AddWordActivity::class.java).apply {
                    putExtra("mode", "edit")
                    putExtra("word", it.word)
                    putExtra("definition", it.definition)
                }
                wordLauncher.launch(intent)
            }
        }

        deleteButton.setOnClickListener {
            selectedWord?.let {
                Log.d("Test", "${it.word} : ${it.definition}")
                wordViewModel.removeWord(it)
                selectedWordText.text = ""
                selectedDefinitionText.text = ""
                editButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
            }
        }
    }
}