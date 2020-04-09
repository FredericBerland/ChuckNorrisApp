package com.example.chucknorrisapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.serialization.UnstableDefault
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.serializer
import java.lang.Exception
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var aListeOfJokes: List<Joke>
    private lateinit var aVA : JokesAdapter
    private lateinit var vPB : ProgressBar
    private val cD = CompositeDisposable()

    @UnstableDefault
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        val viewAdapter = JokesAdapter()
        aVA = viewAdapter

        vPB = findViewById(R.id.main_pb)
        recyclerView = findViewById<RecyclerView>(R.id.main_rv).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        add10Joke(viewAdapter)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                    add10Joke(viewAdapter)
            }
        })
    }

    @UnstableDefault
    fun add10Joke( viewAdapter : JokesAdapter){
        if( (viewManager as LinearLayoutManager).findLastVisibleItemPosition() == viewAdapter.itemCount-1) {
            val vR = JokeApiServiceFactory.returnService().giveMeAJoke().repeat(10)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { vPB.visibility = View.VISIBLE }
                .doAfterTerminate { vPB.visibility = View.INVISIBLE }
                .subscribeBy(
                    onError = { Log.d("Error :", "$it") },
                    onNext = { viewAdapter.jokes = viewAdapter.jokes.plus(it) },
                    onComplete = { aListeOfJokes = viewAdapter.jokes })
            cD.add(vR)

        }
    }


    override fun onSaveInstanceState(savedInstanceState: Bundle){
        super.onSaveInstanceState(savedInstanceState)
        var i = 0
        savedInstanceState.putInt("Size", aListeOfJokes.size)
        aListeOfJokes.forEach {
            savedInstanceState.putString("Joke$i", Json(JsonConfiguration.Stable).stringify(Joke.serializer(), it))
            i++
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        try {
            var vList = emptyList<Joke>()
            val n = savedInstanceState.getInt("Size")
            lateinit var vS : String

            for(i in 0 until n-1){
                try {
                    //recuperation de la string sérialiser
                    vS = savedInstanceState.getString("Joke$i")!!
                    //recuperation de la joke depuis le string sérialiser
                    vList = vList.plus(Json(JsonConfiguration.Stable).parse(Joke.serializer(), vS))
                }
                catch (e : Exception){
                    Log.d("ERROR2", e.toString())
                }
            }

            //ajout des jokes + notify
            aVA.jokes = aVA.jokes.plus(vList)
        }
        catch (e : Exception){
            Log.d("ERROR", e.toString())
        }
    }
}