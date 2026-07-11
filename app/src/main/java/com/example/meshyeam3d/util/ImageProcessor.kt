package com.example.meshyeam3d.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import kotlin.math.min

object ImageProcessor {
    fun centerCropToObjectFrame(context: Context, source: File): Uri {
        val bitmap = BitmapFactory.decodeFile(source.absolutePath)
        val size = min(bitmap.width, bitmap.height)
        val left = (bitmap.width - size) / 2
        val top = (bitmap.height - size) / 2
        val cropped = Bitmap.createBitmap(bitmap, left, top, size, size)
        val output = File(context.cacheDir, "meshy_crop_${System.currentTimeMillis()}.jpg")
        output.outputStream().use { stream ->
            cropped.compress(Bitmap.CompressFormat.JPEG, 92, stream)
        }
        bitmap.recycle()
        cropped.recycle()
        source.delete()
        return Uri.fromFile(output)
    }
}
