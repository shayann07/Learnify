package com.example.elearningplatform

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.elearningplatform.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // ↓ NavHost & controller
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        // Inflate the XML graph, then pick the true entry point
        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        graph.setStartDestination(          // <-- use the setter
            if (FirebaseAuth.getInstance().currentUser != null)
                R.id.homeFragment           // already signed‑in
            else
                R.id.loginFragment          // first‑time / signed‑out
        )
        navController.graph = graph

        // Hook the ActionBar up *after* the graph is assigned
        NavigationUI.setupActionBarWithNavController(this, navController)

        // Hide toolbar on Login / Register
        navController.addOnDestinationChangedListener { _, dest, _ ->
            binding.toolbar.visibility =
                if (dest.id == R.id.loginFragment || dest.id == R.id.registerFragment)
                    View.GONE
                else
                    View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHost.navController.navigateUp() || super.onSupportNavigateUp()
    }
}
