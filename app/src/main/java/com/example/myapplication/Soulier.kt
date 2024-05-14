package com.example.myapplication

import android.media.Image
import java.io.Serializable

class Soulier (
    var revues:MutableList<Revue>?,
    val nom:String,
    var note:Float? =0f,
    var prix:Number,
    val image: Int

):Serializable{

    init {
        calculateNote()
    }

    private fun calculateNote() {
        if (revues?.isNotEmpty() == true) {
            var totalNote = 0f
            for (revue in revues!!) {
                totalNote += revue.note
            }
            note = totalNote / revues!!.size
        }
    }

}