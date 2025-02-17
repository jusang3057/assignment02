package com.example.application1.view

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application1.databinding.ItemRepoBinding
import com.example.application1.model.GithubData

class MainAdapter(private var repoList: List<GithubData>) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    class MainViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val repo = repoList[position]
        with(holder.binding) {
            repoName.text = repo.name
            repoDescription.text = repo.description ?: " "
            repoStar.text = "☆ ${repo.star_count}"
            repoBranch.text = "Ψ ${repo.branch_count}"

            root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.url))
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = repoList.size

    fun updateData(newList: List<GithubData>) {
        repoList = newList
        notifyDataSetChanged()
    }
}
