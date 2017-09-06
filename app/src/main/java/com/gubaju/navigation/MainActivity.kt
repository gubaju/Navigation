package com.gubaju.navigation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.gubaju.navigationdrawer.TopNavigationDrawer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var nav: TopNavigationDrawer<NavModel>? = null
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                nav?.toggle()
                return super.onOptionsItemSelected(item)
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(activityMainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Navigation Drawer
        nav = findViewById(R.id.activityMainTopNavigation) as TopNavigationDrawer<NavModel>
        nav!!.adapter = NavAdapter(this, getNavModels())
        nav!!.setToolbar(activityMainToolbar)
        nav!!.build()
    }

    fun getNavModels(): List<NavModel> {
        val item = ArrayList<NavModel>()
        item.add(NavModel("Flower", R.drawable.ic_flower))
        item.add(NavModel("Directions", R.drawable.ic_direction))
        item.add(NavModel("Car", R.drawable.ic_car))
        item.add(NavModel("Child", R.drawable.ic_child))
        item.add(NavModel("Flight", R.drawable.ic_flight))
        item.add(NavModel("Tram", R.drawable.ic_tram))
        return item
    }

}