package com.example.application1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application1.model.GithubData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainViewModel : ViewModel() {
    private val _repoList = MutableLiveData<List<GithubData>>(emptyList())
    val repoList: LiveData<List<GithubData>> = _repoList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentQuery: String? = null
    private var page = 1

    fun onSearchQueryChanged(query: String?) {
        if (query.isNullOrEmpty()) return

        currentQuery = query
        page = 1
        _repoList.value = emptyList()
        loadMoreResults()
    }

    fun loadMoreResults() {
        if (currentQuery.isNullOrEmpty()) return

        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            delay(500) // 디바운싱 효과
            try {
                val url = URL("https://api.github.com/search/repositories?q=${currentQuery}&per_page=30&page=${page}")
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

                    _repoList.postValue(_repoList.value.orEmpty() + newResults)
                    page++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
