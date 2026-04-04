package com.example.findmycar.cardetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmycar.R
import com.example.findmycar.compare.CompareSharedViewModel
import com.example.findmycar.data.Car
import com.example.findmycar.databinding.FragmentCarDetailsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CarDetailsFragment : Fragment() {

    companion object {
        private const val TAG = "CarDetailsFragment"
    }

    private var _binding: FragmentCarDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CarDetailsViewModel by viewModels()
    private val compareSharedViewModel: CompareSharedViewModel by activityViewModels()
    private val args: CarDetailsFragmentArgs by navArgs()
    private val adapter = CarDetailsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "CarDetailsFragment - onCreate() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "CarDetailsFragment - onCreateView() called")
        _binding = FragmentCarDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "CarDetailsFragment - onViewCreated() called")

        setupRecyclerView()
        setupCompareButton()
        observeUiState()

        // Set the car data in the ViewModel immediately from the navigation arguments
        viewModel.setCar(args.car)
    }

    private fun setupCompareButton() {
        binding.buttonCompare.setOnClickListener {
            val count = compareSharedViewModel.addCar(args.car)
            if (count == 1) {
                Snackbar.make(binding.root, "Car added. Select one more to compare.", Snackbar.LENGTH_LONG).show()
            } else {
                val state = compareSharedViewModel.selectionState.value
                val action = CarDetailsFragmentDirections.actionCarDetailsFragmentToCompareFragment(
                    state.car1!!, state.car2!!
                )
                findNavController().navigate(action)
            }
        }
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

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "CarDetailsFragment - onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "CarDetailsFragment - onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "CarDetailsFragment - onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "CarDetailsFragment - onStop() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "CarDetailsFragment - onDestroyView() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "CarDetailsFragment - onDestroy() called")
    }
}
