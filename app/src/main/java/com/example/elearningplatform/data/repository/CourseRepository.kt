package com.example.elearningplatform.data.repository

import com.example.elearningplatform.data.model.Course
import com.example.elearningplatform.data.model.Lesson
import com.example.elearningplatform.data.model.toCourse
import com.example.elearningplatform.data.model.toLesson
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun getCourses(): List<Course> =
        db.collection("courses").get().await().documents.map { it.toCourse() }

    suspend fun getLessons(courseId: String): List<Lesson> =
        db.collection("courses").document(courseId)
            .collection("lessons").orderBy("order")
            .get().await().documents.map { it.toLesson() }

    // bottom of the file
    /** creates a course and returns generated id */
    suspend fun createCourse(c: Course): String {
        val doc = db.collection("courses").document()
        doc.set(c).await()
        return doc.id
    }

    /** adds one lesson under /courses/{courseId}/lessons/{autoId} */
    suspend fun addLesson(courseId: String, l: Lesson) {
        db.collection("courses")
            .document(courseId)
            .collection("lessons")
            .add(l).await()
    }

}
