package com.example.findmycar.cardetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmycar.R
import com.example.findmycar.data.Car
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

        // Set the car data in the ViewModel immediately from the navigation arguments
        viewModel.setCar(args.car)
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
                            // Initial state, nothing to show yet
                        }
                        is CarDetailsUiState.Success -> {
                            displayCarDetails(state.car)
                        }
                        is CarDetailsUiState.Error -> {
                            // Handle potential errors if needed
                        }
                    }
                }
            }
        }
    }

    private fun displayCarDetails(car: Car) {
        binding.textViewCarName.text = getString(R.string.car_name_format, car.year, car.make, car.model)
        binding.textViewCarPrice.text = getString(R.string.price_format, car.price)
        binding.textViewDescription.text = car.description
        adapter.submitList(car.toDetailList())
        // Image loading with Coil/Glide would go here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
