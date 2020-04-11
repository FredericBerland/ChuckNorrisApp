package com.example.chucknorrisapp

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class JokesAdapter() : RecyclerView.Adapter<JokesAdapter.JokesViewHolder>(){

    var jokes : List<Joke> = emptyList()
        set(vL){
            field = vL
            notifyDataSetChanged()
        }

    class JokesViewHolder(val jokeView: JokeView) : RecyclerView.ViewHolder(jokeView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokesViewHolder {
        val jokeView = JokeView(parent.context).inflate(R.layout.my_text_view)
        return JokesViewHolder(jokeView)
    }

    override fun getItemCount() = jokes.size

    override fun onBindViewHolder(holder: JokesViewHolder, position: Int) {
        var vM = JokeView.Model(jokes[position].toString(), false)
        holder.jokeView.setupView(vM)

        holder.jokeView.aStar.setOnClickListener {
            if (holder.jokeView.aBool)
                vM = JokeView.Model(jokes[position].toString(), false)
            else
                vM = JokeView.Model(jokes[position].toString(), true)
            holder.jokeView.setupView(vM)
        }
    }

    fun moveInList(from : Int, to : Int) : Boolean{
        val joke = jokes[from]
        jokes = jokes.subList(0, from).plus(jokes.subList(from+1, jokes.size))
        jokes = jokes.subList(0, to).plus(joke).plus(jokes.subList(to+1, jokes.size))
        Log.d("Liste", jokes.toString())
        return joke.equals(jokes[to])
    }

    fun deleteJoke(ind : Int){
        jokes = jokes.subList(0, ind).plus(jokes.subList(ind+1, jokes.size))
        Log.d("Liste", jokes.toString())
    }
}