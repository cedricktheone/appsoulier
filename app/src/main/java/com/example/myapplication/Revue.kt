package com.example.myapplication

import android.media.Image

data class Revue(
    var titre:String,
    var commentaire:String,
    val utilisateur:String,
    var note: Int,
    val image: Int?
)