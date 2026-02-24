package com.example.findmycar.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.findmycar.R
import com.example.findmycar.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            viewModel.performLogin(
                binding.editTextUsername.text,
                binding.editTextPassword.text
            )
        }

        binding.buttonSignup.setOnClickListener {
            viewModel.performSignUp(
                binding.editTextUsername.text,
                binding.editTextPassword.text
            )
        }

        // Observe UI State
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Handle Loading State
                    binding.progressBar.isVisible = state.isLoading
                    binding.buttonLogin.isEnabled = !state.isLoading
                    binding.buttonSignup.isEnabled = !state.isLoading

                    // Handle Navigation
                    if (state.navigateToNextScreen) {
                        findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
                        viewModel.onNavigationComplete()
                    }

                    // Handle Errors
                    state.errorMessage?.let { error ->
                        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                        viewModel.onErrorMessageShown()
                    }

                    // Handle Success Messages (e.g., check email)
                    state.successMessage?.let { success ->
                        Snackbar.make(binding.root, success, Snackbar.LENGTH_LONG).show()
                        viewModel.onSuccessMessageShown()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
