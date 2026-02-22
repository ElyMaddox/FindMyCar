package com.example.findmycar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

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