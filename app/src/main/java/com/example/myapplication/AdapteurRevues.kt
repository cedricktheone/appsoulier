package com.example.myapplication

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

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

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("Not yet implemented")
    }
}