package com.openclassrooms.realestatemanager.views.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
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
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.views.fragments.DetailFragment
import com.openclassrooms.realestatemanager.views.fragments.ListFragmentDirections

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.configureToolbar()
        this.configureNavigationView()

        navigationController = findNavController(R.id.main_activity_navHost)
        val fab: FloatingActionButton = findViewById(R.id.main_activity_test_fab)
        fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddHousingFragment()
            navigationController.navigate(action)
        }
    }

    private fun configureToolbar()
    {
        toolbar = findViewById(R.id.main_activity_toolbar)
        setSupportActionBar(toolbar)
    }

    private fun configureNavigationView()
    {
        drawerLayout = findViewById(R.id.main_activity_drawer_layout)
        val actionBarToogle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(actionBarToogle)
        actionBarToogle.syncState()
        navigationView = findViewById(R.id.main_activity_navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        var itemId = item.itemId
        when (itemId)
        {
            R.id.menu_drawer_parameters -> this.navigationController.navigate(ListFragmentDirections.actionListFragmentToParameterFragment())
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.navigation_drawer_menu, menu)
        return true
    }

    override fun onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
            super.onBackPressed()
        }
    }
}