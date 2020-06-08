package com.attractions.wanm.fragments.descAttraction

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.attractions.wanm.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BsvDescView:BottomSheetDialogFragment(),Interface.View {

    private val presenter:Interface.Presenter = BsvDescPresenter(this)

    companion object{
        private var instance:BottomSheetDialogFragment? = null

        fun getInstance(idAttraction:Int):BottomSheetDialogFragment{
            if(instance == null) instance = BsvDescView()
            val args = Bundle()
            args.putInt("idAttraction",idAttraction)
            instance!!.arguments = args
            return instance as BottomSheetDialogFragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ON VIEW CREATE","!")
        return inflater.inflate(R.layout.fragment_desc_attraction,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args:Bundle? = arguments
        if(args != null){
            val idAttraction = args.getInt("idAttraction")
            this.presenter.requestData(idAttraction)
        }//TODO:DESTROY???????


    }

    override fun setTitle(title: String) {
        val view:View? = view
        if(view != null){
            view.findViewById<TextView>(R.id.fragment_desc_title).text = title
        }
    }

    override fun setDesc(desc: String) {
        val view:View? = view
        if(view != null){
            view.findViewById<TextView>(R.id.fragment_desc_desc).text = desc
        }
    }

    override fun setImage(bitmap: Bitmap) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setImageError() {
        //TODO:IMAGE ERROR
    }

    override fun setTitleError() {
        setTitle("Упс(")
    }

    override fun setDescError() {
        setDesc("Произошла ошибка при подлючении к серверу")
    }


    override fun showToastError() {
        //TODO:ERROR TOAST ?????????
    }


}