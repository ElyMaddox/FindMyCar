package com.example.findmycar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.findmycar.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(javaClass.simpleName, "Start onCreate")
        super.onCreate(savedInstanceState)

        Log.d(javaClass.simpleName, "End onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(javaClass.simpleName, "Start onCreateView")

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        Log.d(javaClass.simpleName, "End onCreateView")
        return binding.root
    }

    override fun onStart() {
        Log.d(javaClass.simpleName, "Start onStart")
        super.onStart()

        Log.d(javaClass.simpleName, "End onStart")
    }

    override fun onResume() {
        Log.d(javaClass.simpleName, "Start onResume")
        super.onResume()

        Log.d(javaClass.simpleName, "End onResume")
    }

    override fun onPause() {
        Log.d(javaClass.simpleName, "Start onPause")
        super.onPause()

        Log.d(javaClass.simpleName, "End onPause")
    }

    override fun onStop() {
        Log.d(javaClass.simpleName, "Start onStop")
        super.onStop()

        Log.d(javaClass.simpleName, "End onStop")
    }

    override fun onDestroyView() {
        Log.d(javaClass.simpleName, "Start onDestroyView")
        super.onDestroyView()
        _binding = null

        Log.d(javaClass.simpleName, "End onDestroyView")
    }

    override fun onDestroy() {
        Log.d(javaClass.simpleName, "Start onDestroy")
        super.onDestroy()

        Log.d(javaClass.simpleName, "End onDestroy")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

}