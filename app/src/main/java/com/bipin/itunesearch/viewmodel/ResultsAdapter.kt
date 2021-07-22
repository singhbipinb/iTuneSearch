package com.example.itunesearch.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.itunesearch.R
import com.example.itunesearch.model.Results

class ResultsAdapter(
    val musicList: ArrayList<Results>

) : RecyclerView.Adapter<ResultsAdapter.MusicHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicHolder {


        return MusicHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.result_view, parent, false
            ), parent.context
        )

    }

    override fun onBindViewHolder(holder: MusicHolder, position: Int) {

        holder.bindItems(musicList[position])


    }

    override fun getItemCount(): Int {
        return musicList.size
    }


    class MusicHolder(
        itemView: View,
        context: Context

    ) : RecyclerView.ViewHolder(itemView) {

        val circularProgressDrawable = CircularProgressDrawable(context)


        fun bindItems(results: Results) {
            val image = itemView.findViewById(R.id.musicimage) as ImageView


            val name = itemView.findViewById(R.id.musicname) as TextView
            val artist = itemView.findViewById(R.id.Artistname) as TextView
            val price = itemView.findViewById(R.id.musicprice) as TextView
            val genre = itemView.findViewById(R.id.titlegenre) as TextView

            if (results.trackName == null) {
                name.text = results.collectionName
            } else {
                name.text = results.trackName
            }


            artist.text = results.artistName

            if (results.trackPrice == null) {
                price.text = results.collectionPrice + " " + results.currency
            } else {
                price.text = results.trackPrice + " " + results.currency
            }


            genre.text = results.primaryGenreName

            val url = results.artworkUrl100

            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(itemView)
                .load(url).placeholder(circularProgressDrawable)
                .into(image)
        }


    }


}



