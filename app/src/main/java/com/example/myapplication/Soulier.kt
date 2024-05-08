package com.example.myapplication

import android.media.Image

class Soulier (
    var revues:List<Revue>,
    val nom:String,
    var prix:Number,
    var note:Int,
    val image: Int

){
    fun somme(): Int {
        var somme:Int =0
        var nb_rev:Int =0
        for (revue in revues){
            somme+= revue.note
            nb_rev++
        }
        return somme / nb_rev
    }


}