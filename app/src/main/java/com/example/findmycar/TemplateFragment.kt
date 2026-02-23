package com.example.findmycar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.findmycar.databinding.FragmentFirstBinding

class TemplateFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(this::class.java.simpleName, "Start onCreate")
        super.onCreate(savedInstanceState)

        Log.d(this::class.java.simpleName, "End onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(this::class.java.simpleName, "Start onCreateView")

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        Log.d(this::class.java.simpleName, "End onCreateView")
        return binding.root
    }

    override fun onStart() {
        Log.d(this::class.java.simpleName, "Start onStart")
        super.onStart()

        Log.d(this::class.java.simpleName, "End onStart")
    }

    override fun onResume() {
        Log.d(this::class.java.simpleName, "Start onResume")
        super.onResume()

        Log.d(this::class.java.simpleName, "End onResume")
    }

    override fun onPause() {
        Log.d(this::class.java.simpleName, "Start onPause")
        super.onPause()

        Log.d(this::class.java.simpleName, "End onPause")
    }

    override fun onStop() {
        Log.d(this::class.java.simpleName, "Start onStop")
        super.onStop()

        Log.d(this::class.java.simpleName, "End onStop")
    }

    override fun onDestroyView() {
        Log.d(this::class.java.simpleName, "Start onDestroyView")
        super.onDestroyView()
        _binding = null

        Log.d(this::class.java.simpleName, "End onDestroyView")
    }

    override fun onDestroy() {
        Log.d(this::class.java.simpleName, "Start onDestroy")
        super.onDestroy()

        Log.d(this::class.java.simpleName, "End onDestroy")
    }
}