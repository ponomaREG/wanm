package com.attractions.wanm.model_helper

import android.widget.ImageView
import com.attractions.wanm.R
import com.squareup.picasso.Picasso

class PhotoDownloadManager {

    companion object {
        fun loadImageByPicasso(urlPhoto:String,imageView: ImageView){
            Picasso.get()
                .load(urlPhoto)
                .placeholder(R.drawable.map_desc_placeholder)
                .into(imageView)
        }
    }

}