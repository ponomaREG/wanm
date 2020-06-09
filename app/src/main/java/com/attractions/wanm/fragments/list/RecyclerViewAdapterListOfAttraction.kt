package com.attractions.wanm.fragments.list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.attractions.wanm.R
import com.attractions.wanm.model_helper.Network
import com.attractions.wanm.model_helper.PhotoDownloadManager
import com.attractions.wanm.model_helper.pojo.Attraction
import com.attractions.wanm.presenter_helper.PresenterHelper
import kotlinx.android.synthetic.main.rv_item.view.*
import java.util.zip.Inflater

class RecyclerViewAdapterListOfAttraction(context: Context,private var attractions:List<Attraction>)
    :RecyclerView.Adapter<RecyclerViewAdapterListOfAttraction.ViewHolder>() {
    val latitude = 59.946290
    val longitude = 30.265529

    private var inflater:LayoutInflater? = null


    init {
        this.inflater = LayoutInflater.from(context)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater!!.inflate(R.layout.rv_item,parent,false))
    }

    override fun getItemCount(): Int {
        return attractions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentAttraction:Attraction? = getAttractionAt(position)

        //IMAGE LOADER
        PhotoDownloadManager.loadImageByPicasso(
            Network.transformIdOfAttractionToUrlOfPhotoAttraction(currentAttraction!!.id),
            holder.imageAttraction
        )


        holder.titleAttraction.text = currentAttraction.title
        holder.rangeAttraction.text = String.format(
            holder.rangeAttraction.text.toString(),
            PresenterHelper.calculateDistanceInKilometersDouble(
                currentAttraction.latitude,
                latitude,
                currentAttraction.longitude,
                longitude
            ))

        holder.itemView.setOnClickListener {
            //TODO: START DESC INFO
        }

    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val imageAttraction:ImageView = itemView.rv_item_image
        val titleAttraction:TextView = itemView.rv_item_title
        val rangeAttraction:TextView = itemView.rv_item_range
    }


    private fun getAttractionAt(position: Int):Attraction?{
        if((!attractions.isNullOrEmpty())
            &&(attractions.size>position)
            &&(position>=0))
        { return attractions[position]}
        else return null
    }


}