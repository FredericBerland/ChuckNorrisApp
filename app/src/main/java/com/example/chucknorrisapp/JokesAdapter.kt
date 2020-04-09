package com.example.chucknorrisapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class JokesAdapter() : RecyclerView.Adapter<JokesAdapter.JokesViewHolder>(){

    var jokes : List<Joke> = emptyList()
        set(vL){
            field = vL
            notifyDataSetChanged()
        }

    class JokesViewHolder(val jokeView: JokeView) : RecyclerView.ViewHolder(jokeView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokesViewHolder {
        val jokeView = LayoutInflater.from(parent.context).inflate(R.layout.my_text_view, parent, false) as JokeView
        return JokesViewHolder(jokeView)
    }

    override fun getItemCount() = jokes.size

    override fun onBindViewHolder(holder: JokesViewHolder, position: Int) {
        holder.jokeView.aTextView.text = jokes[position].toString()
        holder.jokeView.aStar.setOnClickListener(View.OnClickListener {
            it.setBackgroundResource(R.drawable.ic_star_black_24dp)
        })
        holder.jokeView.aShare.setOnClickListener(View.OnClickListener {
            Log.d("ID", jokes[position].id)
        })
    }
}