package com.example.elearningplatform.ui.course

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elearningplatform.data.utils.Result
import com.example.elearningplatform.databinding.FragmentCourseDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseDetailFragment : Fragment() {

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!
    private val vm: CourseDetailViewModel by viewModels()
    private val args: CourseDetailFragmentArgs by navArgs()
    private val adapter = LessonAdapter { lesson ->
        val action = CourseDetailFragmentDirections
            .actionCourseToPlayer(args.courseId, lesson.id, lesson.title)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.courseTitle.text = args.courseTitle
        binding.lessonRecycler.adapter = adapter
        binding.lessonRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.lessonRecycler.adapter = adapter

        vm.lessons.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Result.Loading -> binding.progress.show()
                is Result.Success -> {
                    binding.progress.hide(); adapter.submit(res.data)
                }
                is Result.Error -> { binding.progress.hide() }
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
