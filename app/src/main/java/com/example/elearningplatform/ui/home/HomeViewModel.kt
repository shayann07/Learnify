package com.example.elearningplatform.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elearningplatform.data.repository.CourseRepository
import com.example.elearningplatform.data.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: CourseRepository
) : ViewModel() {

    private val _courses =
        MutableLiveData<Result<List<com.example.elearningplatform.data.model.Course>>>()
    val courses: LiveData<Result<List<com.example.elearningplatform.data.model.Course>>> = _courses

    init {
        loadCourses()
    }

    private fun loadCourses() = viewModelScope.launch {
        _courses.value = Result.Loading
        try {
            _courses.value = Result.Success(repo.getCourses())
        } catch (e: Exception) {
            _courses.value = Result.Error(e)
        }
    }
}
