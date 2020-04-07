package com.example.chucknorrisapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.serialization.UnstableDefault
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var vBut : Button
    private val cD = CompositeDisposable()

    @UnstableDefault
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        val viewAdapter = JokesAdapter()


        vBut = findViewById(R.id.main_but)
        vBut.setOnClickListener { addAJoke(viewAdapter) }

        recyclerView = findViewById<RecyclerView>(R.id.main_rv).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        Log.d("Adapter", viewAdapter.jokes.toString())
    }

    @UnstableDefault
    fun addAJoke( viewAdapter : JokesAdapter){
        val jS = JokeApiServiceFactory.returnService().giveMeAJoke()
        val vR = jS
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {Log.d("Error :", "$it")}, onSuccess = { viewAdapter.jokes = viewAdapter.jokes.plus(it) })
        cD.add(vR)
    }
}