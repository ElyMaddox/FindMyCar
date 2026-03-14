package com.example.findmycar.interactivesearch

import android.content.Context
import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import com.example.findmycar.aiassistant.ChatAdapter
import com.example.findmycar.data.toCar
import com.example.findmycar.databinding.FragmentInteractiveSearchBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class InteractiveSearchFragment : Fragment() {

    private var _binding: FragmentInteractiveSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InteractiveSearchViewModel by viewModels()
    private val chatAdapter = ChatAdapter { listing ->
        // Convert the listing to a Car object and pass it via Safe Args
        val car = listing.toCar()
        val action = InteractiveSearchFragmentDirections.actionInteractiveSearchFragmentToCarDetailsFragment(car)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInteractiveSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewChat.adapter = chatAdapter

        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString()
            if (messageText.isNotBlank()) {
                viewModel.sendUserMessage(messageText)
                binding.editTextMessage.text?.clear()
                hideKeyboard(view)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    chatAdapter.submitList(state.messages) {
                        if (state.messages.isNotEmpty()) {
                            binding.recyclerViewChat.smoothScrollToPosition(state.messages.size - 1)
                        }
                    }

                    binding.progressBarLoading.isVisible = state.isLoading
                    binding.buttonSend.isEnabled = !state.isLoading

                    state.error?.let { errorMessage ->
                        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
