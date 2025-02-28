package com.example.ezemkofi.ui.theme

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

fun downloadImage(url: String): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input: InputStream = connection.inputStream
        bitmap = BitmapFactory.decodeStream(input)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}