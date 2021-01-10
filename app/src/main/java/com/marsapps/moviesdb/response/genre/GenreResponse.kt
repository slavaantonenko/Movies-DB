package com.marsapps.moviesdb.response.genre

import com.google.gson.annotations.SerializedName

class GenreResponse(

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("id")
    val id: Int? = null
)