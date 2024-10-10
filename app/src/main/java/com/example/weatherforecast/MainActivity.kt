package com.example.weatherforecast

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.weatherforecast.data.SharePrefrenceData
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_NOTIFICATION_ACCESS = 123
    }
    private lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var binding: ActivityMainBinding
    lateinit var  navController:NavController
    lateinit var sharedPrefrenceData:SharePrefrenceData
    private lateinit var drawerLayout: DrawerLayout



    override fun onStart() {
        super.onStart()

        LocaleHelper.setdefault(sharedPrefrenceData,this)

    }

    override fun onResume() {
        super.onResume()
        LocaleHelper.setdefault(sharedPrefrenceData,this)
//        val savedLanguage = LocaleHelper.getSavedLanguage(this)
//        Log.i("MainActivity", "$savedLanguage")
//        LocaleHelper.setLocale(this, savedLanguage ?: "en")
    }
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sharedPrefrenceData=  SharePrefrenceData(this)

        LocaleHelper.setdefault(sharedPrefrenceData,this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        initNavHostFragment()
        initDrawerLayout()
        initDrawerLayoutMenu()

    }


    private fun initNavHostFragment() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }
    private fun initDrawerLayout() {
        drawerLayout = binding.drawerLayout
        drawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        val navigationIcon = ContextCompat.getDrawable(this, R.drawable.baseline_menu_24)?.mutate()
        navigationIcon?.setTint(getColor(R.color.white)) // Set the icon tint color
        binding.toolbar.navigationIcon = navigationIcon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(navigationIcon)
    }



    private fun initDrawerLayoutMenu() {
        val navigationView = findViewById<NavigationView>(R.id.nav)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.settingFragment -> {
                    navController.navigate(R.id.settingFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.favFragment -> {
                    navController.navigate(R.id.favFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.alarmFragment-> {
                    navController.navigate(R.id.alarmFragment)
                    drawerLayout.closeDrawers()
                    true
                }


                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}