package com.marsapps.moviesdb.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        val list = arrayListOf<String>()

        value.split(",".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray().forEach {
            list.add(it)
        }

        return list
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        var value = ""

        list.forEachIndexed { index, item ->
            value += item

            if (index + 1 < list.size)
                value += ","
        }

        return value
    }
}