package com.example.findmycar.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.findmycar.R
import com.example.findmycar.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDropdowns()

        binding.buttonSaveProfile.setOnClickListener {
            viewModel.saveProfile(
                fullName = binding.editTextFullName.text,
                bodyType = binding.autoCompleteBodyType.text.toString(),
                drivetrain = binding.autoCompleteDrivetrain.text.toString(),
                features = viewModel.uiState.value.profile?.features ?: emptyList()
            )
        }

        binding.buttonBackToWelcome.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    
                    state.profile?.let { profile ->
                        if (binding.editTextFullName.text.isNullOrEmpty()) {
                            binding.editTextFullName.setText(profile.fullName)
                        }

                        binding.textViewEmail.text = profile.email
                        
                        // Set dropdown values if not already set by user
                        if (binding.autoCompleteBodyType.text.isNullOrEmpty() && profile.preferredBodyType != null) {
                            binding.autoCompleteBodyType.setText(profile.preferredBodyType, false)
                        }
                        if (binding.autoCompleteDrivetrain.text.isNullOrEmpty() && profile.preferredDrivetrain != null) {
                            binding.autoCompleteDrivetrain.setText(profile.preferredDrivetrain, false)
                        }
                    }

                    state.successMessage?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                        viewModel.onSuccessMessageShown()
                    }

                    state.errorMessage?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                        viewModel.onErrorMessageShown()
                    }
                }
            }
        }

        viewModel.loadProfile()
    }

    private fun setupDropdowns() {
        val bodyTypes = arrayOf("Sedan", "SUV", "Truck", "Coupe", "Hatchback", "Convertible")
        val bodyTypeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, bodyTypes)
        binding.autoCompleteBodyType.setAdapter(bodyTypeAdapter)

        val drivetrains = arrayOf("FWD", "RWD", "AWD", "4WD")
        val drivetrainAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, drivetrains)
        binding.autoCompleteDrivetrain.setAdapter(drivetrainAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
