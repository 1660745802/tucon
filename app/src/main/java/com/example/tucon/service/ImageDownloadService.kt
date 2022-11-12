package com.example.tucon.service

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.example.tucon.utils.BitmapUtil
import com.example.tucon.utils.ImageUrlUtil
import kotlinx.coroutines.runBlocking
import java.io.File


class ImageDownloadService: IntentService("ImageDownloadService") {
    override fun onHandleIntent(intent: Intent?) {
        try {
            runBlocking {
                val imageUrl = intent?.getStringExtra("imagePath")
                var imageName = ImageUrlUtil.getUrlName(imageUrl)
                saveImageByBitmap(imageName, getBitmap(ImageUrlUtil.changeUrlQuality(imageUrl, 2))!!)
            }
        } catch (e: InterruptedException) {
            Toast.makeText(applicationContext, "图片下载失败!", Toast.LENGTH_SHORT).show()
            Thread.currentThread().interrupt()
        }
    }
    private suspend fun getBitmap(imgUrl: String): Bitmap? {
        val request = ImageRequest.Builder(this)
            .data(imgUrl)
            .allowHardware(false)
            .build()
        val result = imageLoader.execute(request).drawable
        return result?.toBitmap()
    }
    private fun saveImageByBitmap(fileName: String, bitmap: Bitmap) {
        try {
            val uri = BitmapUtil.saveImageByBitmap(
                applicationContext,
                fileName,
                Environment.getExternalStorageDirectory().path.plus(
                    File.separator + Environment.DIRECTORY_DCIM + File.separator + "Tucon"),
                bitmap
            )
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(uri)
            applicationContext.sendBroadcast(intent)
            Toast.makeText(applicationContext, "图片下载成功!", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Toast.makeText(this, "图片下载失败!", Toast.LENGTH_SHORT).show()
        }
    }
}