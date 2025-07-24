package com.example.elearningplatform

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
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

        /* ── NavHost / controller ─────────────────────────────────────────── */
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        /* Build the graph only on the first launch (not on rotations) */
        if (savedInstanceState == null) {
            val graph = navController.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(
                if (FirebaseAuth.getInstance().currentUser != null)
                    R.id.homeFragment
                else
                    R.id.loginFragment
            )
            navController.graph = graph
        }

        /* Top-level destinations (no “up” arrow) */
        val appBarConfig = AppBarConfiguration(
            setOf(R.id.homeFragment)      // add more top-level IDs if needed
        )

        /* Wire ActionBar <-> NavController with that config */
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig)

        /* Hide toolbar on Login / Register / Player screens */
        navController.addOnDestinationChangedListener { _, dest, _ ->
            binding.toolbar.visibility =
                if (dest.id == R.id.loginFragment ||
                    dest.id == R.id.registerFragment ||
                    dest.id == R.id.playerFragment)
                    View.GONE
                else
                    View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHost.navController.navigateUp() || super.onSupportNavigateUp()
    }
}
