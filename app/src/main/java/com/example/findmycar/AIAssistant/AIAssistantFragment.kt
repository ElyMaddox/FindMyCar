package com.example.findmycar.AIAssistant

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.findmycar.databinding.FragmentAiAssistantBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AIAssistantFragment : Fragment() {

    companion object {
        private const val TAG = "AIAssistantFragment"
    }

    private var _binding: FragmentAiAssistantBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AiAssistantViewModel by viewModels()
    private val chatAdapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "AIAssistantFragment - onCreate() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "AIAssistantFragment - onCreateView() called")
        _binding = FragmentAiAssistantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        binding.recyclerViewChat.adapter = chatAdapter

        // Send message logic
        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString()
            if (messageText.isNotBlank()) {
                viewModel.sendUserMessage(messageText)
                binding.editTextMessage.text?.clear()

                // Hide keyboard after sending
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        // Observe UI State
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Update the list of messages
                    chatAdapter.submitList(state.messages) {
                        // Scroll to bottom when a new message is added
                        if (state.messages.isNotEmpty()) {
                            binding.recyclerViewChat.smoothScrollToPosition(state.messages.size - 1)
                        }
                    }

                    // Handle Loading State
                    binding.progressBarAi.isVisible = state.isLoading
                    binding.buttonSend.isEnabled = !state.isLoading

                    // Handle Errors
                    state.error?.let { errorMessage ->
                        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "AIAssistantFragment - onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "AIAssistantFragment - onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "AIAssistantFragment - onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "AIAssistantFragment - onStop() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "AIAssistantFragment - onDestroyView() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "AIAssistantFragment - onDestroy() called")
    }
}
