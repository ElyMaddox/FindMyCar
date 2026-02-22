package com.example.findmycar

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.util.Log
import com.example.findmycar.databinding.ActivityMainBinding

class TemplateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(this.localClassName, "Start onCreate")
        super.onCreate(savedInstanceState)

        Log.d(this.localClassName, "End onCreate")
    }

    override fun onStart() {
        Log.d(this.localClassName, "Start onStart")
        super.onStart()

        Log.d(this.localClassName, "End onStart")
    }

    override fun onResume() {
        Log.d(this.localClassName, "Start onResume")
        super.onResume()

        Log.d(this.localClassName, "End onResume")
    }

    override fun onPause() {
        Log.d(this.localClassName, "Start onPause")
        super.onPause()

        Log.d(this.localClassName, "End onPause")
    }

    override fun onStop() {
        Log.d(this.localClassName, "Start onStop")
        super.onStop()

        Log.d(this.localClassName, "End onStop")
    }

    override fun onDestroy() {
        Log.d(this.localClassName, "Start onDestroy")
        super.onDestroy()

        Log.d(this.localClassName, "End onDestroy")
    }

}