package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.RatingBar
import android.widget.TextView
import androidx.compose.runtime.currentRecomposeScope


class AdapteurSoulier(private val context: Context, private val dataList: MutableList<Soulier>):BaseAdapter() {
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
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.un_pare,p2,false)

        val nom = itemView.findViewById<TextView>(R.id.textViewnom)
        val image = itemView.findViewById<ImageView>(R.id.imageViewsoulier)
        val rating = itemView.findViewById<RatingBar>(R.id.ratingBar2)
        val prix = itemView.findViewById<TextView>(R.id.textViewprix)



        nom.text = currentItem.nom
        image.setImageResource(currentItem.image)
        rating.numStars = currentItem.note
        prix.text = currentItem.prix.toString()


       return itemView
    }
}