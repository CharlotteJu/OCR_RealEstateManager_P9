package com.openclassrooms.realestatemanager.views.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.notifications.NotificationWorker
import com.openclassrooms.realestatemanager.utils.*
import com.openclassrooms.realestatemanager.views.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mToolbar: Toolbar
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mNavigationView: NavigationView
    private lateinit var mNavigationController: NavController
    private lateinit var mView: View
    private var mDetailFragment : DetailFragment? = null
    private var isTablet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.mView = window.decorView.rootView
        this.configureToolbar()
        this.configureNavigationView()
        this.configureNavigationController()
        this.configureTabMode()

        if (isTablet) showDetailFragment()
    }

    private fun configureToolbar()
    {
        mToolbar = findViewById(R.id.main_activity_toolbar)
        setSupportActionBar(mToolbar)
    }

    private fun configureNavigationView()
    {
        mDrawerLayout = findViewById(R.id.main_activity_drawer_layout)
        val actionBarToogle = ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close)
        mDrawerLayout.addDrawerListener(actionBarToogle)
        actionBarToogle.syncState()
        mNavigationView = findViewById(R.id.main_activity_navigation_view)
        mNavigationView.setNavigationItemSelectedListener(this)
    }

    private fun configureNavigationController()
    {
        mNavigationController = findNavController(R.id.main_activity_navHost)
    }

    private fun configureTabMode()
    {
        //isTablet = findViewById<View>(R.id.tabMode_detail_frame_layout) != null
        this.isTablet = resources.getBoolean(R.bool.isTablet)
        mNavigationController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id)
            {
                R.id.listFragment -> showDetailFragment()
                R.id.mapFragment -> showDetailFragment()
                R.id.addEstateAgentFragment -> hideDetailFragment()
                R.id.addHousingFragment -> hideDetailFragment()
                R.id.editHousingFragment -> hideDetailFragment()
                R.id.settingsFragment -> hideDetailFragment()
            }
        }
    }

    fun getDetailFragment() : DetailFragment?
    {
        return mDetailFragment
    }

    private fun showDetailFragment()
    {
        if (isTablet)
        {
            if (findViewById<View>(R.id.tabMode_detail_frame_layout) != null)
            {
                mDetailFragment = DetailFragment()
                val param = mView.tabMode_detail_frame_layout.layoutParams as LinearLayout.LayoutParams
                param.weight = 50f
                supportFragmentManager.beginTransaction().replace(R.id.tabMode_detail_frame_layout, mDetailFragment!!).commit()
                mView.tabMode_detail_frame_layout.layoutParams = param

            }
        }

    }

    private fun hideDetailFragment()
    {
        if (isTablet)
        {
            if (supportFragmentManager.findFragmentById(R.id.listFragment) == null && supportFragmentManager.findFragmentById(R.id.map) == null && mDetailFragment != null)
            {
                //supportFragmentManager.beginTransaction().detach(mDetailFragment!!)
                if (mView.tabMode_detail_frame_layout != null)
                {
                    val param = mView.tabMode_detail_frame_layout.layoutParams as LinearLayout.LayoutParams
                    param.weight = 0f
                    mView.tabMode_detail_frame_layout.layoutParams = param
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.menu_drawer_home -> {
                this.mNavigationController.navigate(R.id.listFragment)
            }
            R.id.menu_drawer_settings -> {
                this.mNavigationController.navigate(R.id.settingsFragment)
            }
            R.id.menu_drawer_add_housing -> {
                val bundle = Bundle()
                bundle.putString(BUNDLE_REFERENCE, UUID.randomUUID().toString())
                this.mNavigationController.navigate(R.id.addHousingFragment, bundle)
            }
            R.id.menu_drawer_add_estate_agent -> {
                this.mNavigationController.navigate(R.id.addEstateAgentFragment)
            }
        }
        // To Close drawerLayout auto
        this.mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId)
        {
            R.id.menu_toolbar_filter -> this.mNavigationController.navigate(R.id.filterDialogFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onBackPressed() {
        if(this.mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            this.mDrawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()
        return true
    }
}