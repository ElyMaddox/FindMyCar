package com.example.findmycar.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.findmycar.R
import com.example.findmycar.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(javaClass.simpleName, "Start onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(javaClass.simpleName, "Start onCreateView")
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
    }

    override fun onStart() {
        super.onStart()
        Log.d(javaClass.simpleName, "Start onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(javaClass.simpleName, "Start onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(javaClass.simpleName, "Start onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(javaClass.simpleName, "Start onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(javaClass.simpleName, "Start onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(javaClass.simpleName, "Start onDestroy")
    }
}
