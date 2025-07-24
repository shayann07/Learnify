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
import com.example.elearningplatform.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val vm: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loginBtn.setOnClickListener {
            vm.login(
                binding.emailEdit.text.toString().trim(),
                binding.passwordEdit.text.toString()
            )
        }
        binding.toRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        vm.state.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Result.Loading -> binding.loginBtn.isEnabled = false

                is Result.Success -> {
                    // use the ACTION id, not the fragment id
                    findNavController().navigate(R.id.action_login_to_home)
                }

                is Result.Error -> {
                    binding.loginBtn.isEnabled = true
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
