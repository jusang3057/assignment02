package com.example.assignment02.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment02.R
import com.example.assignment02.data.model.Word

class MainAdapter(private val onItemClick: ((Word) -> Unit) ?= null) : RecyclerView.Adapter<MainAdapter.WordViewHolder>() {

    private var wordList = listOf<Word>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = wordList[position]
        holder.bind(word)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(word)
        }
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    fun submitList(list: List<Word>) {
        wordList = list
        notifyDataSetChanged()
    }

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordText: TextView = itemView.findViewById(R.id.wordText)
        private val definitionText: TextView = itemView.findViewById(R.id.definitionText)

        fun bind(word: Word) {
            wordText.text = word.word
            definitionText.text = word.definition
        }
    }
}
