package com.example.findmycar.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.findmycar.R
import com.example.findmycar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(this.localClassName, "Start onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
