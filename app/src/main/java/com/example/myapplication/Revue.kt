package com.example.myapplication

import android.media.Image

data class Revue(
    var titre:String,
    var commentaire:String,
    val utilisateur:Utilisateur,
    var note:Int,
    val image: Image?
)