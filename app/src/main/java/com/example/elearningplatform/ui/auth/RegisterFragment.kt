package com.example.elearningplatform.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.elearningplatform.R
import com.example.elearningplatform.data.utils.Result
import com.example.elearningplatform.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val vm: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.registerBtn.setOnClickListener {
            vm.register(
                binding.emailEdit.text.toString().trim(),
                binding.passwordEdit.text.toString(),
                binding.nameEdit.text.toString().trim()
            )
        }
        binding.toLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }

        vm.state.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Result.Loading -> binding.registerBtn.isEnabled = false

                is Result.Success -> findNavController()
                    .navigate(R.id.action_register_to_login)   // ← go to Login, not Home

                is Result.Error -> {
                    binding.registerBtn.isEnabled = true
                    binding.emailInputLayout.error = res.throwable.localizedMessage
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
