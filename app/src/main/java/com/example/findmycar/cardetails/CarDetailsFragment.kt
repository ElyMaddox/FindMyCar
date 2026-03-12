package com.example.findmycar.cardetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmycar.R
import com.example.findmycar.databinding.FragmentCarDetailsBinding
import kotlinx.coroutines.launch

class CarDetailsFragment : Fragment() {

    private var _binding: FragmentCarDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CarDetailsViewModel by viewModels()
    private val args: CarDetailsFragmentArgs by navArgs()
    private val adapter = CarDetailsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeUiState()

        viewModel.loadCarDetails(args.carId)
    }

    private fun setupRecyclerView() {
        binding.recyclerViewDetails.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewDetails.adapter = adapter
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CarDetailsUiState.Loading -> {
                            // Show loading if needed
                        }
                        is CarDetailsUiState.Success -> {
                            val car = state.car
                            binding.textViewCarName.text = getString(R.string.car_name_format, car.year, car.make, car.model)
                            binding.textViewCarPrice.text = getString(R.string.price_format, car.price)
                            binding.textViewDescription.text = car.description
                            adapter.submitList(car.toDetailList())
                            
                            // Load image here if we had Glide/Coil
                        }
                        is CarDetailsUiState.Error -> {
                            Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                        }
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
