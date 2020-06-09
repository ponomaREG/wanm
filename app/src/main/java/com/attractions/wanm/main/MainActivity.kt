package com.attractions.wanm.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.attractions.wanm.R
import com.attractions.wanm.fragments.list.ListAttView
import com.attractions.wanm.fragments.map.MapView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setOclsToMenuItems()

    }

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
}
