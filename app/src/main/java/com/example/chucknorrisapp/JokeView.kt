package com.example.chucknorrisapp

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout


class JokeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    var aTextView : TextView = findViewById(R.id.my_tv)
    var aShare : ImageView = findViewById(R.id.my_share)
    var aStar : ImageView = findViewById(R.id.my_star)

    data class Model(val textView : TextView, val share : ImageView, val star : ImageView)

    fun setupView(model: Model){
        aTextView = model.textView
        aShare = model.share
        aStar = model.star
    }
}