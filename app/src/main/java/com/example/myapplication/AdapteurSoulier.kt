package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter


class AdapteurSoulier(private val context: Context, private val dataList: MutableList<Revue>):BaseAdapter() {
    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(p0: Int, convertView: View?, p2: ViewGroup?): View {
        val currentItem = getItem(p0) as Soulier;
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.liste_revues,p2,false)
        TODO("Not yet implemented")
    }
}