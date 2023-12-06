package com.danilo.evolveusers.util

import java.sql.Date
import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object convertDate {
    private val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())

    fun formatToSQLiteDate(timestamp: Timestamp): String {


        return dateFormat.format(Date(timestamp.seconds * 1000L))
    }



    fun parseStringToTimestamp(dateString: String): Timestamp? {
        return try {
            val date = dateFormat.parse(dateString)
            date?.let { Timestamp(it.time) }
        } catch (e: ParseException) {
            e.printStackTrace()
            // Lide com a exceção conforme necessário
            null
        }
    }
}