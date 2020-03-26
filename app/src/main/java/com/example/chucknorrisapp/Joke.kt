package com.example.chucknorrisapp

import android.util.Log
import kotlinx.serialization.*
import java.lang.Exception

@Serializable
class Joke(val categories:List<String>,
           @SerialName("created_at")
           val createdAt:String,
           @SerialName("icon_url")
           val iconUrl:String,
           val id:String,
           @SerialName("updated_at")
           val updatedAt:String,
           val url:String,
           val value:String){


    override fun equals(other: Any?): Boolean {
        var vB = true
        val param : Joke
        try {
             param = other as Joke
        }
        catch (e : Exception) {
            Log.d("Exception", e.toString())
            return false
        }
        if(categories!=param.categories || createdAt!=param.createdAt || iconUrl!=param.iconUrl || id!=param.id || updatedAt!=param.updatedAt || url!=param.url || value!=param.value){
            vB=false
        }
        return vB
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String {
        return value
    }
}