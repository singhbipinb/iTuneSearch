package com.example.itunesearch.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itunesearch.R
import com.example.itunesearch.model.APICalls
import com.example.itunesearch.model.Results
import com.example.itunesearch.model.ResultsDatabase
import com.example.itunesearch.viewmodel.ResultsAdapter
import com.example.itunesearch.viewmodel.resultViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    lateinit var search: EditText
    lateinit var mresultViewModel: resultViewModel

    var gson: Gson = Gson()
    var musicArray: ArrayList<Results> = ArrayList()

    private var resultsDatabase: ResultsDatabase? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchbar: LinearLayout
    private lateinit var resultLL: LinearLayout
    private lateinit var searchItemsCount: TextView
    private lateinit var loader: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "iTuneSearch"
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.searchresultrec)
        searchbar = findViewById(R.id.searchbar)
        resultLL = findViewById(R.id.searchresultLL)
        searchItemsCount = findViewById(R.id.serachtermout)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.maintoolbar)
        loader = findViewById(R.id.loading)
        setSupportActionBar(toolbar)

        resultsDatabase = ResultsDatabase.getDatabase(this)

        search = findViewById(R.id.editTextTextPersonName)


    }

    fun searchMusic(view: View) {


        if (search.text.toString() == "") {
            Toast.makeText(this, "Please enter something", Toast.LENGTH_SHORT).show()
        } else {

            hideKeyboard(currentFocus ?: View(this))

            searchbar.visibility = View.INVISIBLE
            loader.visibility = View.VISIBLE

            var term: String = search.text.toString()


            if (isOnline(this)) {
                searchOnline(term)
            } else {
                searchDB(term)
            }


        }


    }

    private fun searchDB(term: String) {

        Thread(Runnable {


            mresultViewModel = ViewModelProvider(this).get(resultViewModel::class.java)

            var list = mresultViewModel.readAlldata

            this.runOnUiThread(Runnable {


                for (music in list) {

                    if (music.trackName.lowercase()
                            .contains(term.lowercase()) || music.collectionName.lowercase()
                            .contains(term.lowercase())
                    ) {
                        musicArray.add(music)
                    }

                }

                if (musicArray.size == 0) {
                    Toast.makeText(
                        this@SearchActivity,
                        "No Result Found! Try Something Else",
                        Toast.LENGTH_SHORT
                    ).show()

                    searchbar.visibility = View.VISIBLE
                    loader.visibility = View.GONE

                } else {

                    Toast.makeText(
                        this@SearchActivity,
                        "Top " + musicArray.size + " Results",
                        Toast.LENGTH_SHORT
                    ).show()

                    searchItemsCount.text = term
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    val adapter =
                        ResultsAdapter(musicArray)
                    recyclerView.adapter = adapter
                    loader.visibility = View.GONE
                    resultLL.visibility = View.VISIBLE

                    search.text = null
                }

            })

        }).start()

    }

    private fun searchOnline(term: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var api: APICalls = retrofit.create(APICalls::class.java)


        var call: Call<JsonObject> = api.getMusic(term)

        call.enqueue(object : Callback<JsonObject> {
            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                searchItemsCount.text = term

                if (response.body()?.get("resultCount").toString().equals("0")) {
                    Toast.makeText(
                        this@SearchActivity,
                        "No Result Found! Try Something Else",
                        Toast.LENGTH_SHORT
                    ).show()

                    searchbar.visibility = View.VISIBLE
                    loader.visibility = View.GONE

                } else {

                    Toast.makeText(
                        this@SearchActivity,
                        "Top " + response.body()?.get("resultCount").toString() + " Results",
                        Toast.LENGTH_SHORT
                    ).show()

                    val userListType = object : TypeToken<ArrayList<Results>>() {}.type


                    try {
                        musicArray = gson.fromJson<ArrayList<Results>>(
                            response.body()?.get("results").toString(),
                            userListType
                        )
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    recyclerView.layoutManager = LinearLayoutManager(applicationContext)

                    val adapter =
                        ResultsAdapter(musicArray)
                    recyclerView.adapter = adapter

                    loader.visibility = View.GONE
                    resultLL.visibility = View.VISIBLE

                    search.text = null

                    Thread(Runnable {

                        for (music in musicArray) {
                            mresultViewModel =
                                ViewModelProvider(this@SearchActivity).get(resultViewModel::class.java)
                            mresultViewModel.addMusic(music)
                        }

                    }).start()
                }


            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    this@SearchActivity,
                    "No Connection! Searching Offline",
                    Toast.LENGTH_SHORT
                ).show()
                searchDB(term)

            }

        })


    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null) {
                return true
            }
        }
        return false

    }

    override fun onBackPressed() {

        if (searchbar.visibility == View.INVISIBLE) {

            searchbar.visibility = View.VISIBLE
            resultLL.visibility = View.INVISIBLE

        } else {
            finish()
        }

    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}