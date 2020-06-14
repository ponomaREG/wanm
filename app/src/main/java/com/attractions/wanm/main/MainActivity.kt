package com.attractions.wanm.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.attractions.wanm.R
import com.attractions.wanm.fragments.list.ListAttView
import com.attractions.wanm.fragments.map.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),CommunicationBetweenListAndMap {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ON CREATE","!")
        setContentView(R.layout.activity_main)
        setOclsToMenuItems()
        getIncomeIntentIfExists()
    }

    private fun getIncomeIntentIfExists(){
        val bundle = intent.extras
        if(bundle != null){
            val id_attr = bundle.getInt("id",-1)
            if (id_attr != -1){
                val fragment = MapView.getInstance(id_attr)
                removeAllFragments()
                insertFragment(fragment)
            }
        }
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
        Log.e("inwert fragment","1")
        fragmentManager.beginTransaction()
            .replace(R.id.main_frame,fragment)
            .commitNow()


    }

    private fun removeAllFragments(){
        supportFragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun showOnMapLatLng(latitude:Double,longitude:Double) {
        insertFragment(MapView.getInstance(latitude,longitude))
    }


}

interface CommunicationBetweenListAndMap{
    fun showOnMapLatLng(latitude:Double,longitude:Double)
}
