package com.marsapps.moviesdb.response.main

import com.google.gson.annotations.SerializedName

class MainResponse(

    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("results")
    val results: List<MovieResponse?>? = null,

    @SerializedName("total_results")
    val totalResults: Int? = null,

    @SerializedName("total_pages")
    val totalPages: Int? = null
)