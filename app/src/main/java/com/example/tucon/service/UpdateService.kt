package com.example.tucon.service

import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.lang.Exception

class UpdateService: Service() {
    private val url = "http://konghonglee.top:8000/latestapk"
    private var manager: DownloadManager? = null
    private var receiver: DownloadCompleteReceiver? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initDownManager()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initDownManager() {
        val file = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "tucon.apk")
        if (file.exists()) {
            file.delete()
        }
        Toast.makeText(applicationContext, "下载开始!", Toast.LENGTH_SHORT).show()

        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "tucon.apk")
        request.setMimeType("application/vnd.android.package-archive")

        manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        manager?.enqueue(request)

        receiver = DownloadCompleteReceiver()
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver)
        }
        super.onDestroy()
    }

    inner class DownloadCompleteReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action.toString() == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                installApk(context, downloadId)
                this@UpdateService.stopSelf()
            }
        }

        private fun installApk(context: Context?, downloadId: Long?) {
            try {
                val file = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "tucon.apk")
                val uri = context?.let { FileProvider.getUriForFile(it, "com.example.tucon.fileprovider", file) }
                val intent = Intent()
                intent.setAction(Intent.ACTION_VIEW)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .setDataAndType(uri, "application/vnd.android.package-archive")
                applicationContext.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}