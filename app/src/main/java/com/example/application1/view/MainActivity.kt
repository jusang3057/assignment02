package com.example.application1.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.example.application1.databinding.ActivityMainBinding
import com.example.application1.model.GithubData
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var executor: ScheduledExecutorService
    private var searchFuture: ScheduledFuture<*>? = null
    private lateinit var adapter: MainAdapter
    private var Loading = false
    private var page = 1
    private var pQuery: String? = null

    private val repoList = mutableListOf<GithubData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executor = Executors.newSingleThreadScheduledExecutor()

        adapter = MainAdapter(repoList)
        binding.RecyclerView.layoutManager = LinearLayoutManager(this)
        binding.RecyclerView.adapter = adapter

        binding.progressBar.visibility = View.GONE

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                startNewSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchFuture?.cancel(false)
                searchFuture = executor.schedule(
                    { startNewSearch(newText) },
                    500,
                    TimeUnit.MILLISECONDS
                )
                return true
            }
        })

        // 스크롤시 30개 추가로 불러오는 코드
        binding.RecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!Loading && lastVisibleItem >= totalItemCount - 5) {
                    loadMoreResults()
                }
            }
        })
    }

    private fun startNewSearch(query: String?) {
        if (query.isNullOrEmpty()) return

        pQuery = query
        page = 1  // 페이지 번호 초기화
        repoList.clear()  // 기존 데이터 삭제
        adapter.notifyDataSetChanged()
        loadMoreResults()
    }

    private fun loadMoreResults() {
        if (pQuery.isNullOrEmpty()) return
        Loading = true  // 로딩 시작
        runOnUiThread { binding.progressBar.visibility = View.VISIBLE }

        thread {
            try {
                val url = URL("https://api.github.com/search/repositories?q=${pQuery}&per_page=30&page=${page}")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(response)
                    val items = jsonObject.getJSONArray("items")

                    val newResults = mutableListOf<GithubData>()
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val name = item.getString("name")
                        val description = item.optString("description", "No description")
                        val stars = item.getInt("stargazers_count")
                        val forks = item.getInt("forks_count")
                        val url = item.getString("html_url")

                        newResults.add(GithubData(name, description, stars, forks, url))
                    }

                    runOnUiThread {
                        repoList.addAll(newResults)
                        adapter.notifyDataSetChanged()
                        page++
                        Loading = false
                        binding.progressBar.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                // 오류나면 모두 멈춤
                e.printStackTrace()
                Loading = false
                runOnUiThread { binding.progressBar.visibility = View.GONE }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}