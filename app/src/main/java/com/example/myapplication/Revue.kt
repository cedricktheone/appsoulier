package com.example.myapplication

import java.io.Serializable
import java.util.concurrent.atomic.AtomicInteger


private val count: AtomicInteger = AtomicInteger(0)

data class Revue(

    var titre:String,
    var commentaire:String,
    val utilisateur:String,
    var note: Float,
    var image: String?
):Serializable{
    val Id : Int = count.incrementAndGet();

}