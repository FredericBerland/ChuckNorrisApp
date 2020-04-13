package com.example.chucknorrisapp

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class JokesAdapter(pFav : List<Joke>) : RecyclerView.Adapter<JokesAdapter.JokesViewHolder>(){

    var aFavList : List<Joke> = pFav
    var jokes : List<Joke> = aFavList
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    class JokesViewHolder(val jokeView: JokeView) : RecyclerView.ViewHolder(jokeView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokesViewHolder {
        val jokeView = JokeView(parent.context).inflate(R.layout.my_text_view)
        return JokesViewHolder(jokeView)
    }

    override fun getItemCount() = jokes.size

    override fun onBindViewHolder(holder: JokesViewHolder, position: Int) {
        lateinit var vM : JokeView.Model

        if(jokes[position].fav) {
            vM = JokeView.Model(jokes[position].toString(), true)
        }
        else {
            vM = JokeView.Model(jokes[position].toString(), false)
        }
        holder.jokeView.setupView(vM)

        holder.jokeView.aStar.setOnClickListener {
            if(jokes[position].fav) {
                vM = JokeView.Model(jokes[position].toString(), false)
                jokes[position].fav = false
                aFavList = aFavList.minus(jokes[position])
            }
            else {
                vM = JokeView.Model(jokes[position].toString(), true)
                jokes[position].fav = true
                aFavList = aFavList.plus(jokes[position])
            }
            holder.jokeView.setupView(vM)
        }
    }

    fun moveInList(from : Int, to : Int) : Boolean{
        val joke = jokes[from]
        jokes = jokes.minus(jokes[from])
        jokes = jokes.subList(0, to).plus(joke).plus(jokes.subList(to, jokes.size))
        return joke.equals(jokes[to])
    }

    fun deleteJoke(ind : Int){
        jokes = jokes.minus(jokes[ind])
        if(jokes[ind].fav)
            aFavList = aFavList.minus(jokes[ind])
    }
}