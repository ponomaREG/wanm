package com.attractions.wanm.fragments.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.attractions.wanm.R
import com.attractions.wanm.fragments.descAttraction.BsvDescView
import com.attractions.wanm.main.CommunicationBetweenListAndMap
import com.attractions.wanm.main.MainActivity
import com.attractions.wanm.model_helper.Network
import com.attractions.wanm.model_helper.PhotoDownloadManager
import com.attractions.wanm.model_helper.pojo.Attraction
import com.attractions.wanm.presenter_helper.PresenterHelper
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.rv_item.view.*

class RecyclerViewAdapterListOfAttraction(
    context: Context,
    private var attractions:List<Attraction>,
    private var userLatLng: LatLng?
)
    :RecyclerView.Adapter<RecyclerViewAdapterListOfAttraction.ViewHolder>() {

    private var fragmentManager: FragmentManager
    private var view:CommunicationBetweenListAndMap? = null
    private var inflater:LayoutInflater? = null


    init {
        this.inflater = LayoutInflater.from(context)
        this.fragmentManager = (context as MainActivity).supportFragmentManager
        this.view = context
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater!!.inflate(R.layout.rv_item,parent,false))
    }

    override fun getItemCount(): Int {
        return attractions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentAttraction:Attraction? = getAttractionAt(position)

        PhotoDownloadManager.loadImageByPicasso(
            Network.transformIdOfAttractionToUrlOfPhotoAttraction(currentAttraction!!.id),
            holder.imageAttraction
        )


        holder.titleAttraction.text = currentAttraction.title

        if(userLatLng != null) {
            holder.rangeAttraction.text = String.format(
                holder.rangeAttraction.text.toString(),
                PresenterHelper.calculateDistanceInKilometersDouble(
                    currentAttraction.latitude,
                    userLatLng!!.latitude,
                    currentAttraction.longitude,
                    userLatLng!!.longitude
                )
            )

            holder.findOnMap.setOnClickListener {
                this.view!!.showOnMapLatLng(currentAttraction.latitude,currentAttraction.longitude)
            }
        }else{
            holder.findOnMap.visibility = View.GONE
            holder.rangeAttraction.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            BsvDescView.getInstance(currentAttraction.id).show(fragmentManager,"Description of attraction")
        }

    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val imageAttraction:ImageView = itemView.rv_item_image
        val titleAttraction:TextView = itemView.rv_item_title
        val rangeAttraction:TextView = itemView.rv_item_range
        val findOnMap:ImageView = itemView.rv_item_findOnMap
    }


    private fun getAttractionAt(position: Int):Attraction?{
        if((!attractions.isNullOrEmpty())
            &&(attractions.size>position)
            &&(position>=0))
        { return attractions[position]}
        else return null
    }


}