package com.openclassrooms.realestatemanager.views.activities

import android.os.Bundle
import android.view.Menu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.views.fragments.DetailFragment
import com.openclassrooms.realestatemanager.views.fragments.ListFragmentDirections

class MainActivity : AppCompatActivity() {


    private lateinit var detailFragment: DetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.main_activity_toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.main_activity_drawer_layout)
        val navView: NavigationView = findViewById(R.id.main_activity_navigation_view)
        val navController = findNavController(R.id.main_activity_navHost)

        val fab: FloatingActionButton = findViewById(R.id.main_activity_test_fab)
        fab.setOnClickListener { view ->
            val action = ListFragmentDirections.actionListFragmentToDetailFragment()
            navController.navigate(action)
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       // appBarConfiguration = AppBarConfiguration(setOf( R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)

        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_drawer_menu, menu)
        return true
    }


}