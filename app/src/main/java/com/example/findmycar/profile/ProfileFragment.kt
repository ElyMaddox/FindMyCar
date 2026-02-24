package com.example.findmycar.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.findmycar.R
import com.example.findmycar.databinding.FragmentProfileBinding
import com.example.findmycar.login.LoginViewModel
import kotlin.getValue

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

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

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        Log.d(javaClass.simpleName, "End onCreateView")
        return binding.root
    }

    override fun onDestroyView() {
        Log.d(javaClass.simpleName, "Start onDestroyView")
        super.onDestroyView()
        _binding = null

        Log.d(javaClass.simpleName, "End onDestroyView")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(javaClass.simpleName, "Start onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSaveProfile.setOnClickListener {
            viewModel.saveProfile(binding.editTextFullName.text)
        }

        binding.buttonBackToWelcome.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        Log.d(javaClass.simpleName, "End onViewCreated")
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

    override fun onDestroy() {
        Log.d(javaClass.simpleName, "Start onDestroy")
        super.onDestroy()

        Log.d(javaClass.simpleName, "End onDestroy")
    }
}
