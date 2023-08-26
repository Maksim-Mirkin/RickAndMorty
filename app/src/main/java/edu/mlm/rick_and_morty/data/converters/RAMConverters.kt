package edu.mlm.rick_and_morty.data.converters

import androidx.room.TypeConverter

fun <T> List<T>.toShortString() = joinToString(",") { "$it" }

class RAMConverters {
    @TypeConverter
    fun listToString(genres: List<Int>): String {
        return genres.toShortString()
    }

    @TypeConverter
    fun stringToList(string: String): List<Int> {
        return string.split(",").map { it.toInt() }
    }
}