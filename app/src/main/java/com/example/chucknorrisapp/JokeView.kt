package com.example.chucknorrisapp


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout


class JokeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var aTextView : TextView
    lateinit var aShare : ImageView
    lateinit var aStar : ImageView

    data class Model(val pS : String, val pFavorite : Boolean)

    fun setupView(model: Model){
        aTextView.text = model.pS
        if(model.pFavorite)
            aStar.setImageResource(R.drawable.ic_star_black_24dp)
        else
            aStar.setImageResource(R.drawable.ic_star_border_black_24dp)
    }

    fun inflate(layout : Int) : JokeView{
        View.inflate(context, layout, this)
        aTextView =findViewById(R.id.my_tv)
        aShare =findViewById(R.id.my_share)
        aStar =findViewById(R.id.my_star)
        return this
    }

}