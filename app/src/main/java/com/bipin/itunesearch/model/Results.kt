package com.example.itunesearch.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_stored")
data class Results(
    var wrapperType: String,
    var kind: String,
    var artistId: String,
    var collectionId: String,
    @PrimaryKey
    var trackId: String,
    var artistName: String,
    var collectionName: String,
    var trackName: String,
    var collectionCensoredName: String,
    var trackCensoredName: String,
    var artistViewUrl: String,
    var collectionViewUrl: String,
    var trackViewUrl: String,
    var previewUrl: String,
    var artworkUrl30: String,
    var artworkUrl60: String,
    var artworkUrl100: String,
    var collectionPrice: String,
    var trackPrice: String,
    var releaseDate: String,
    var collectionExplicitness: String,
    var trackExplicitness: String,
    var discCount: String,
    var discNumber: String,
    var trackCount: String,
    var trackNumber: String,
    var trackTimeMillis: String,
    var country: String,
    var currency: String,
    var primaryGenreName: String,
    var isStreamable: String


)
