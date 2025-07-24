package com.example.elearningplatform.ui.course

import androidx.lifecycle.*
import com.example.elearningplatform.data.model.Lesson
import com.example.elearningplatform.data.repository.CourseRepository
import com.example.elearningplatform.data.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val repo: CourseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val courseId: String = savedStateHandle["courseId"]!!

    private val _lessons = MutableLiveData<Result<List<Lesson>>>()
    val lessons: LiveData<Result<List<Lesson>>> = _lessons

    init { loadLessons() }

    private fun loadLessons() = viewModelScope.launch {
        _lessons.value = Result.Loading
        try {
            _lessons.value = Result.Success(repo.getLessons(courseId))
        } catch (e: Exception) {
            _lessons.value = Result.Error(e)
        }
    }
}
