package com.example.chucknorrisapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var aVA : JokesAdapter
    private val cD = CompositeDisposable()
    private lateinit var aFav : SharedPreferences
    private lateinit var swip: SwipeRefreshLayout

    @SuppressLint("ResourceAsColor")
    @UnstableDefault
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aFav = getSharedPreferences("Favorite Joke", Context.MODE_PRIVATE)
        var vFavList: List<Joke> = emptyList()
        aFav.all.forEach{vFavList = vFavList.plus( Json(JsonConfiguration.Stable).parse(Joke.serializer(), it.value.toString())) ; Log.d("ADD", it.value.toString()) }
        aFav.edit().apply { clear() }.apply()

        viewManager = LinearLayoutManager(this)
        val viewAdapter = JokesAdapter(vFavList)
        aVA = viewAdapter

        swip = findViewById(R.id.main_swip)
        swip.setProgressBackgroundColorSchemeResource(R.color.blanc)
        swip.setColorSchemeResources(R.color.rose)

        recyclerView = findViewById<RecyclerView>(R.id.main_rv).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((viewManager as LinearLayoutManager).findLastVisibleItemPosition() == viewAdapter.itemCount - 1 || (viewManager as LinearLayoutManager).findLastVisibleItemPosition() == -1)
                    add10Joke(viewAdapter)
                Log.d("SCROLL", "SCROLL")
            }
        })

        val callback = JokeTouchHelper({ m -> viewAdapter.deleteJoke(m) },
            { m, n -> viewAdapter.moveInList(m, n) })
        callback.attachToRecyclerView(recyclerView)

        add10Joke(viewAdapter)
    }

    @UnstableDefault
    fun add10Joke( viewAdapter : JokesAdapter){
        val vR = JokeApiServiceFactory.returnService().giveMeAJoke().repeat(10)
            .delay(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { swip.isRefreshing = true }
            .doAfterTerminate { swip.isRefreshing = false }
            .subscribeBy(
                onError = { Log.d("Error :", "$it") },
                onNext = { viewAdapter.jokes = viewAdapter.jokes.plus(it) },
                onComplete = { })
        cD.add(vR)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle){
        super.onSaveInstanceState(savedInstanceState)
        var i = 0
        savedInstanceState.putInt("Size", aVA.jokes.size)
        aVA.jokes.forEach {
            if (!it.fav) {
                savedInstanceState.putString(
                    "Joke$i",
                    Json(JsonConfiguration.Stable).stringify(Joke.serializer(), it)
                )
            }
            i++
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        var vList = emptyList<Joke>()
        val n = savedInstanceState.getInt("Size")
        lateinit var vS : String
        for(i in 0 until n-1){
            try{
                vS = savedInstanceState.getString("Joke$i")!!
                vList = vList.plus(Json(JsonConfiguration.Stable).parse(Joke.serializer(), vS))
            } catch (e : Exception){}
        }
        aVA.jokes = aVA.jokes.plus(vList)
    }

    fun shareClicked(view : View){
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, aVA.jokes[recyclerView.findContainingViewHolder(view)!!.adapterPosition].toString())
        }, "Partager")
        startActivity(share)
    }

    override fun onStop() {
        super.onStop()
        aFav.edit().apply { aVA.aFavList.forEach {putString("Joke${aVA.jokes.indexOf(it)}", Json(JsonConfiguration.Stable).stringify(Joke.serializer(), it))} }.apply()
    }
}