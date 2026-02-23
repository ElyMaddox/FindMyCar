package com.example.findmycar.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.findmycar.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import com.example.findmycar.R

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(this::class.java.simpleName, "Start onCreate")
        super.onCreate(savedInstanceState)

        Log.d(this::class.java.simpleName, "End onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(this::class.java.simpleName, "Start onCreateView")

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        Log.d(this::class.java.simpleName, "End onCreateView")
        return binding.root
    }

    override fun onDestroyView() {
        Log.d(this::class.java.simpleName, "Start onDestroyView")
        super.onDestroyView()
        _binding = null

        Log.d(this::class.java.simpleName, "End onDestroyView")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: execute login stuff upon login click
        binding.buttonLogin.setOnClickListener {
            Log.d(this::class.java.simpleName, "Login button clicked")
            viewModel.performLogin(
                binding.editTextUsername.text,
                binding.editTextPassword.text
            )
        }

        // Keep track of state so we know when to move on from login screen. Monitors stuff
        // from the loginViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.navigateToNextScreen) {
                        // Perform the actual navigation
                        findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)

                        // Tell the ViewModel we've navigated so it doesn't trigger again
                        viewModel.onNavigationComplete()
                    }

                    state.errorMessage?.let {
                        viewModel.onErrorMessageShown()
                    }
                }
            }
        }
    }

    override fun onStart() {
        Log.d(this::class.java.simpleName, "Start onStart")
        super.onStart()

        Log.d(this::class.java.simpleName, "End onStart")
    }

    override fun onResume() {
        Log.d(this::class.java.simpleName, "Start onResume")
        super.onResume()

        Log.d(this::class.java.simpleName, "End onResume")
    }

    override fun onPause() {
        Log.d(this::class.java.simpleName, "Start onPause")
        super.onPause()

        Log.d(this::class.java.simpleName, "End onPause")
    }

    override fun onStop() {
        Log.d(this::class.java.simpleName, "Start onStop")
        super.onStop()

        Log.d(this::class.java.simpleName, "End onStop")
    }

    override fun onDestroy() {
        Log.d(this::class.java.simpleName, "Start onDestroy")
        super.onDestroy()

        Log.d(this::class.java.simpleName, "End onDestroy")
    }
}