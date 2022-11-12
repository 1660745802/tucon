package com.example.tucon.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


object BitmapUtil {

    fun saveImageByBitmap(context: Context, fileName: String, dirPath: String?, bitmap: Bitmap): Uri {
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dirPath, fileName)
        if (file.exists()) {
            file.delete()
        }
        val fos = FileOutputStream(file)
        bitmap.compress(
            if (fileName.endsWith(".jpg")) Bitmap.CompressFormat.JPEG else Bitmap.CompressFormat.PNG,
            100, fos
        )
        fos.flush()
        fos.close()
        return if (dirPath == context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString())
            FileProvider.getUriForFile(context, "com.example.tucon.fileprovider", file)
        else
            Uri.fromFile(file)
    }
}