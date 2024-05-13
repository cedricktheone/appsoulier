package com.example.myapplication

import android.media.Image

class Soulier (
    var revues:MutableList<Revue>?,
    val nom:String,
    var prix:Number,
    val image: Int

){
    var note:Float? =0f
    init {
        calculateNote()
    }

    private fun calculateNote() {
        if (revues.isNotEmpty()) {
            var totalNote = 0f
            for (revue in revues) {
                totalNote += revue.note
            }
            note = totalNote / revues.size
        }
    }

}