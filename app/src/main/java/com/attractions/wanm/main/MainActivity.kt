package com.attractions.wanm.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.attractions.wanm.R
import com.attractions.wanm.fragments.list.ListAttView
import com.attractions.wanm.fragments.map.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),CommunicationBetweenListAndMap {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOclsToMenuItems()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.settings,menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.menu_settings_geolocation ->{
//
//            }
//        }
//        return super.onOptionsItemSelected(item)
//}

    private fun setOclsToMenuItems(){
        val bnv: BottomNavigationView = findViewById(R.id.main_bnv)
        bnv.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.bnv_map ->{
                    //INSERT FRAGMENT MAP
                    val fragment:Fragment = MapView.getInstance()
                    insertFragment(fragment)
                    true
                }

                R.id.bnv_listAttraction ->{
                    val fragment = ListAttView.getInstance()
                    insertFragment(fragment)
                    true
                }
                else -> false
            }
        }
    }


    private fun insertFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.main_frame,fragment)
            .commitNow()
    }

    override fun showOnMapLatLng(latitude:Double,longitude:Double) {
        insertFragment(MapView.getInstance(latitude,longitude))
    }


}

interface CommunicationBetweenListAndMap{
    fun showOnMapLatLng(latitude:Double,longitude:Double)
}
