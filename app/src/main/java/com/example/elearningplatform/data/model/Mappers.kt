package com.example.elearningplatform.data.model

import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toCourse(): Course = Course(
    id = id,
    title = getString("title") ?: "",
    description = getString("description") ?: "",
    categoryId = getString("categoryId") ?: "",
    thumbnail = getString("thumbnail") ?: "",
    instructor = getString("instructor") ?: "",
    language = getString("language") ?: "EN",
    free = getBoolean("free") ?: true
)

fun DocumentSnapshot.toLesson(): Lesson = Lesson(
    id = id,
    title = getString("title") ?: "",
    videoUrl = getString("videoUrl") ?: "",
    duration = (getLong("duration") ?: 0L).toInt(),
    order = (getLong("order") ?: 0L).toInt()
)
