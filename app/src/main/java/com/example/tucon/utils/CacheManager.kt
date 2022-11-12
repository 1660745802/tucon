package com.example.tucon.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode


object CacheManager {
    fun getTotalCacheSize(context: Context?): String {
        if (context == null) return ""
        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.externalCacheDir!!)
        }
        cacheSize += getFolderSize(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!)
        return getFormatSize(cacheSize)
    }

    private fun getFormatSize(cacheSize: Long): String {
        val kiloByte = cacheSize.toDouble() / 1024
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            return BigDecimal(kiloByte).setScale(2, RoundingMode.FLOOR).toString().plus("KB")
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            return BigDecimal(megaByte).setScale(2, RoundingMode.FLOOR).toString().plus("MB")
        }
        val teraByte = megaByte / 1024
        if (teraByte < 1) {
            return BigDecimal(gigaByte).setScale(2, RoundingMode.FLOOR).toString().plus("GB")
        }
        return BigDecimal(teraByte).setScale(2, RoundingMode.FLOOR).toString().plus("TB")
    }

    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (i in fileList) {
                size += if (i.isDirectory) getFolderSize(i) else i.length()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteDir(context.externalCacheDir)
        }
        deleteDir(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS))
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir == null) {
            return false
        }
        if (dir.isDirectory) {
            val children: Array<String> = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }
}