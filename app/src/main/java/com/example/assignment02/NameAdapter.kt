package com.example.assignment02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NameAdapter(
    private val nameList: MutableList<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<NameAdapter.NameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_name, parent, false)
        return NameViewHolder(view)
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.name_text.text = nameList[position]
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = nameList.size

    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name_text: TextView = itemView.findViewById(R.id.name_text)
    }
}
