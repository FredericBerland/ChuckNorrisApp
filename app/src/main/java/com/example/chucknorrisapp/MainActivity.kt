package com.example.chucknorrisapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.serialization.UnstableDefault
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var vBut : Button
    private lateinit var vPB : ProgressBar
    private val cD = CompositeDisposable()

    @UnstableDefault
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        val viewAdapter = JokesAdapter()

        vPB = findViewById(R.id.main_pb)
        vBut = findViewById(R.id.main_but)
        vBut.setOnClickListener {
            add10Joke(viewAdapter)
        }

        recyclerView = findViewById<RecyclerView>(R.id.main_rv).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    @UnstableDefault
    fun addAJoke( viewAdapter : JokesAdapter){
        val jS = JokeApiServiceFactory.returnService().giveMeAJoke()
        val vR = jS
            .delay(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { vPB.visibility = View.VISIBLE }
            .doAfterSuccess { vPB.visibility = View.INVISIBLE }
            .subscribeBy(onError = {Log.d("Error :", "$it")}, onSuccess = { viewAdapter.jokes = viewAdapter.jokes.plus(it) })
        cD.add(vR)
    }

    @UnstableDefault
    fun add10Joke( viewAdapter : JokesAdapter){
        val vR = JokeApiServiceFactory.returnService().giveMeAJoke().repeat(10)
            .delay(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { vPB.visibility = View.VISIBLE }
            .doAfterTerminate { vPB.visibility = View.INVISIBLE }
            .subscribeBy(onError = {Log.d("Error :", "$it")}, onNext = { viewAdapter.jokes = viewAdapter.jokes.plus(it) }, onComplete = {  })
        cD.add(vR)
    }
}