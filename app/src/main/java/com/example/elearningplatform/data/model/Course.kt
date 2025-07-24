package com.example.elearningplatform.data.model

data class Course(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val categoryId: String = "",
    val thumbnail: String = "",
    val instructor: String = "",
    val language: String = "EN",
    val free: Boolean = true
)
