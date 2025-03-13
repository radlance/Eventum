package com.radlance.eventum.presentation.profile.edit

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.ByteArrayOutputStream

interface ImageState {

    fun image(): Any

    fun byteArray(context: Context): ByteArray?

    class BitmapImage(private val bitmap: Bitmap) : ImageState {

        override fun image(): Any = bitmap

        override fun byteArray(context: Context): ByteArray? {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            return stream.toByteArray()
        }
    }

    class UriImage(private val uri: Uri) : ImageState {

        override fun image(): Any = uri

        override fun byteArray(context: Context): ByteArray? {
            return context.contentResolver.openInputStream(uri)?.use { it.buffered().readBytes() }
        }
    }

    object Base : ImageState {

        override fun image(): Any = Unit

        override fun byteArray(context: Context): ByteArray? = null
    }
}
