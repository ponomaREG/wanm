package com.attractions.wanm.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attractions.wanm.R
import com.attractions.wanm.model_helper.pojo.Attraction
import com.attractions.wanm.model_helper.pojo.ResponseAttraction

class ListAttView: Fragment(),Interface.View {

    private val presenter:Interface.Presenter = ListAttPresenter(this)


    companion object{
        private var instance:ListAttView? = null
        fun getInstance():ListAttView{
            if(instance == null) instance = ListAttView()
            return  instance as ListAttView
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_view,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val latitude = 59.946290
        val longitude = 30.265529
        this.presenter.requestAttractions(latitude,longitude)

    }

    override fun setDataIntoRecyclerView(attractions: List<Attraction>) {
        val view = view ?: return

        val adapter = RecyclerViewAdapterListOfAttraction(context!!,attractions)

        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_list_rv)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun showToastError() {
        Toast.makeText(context,"Произошла ошибка",Toast.LENGTH_SHORT).show()
    }

    override fun hideMainProgressBar() {
        val view = view?: return
        view.findViewById<ProgressBar>(R.id.fragment_list_progressBar).visibility= View.GONE
    }


}