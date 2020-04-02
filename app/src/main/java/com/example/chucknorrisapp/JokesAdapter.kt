package com.example.chucknorrisapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JokesAdapter : RecyclerView.Adapter<JokesAdapter.JokesViewHolder>(){

    var jokes : List<Joke> = List(1){Joke(emptyList(), "", "", "", "", "","coucou")}

    class JokesViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokesViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.my_text_view, parent, false) as TextView
        return JokesViewHolder(textView)
    }

    override fun getItemCount() = jokes.size

    override fun onBindViewHolder(holder: JokesViewHolder, position: Int) {
        holder.textView.text = jokes[position].toString()
    }

    fun setter(list: List<Joke>) { jokes=list }
}