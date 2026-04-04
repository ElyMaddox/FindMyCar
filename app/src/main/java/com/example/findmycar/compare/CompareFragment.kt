package com.example.findmycar.compare

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.findmycar.R
import com.example.findmycar.data.Car
import com.example.findmycar.databinding.FragmentCompareBinding
import kotlinx.coroutines.launch
import java.util.Locale

class CompareFragment : Fragment() {

    companion object {
        private const val TAG = "CompareFragment"
    }

    private var _binding: FragmentCompareBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompareViewModel by viewModels()
    private val sharedViewModel: CompareSharedViewModel by activityViewModels()
    private val args: CompareFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "CompareFragment - onCreate() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "CompareFragment - onCreateView() called")
        _binding = FragmentCompareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "CompareFragment - onViewCreated() called")

        binding.buttonClear.setOnClickListener {
            viewModel.clearComparison()
            sharedViewModel.clearSelection()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CompareUiState.Loading -> {
                            Log.d(TAG, "State: Loading")
                        }
                        is CompareUiState.Ready -> {
                            displayComparison(state.car1, state.car2)
                        }
                        is CompareUiState.Error -> {
                            Log.e(TAG, "State: Error - ${state.message}")
                        }
                    }
                }
            }
        }

        viewModel.setCars(args.car1, args.car2)
    }

    private fun displayComparison(car1: Car, car2: Car) {
        // Images
        binding.imageViewCar1.load(car1.imageUrl.ifBlank { null }) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.ic_launcher_background)
        }
        binding.imageViewCar2.load(car2.imageUrl.ifBlank { null }) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.ic_launcher_background)
        }

        // Names
        binding.textViewCar1Name.text = getString(R.string.car_name_format, car1.year, car1.make, car1.model)
        binding.textViewCar2Name.text = getString(R.string.car_name_format, car2.year, car2.make, car2.model)

        // Specs - with highlighting for better values
        bindSpec(binding.textViewCar1Price, binding.textViewCar2Price,
            String.format(Locale.US, "$%,.0f", car1.price),
            String.format(Locale.US, "$%,.0f", car2.price),
            car1.price, car2.price, lowerIsBetter = true)

        bindSpec(binding.textViewCar1Mileage, binding.textViewCar2Mileage,
            String.format(Locale.US, "%,d mi", car1.mileage),
            String.format(Locale.US, "%,d mi", car2.mileage),
            car1.mileage.toDouble(), car2.mileage.toDouble(), lowerIsBetter = true)

        bindSpec(binding.textViewCar1Horsepower, binding.textViewCar2Horsepower,
            "${car1.horsepower} hp",
            "${car2.horsepower} hp",
            car1.horsepower.toDouble(), car2.horsepower.toDouble(), lowerIsBetter = false)

        // Year - higher is better
        bindSpec(binding.textViewCar1Year, binding.textViewCar2Year,
            car1.year.toString(),
            car2.year.toString(),
            car1.year.toDouble(), car2.year.toDouble(), lowerIsBetter = false)

        // Non-comparable specs (just display)
        binding.textViewCar1FuelType.text = car1.fuelType.ifBlank { "N/A" }
        binding.textViewCar2FuelType.text = car2.fuelType.ifBlank { "N/A" }

        binding.textViewCar1Transmission.text = car1.transmission.ifBlank { "N/A" }
        binding.textViewCar2Transmission.text = car2.transmission.ifBlank { "N/A" }

        binding.textViewCar1Engine.text = car1.engineSize.ifBlank { "N/A" }
        binding.textViewCar2Engine.text = car2.engineSize.ifBlank { "N/A" }

        binding.textViewCar1Color.text = car1.color.ifBlank { "N/A" }
        binding.textViewCar2Color.text = car2.color.ifBlank { "N/A" }

        binding.textViewCar1Make.text = car1.make.ifBlank { "N/A" }
        binding.textViewCar2Make.text = car2.make.ifBlank { "N/A" }

        binding.textViewCar1Model.text = car1.model.ifBlank { "N/A" }
        binding.textViewCar2Model.text = car2.model.ifBlank { "N/A" }
    }

    private fun bindSpec(
        tv1: TextView, tv2: TextView,
        text1: String, text2: String,
        value1: Double, value2: Double,
        lowerIsBetter: Boolean
    ) {
        tv1.text = text1
        tv2.text = text2

        // Reset styles
        tv1.setTypeface(null, Typeface.NORMAL)
        tv2.setTypeface(null, Typeface.NORMAL)
        tv1.setTextColor(requireContext().getColor(R.color.md_theme_onSurface))
        tv2.setTextColor(requireContext().getColor(R.color.md_theme_onSurface))

        // Skip highlighting if both are zero (no data)
        if (value1 == 0.0 && value2 == 0.0) return
        if (value1 == value2) return

        val betterColor = requireContext().getColor(R.color.md_theme_primary)
        if (lowerIsBetter) {
            if (value1 < value2) {
                tv1.setTypeface(null, Typeface.BOLD)
                tv1.setTextColor(betterColor)
            } else {
                tv2.setTypeface(null, Typeface.BOLD)
                tv2.setTextColor(betterColor)
            }
        } else {
            if (value1 > value2) {
                tv1.setTypeface(null, Typeface.BOLD)
                tv1.setTextColor(betterColor)
            } else {
                tv2.setTypeface(null, Typeface.BOLD)
                tv2.setTextColor(betterColor)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "CompareFragment - onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "CompareFragment - onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "CompareFragment - onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "CompareFragment - onStop() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "CompareFragment - onDestroyView() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "CompareFragment - onDestroy() called")
    }
}
