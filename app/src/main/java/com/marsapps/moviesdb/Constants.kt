package com.marsapps.moviesdb

class Constants {
    object PERSISTENCE {
        const val DB_NAME = "movies_database"
        const val MOVIES_TABLE = "movies"
        const val GENRES_TABLE = "genres"
        const val WATCHLISTS_TABLE = "watchlists"
    }

    object REST {
        const val BASE_API_URL = "https://api.themoviedb.org/3/"
        const val BASE_IMAGES_URL = "https://image.tmdb.org/t/p/w500"

        // TODO change API key
        private const val API_KEY = "?api_key=d0faff448ac31079d756cb781c7e9727"//40ab4b29399a2e3f961acf68acc457e8"

        const val POPULAR = "movie/popular$API_KEY"
        const val GENRES = "genre/movie/list$API_KEY"

        const val QUERY_KEY_REGION = "region"
        const val QUERY_KEY_REGION_DEFAULT = "US"
        const val QUERY_KEY_PAGE = "page"
        const val QUERY_KEY_PAGE_DEFAULT = 1

        const val MINIMUM_VISIBLE_MOVIES_LEFT = 5
        const val MOVIES_PER_PAGE = 20

        const val SUCCESSFUL_CODE = 200
    }

    object WebView {
        const val TIMEOUT = 5000L
        const val YOUTUBE_URL = "https://www.youtube.com/"
        const val YOUTUBE_SEARCH_QUERY = "results?search_query="
    }
}