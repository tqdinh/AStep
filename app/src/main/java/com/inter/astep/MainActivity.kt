package com.inter.astep

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    val permissions = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_frag) as NavHostFragment
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val navController  =navHostFragment.navController
        bottomNavigationView.setupWithNavController(navHostFragment.navController)
        checkNotificationPermission()



    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (true == it) {

            } else {
//                Intent(
//                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                    Uri.fromParts("package", packageName, null)
//                )?.also {
//                    startActivity(it)
//                }

            }
        }

    fun checkNotificationPermission() {
        val permissionsNeed = permissions.any {
            PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                applicationContext,
                it
            )
        }
        if (permissionsNeed) {

            permissions.forEach {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity,
                        it
                    )
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please allow pushing notification",
                        LENGTH_SHORT
                    )
                        .show()
                }
                requestPermissionLauncher.launch(it)

            }

        }


    }
}