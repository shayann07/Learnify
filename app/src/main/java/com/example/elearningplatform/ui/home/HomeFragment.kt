// replace previous placeholder version
package com.example.elearningplatform.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.elearningplatform.R
import com.example.elearningplatform.data.utils.Result
import com.example.elearningplatform.data.utils.show
import com.example.elearningplatform.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val vm: HomeViewModel by viewModels()
    private val adapter = CourseAdapter { course ->
        val action = HomeFragmentDirections
            .actionHomeToCourse(courseId = course.id, courseTitle = course.title)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        vm.courses.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Result.Loading -> binding.progress.show()
                is Result.Success -> {
                    binding.progress.hide()
                    adapter.submit(res.data)
                }

                is Result.Error -> {
                    binding.progress.hide()
                    binding.empty.text = res.throwable.localizedMessage
                    binding.empty.show()
                }
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView(); _binding = null
    }
}
