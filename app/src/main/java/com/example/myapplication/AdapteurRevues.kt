package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import kotlinx.coroutines.NonDisposableHandle.parent


class AdapteurRevues(private val context: Context, private val dataList: MutableList<Revue>):BaseAdapter() {
    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
       return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(p0: Int, convertView: View?, parent: ViewGroup?): View {
        val currentItem = getItem(p0) as Revue;
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.une_revue,parent,false)



        val titre = itemView.findViewById<TextView>(R.id.texttitre)
        val revue = itemView.findViewById<ScrollView>(R.id.textViewcomms)
        val image =  itemView.findViewById<ImageView>(R.id.imagesoulier)



        return itemView

    }
}