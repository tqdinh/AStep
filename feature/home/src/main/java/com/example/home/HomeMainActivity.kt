package com.example.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeMainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        val bundle = intent.extras
        bundle?.apply {
            val journeyId = getString("journey_id")
            val bundle = bundleOf("journey_id" to journeyId)

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_home_host) as NavHostFragment
            navHostFragment.navController.setGraph(R.navigation.graph_home, bundle)

        }
    }
}