package com.example.elearningplatform.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elearningplatform.data.model.Course
import com.example.elearningplatform.data.model.Lesson
import com.example.elearningplatform.data.repository.CourseRepository
import com.example.elearningplatform.databinding.FragmentProfileBinding
import com.example.elearningplatform.databinding.ItemLessonBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val b get() = _binding!!

    @Inject
    lateinit var repo: CourseRepository

    /* ───────── temporary lesson list ───────── */
    private val lessons = mutableListOf<Lesson>()
    private lateinit var lessonAdapter: LessonAdapter

    override fun onCreateView(
        i: LayoutInflater, c: ViewGroup?, s: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        /* Recycler */
        lessonAdapter = LessonAdapter()
        b.lessonRecycler.apply {
            adapter = lessonAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Initial state (maybe not needed but safe)
        updateLessonsScheduledTitle()

        /* + Lesson */
        b.addLessonBtn.setOnClickListener {
            val title = b.lessonTitleEdit.text.toString().trim()
            val url = b.lessonUrlEdit.text.toString().trim()
            if (title.isBlank() || url.isBlank()) return@setOnClickListener
            lessons += Lesson(
                id = "", title = title, videoUrl = url,
                order = lessons.size, duration = 0
            )
            lessonAdapter.submit(lessons)
            updateLessonsScheduledTitle()
            b.lessonTitleEdit.text!!.clear()
            b.lessonUrlEdit.text!!.clear()
        }

        /* SAVE */
        b.saveBtn.setOnClickListener { saveCourse() }
    }

    /* ────────────────────────────────────────── */
    private fun saveCourse() {
        val course = Course(
            title = b.courseTitleEdit.text.toString().trim(),
            description = b.courseDescEdit.text.toString().trim(),
            categoryId = b.categoryEdit.text.toString().trim(),
            thumbnail = b.thumbEdit.text.toString().trim(),
            instructor = b.instructorEdit.text.toString().trim(),
            free = b.freeSwitch.isChecked,
            language = "EN"
        )

        lifecycleScope.launch {
            try {
                val cid = repo.createCourse(course)
                lessons.forEach { repo.addLesson(cid, it) }
                Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                clearForm()
            } catch (e: Exception) {
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearForm() {
        b.courseTitleEdit.text!!.clear()
        b.courseDescEdit.text!!.clear()
        b.categoryEdit.text!!.clear()
        b.thumbEdit.text!!.clear()
        b.instructorEdit.text!!.clear()
        b.freeSwitch.isChecked = true
        lessons.clear()
        lessonAdapter.submit(lessons)
        updateLessonsScheduledTitle()
    }

    // Helper to show/hide the title based on lesson count
    private fun updateLessonsScheduledTitle() {
        b.lessonsScheduledTitle.visibility =
            if (lessons.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView(); _binding = null
    }

    /* ── tiny in‑file adapter ────────────────── */
    private inner class LessonAdapter :
        RecyclerView.Adapter<LessonAdapter.VH>() {

        private var data = listOf<Lesson>()
        fun submit(list: List<Lesson>) {
            data = list; notifyDataSetChanged()
        }

        inner class VH(val bind: ItemLessonBinding) :
            RecyclerView.ViewHolder(bind.root)

        override fun onCreateViewHolder(p: ViewGroup, v: Int) =
            VH(ItemLessonBinding.inflate(layoutInflater, p, false))

        override fun onBindViewHolder(h: VH, i: Int) {
            h.bind.title.text = data[i].title
        }

        override fun getItemCount() = data.size
    }
}