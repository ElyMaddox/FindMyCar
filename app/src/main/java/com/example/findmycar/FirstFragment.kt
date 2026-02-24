package com.example.findmycar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.findmycar.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("FirstFragment", "Start onCreate")
        super.onCreate(savedInstanceState)

        Log.d("FirstFragment", "End onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("FirstFragment", "Start onCreateView")

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        Log.d("FirstFragment", "End onCreateView")
        return binding.root
    }

    override fun onStart() {
        Log.d("FirstFragment", "Start onStart")
        super.onStart()

        Log.d("FirstFragment", "End onStart")
    }

    override fun onResume() {
        Log.d("FirstFragment", "Start onResume")
        super.onResume()

        Log.d("FirstFragment", "End onResume")
    }

    override fun onPause() {
        Log.d("FirstFragment", "Start onPause")
        super.onPause()

        Log.d("FirstFragment", "End onPause")
    }

    override fun onStop() {
        Log.d("FirstFragment", "Start onStop")
        super.onStop()

        Log.d("FirstFragment", "End onStop")
    }

    override fun onDestroyView() {
        Log.d("FirstFragment", "Start onDestroyView")
        super.onDestroyView()
        _binding = null

        Log.d("FirstFragment", "End onDestroyView")
    }

    override fun onDestroy() {
        Log.d("FirstFragment", "Start onDestroy")
        super.onDestroy()

        Log.d("FirstFragment", "End onDestroy")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}
