package com.example.findmycar.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.findmycar.R
import com.example.findmycar.compare.CompareSharedViewModel
import com.example.findmycar.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val compareSharedViewModel: CompareSharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeFragment - onCreate() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "HomeFragment - onCreateView() called")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGoToProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.buttonGoToAi.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aiAssistantFragment)
        }

        binding.buttonInteractiveSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_interactiveSearchFragment)
        }

        binding.buttonCompareCars.setOnClickListener {
            val state = compareSharedViewModel.selectionState.value
            if (state.car1 != null && state.car2 != null) {
                val action = HomeFragmentDirections.actionHomeFragmentToCompareFragment(
                    state.car1, state.car2
                )
                findNavController().navigate(action)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                compareSharedViewModel.selectionState.collect { state ->
                    binding.buttonCompareCars.isEnabled = state.selectedCount == 2
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "HomeFragment - onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "HomeFragment - onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "HomeFragment - onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "HomeFragment - onStop() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "HomeFragment - onDestroyView() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "HomeFragment - onDestroy() called")
    }
}
