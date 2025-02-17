package com.example.application1.model

data class GithubData(
    val name: String,
    val description: String?,
    val star_count: Int,
    val branch_count: Int,
    val url: String
)