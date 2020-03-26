package com.example.chucknorrisapp

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.UnstableDefault
import io.reactivex.rxkotlin.subscribeBy

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    @UnstableDefault
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jS = JokeApiServiceFactory.returnService()
        val vSJ = jS.giveMeAJoke()
        vSJ.subscribeBy(onError = { joke -> TODO("Error on Joke") }, onSuccess = { joke -> TODO("Success on Joke") })

        viewManager = LinearLayoutManager(this)
        viewAdapter = JokesAdapter()

        recyclerView = findViewById<RecyclerView>(R.id.main_rv).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}