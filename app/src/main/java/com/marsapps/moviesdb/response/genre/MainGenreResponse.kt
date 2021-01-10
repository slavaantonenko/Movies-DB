package com.marsapps.moviesdb.response.genre

import com.google.gson.annotations.SerializedName

class MainGenreResponse(

    @SerializedName("genres")
    val genres: List<GenreResponse?>? = null
)