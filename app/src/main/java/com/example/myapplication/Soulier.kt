package com.example.myapplication

import android.media.Image
import java.io.Serializable

class Soulier (
    val nom:String,
    var prix:Number,
    var image: String?,
    var revues:MutableList<Revue>? = mutableListOf(),
    var note:Float? =0f,

    ):Serializable{





}