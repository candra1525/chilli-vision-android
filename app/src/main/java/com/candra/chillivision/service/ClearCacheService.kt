package com.candra.chillivision.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ClearCacheService : Service() {
    override fun onTaskRemoved(rootIntent: Intent?) {
        clearAppCache(applicationContext) // Panggil clear cache di sini
        stopSelf() // Hentikan service setelah cache dihapus
        super.onTaskRemoved(rootIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

// Fungsi menghapus cache
fun clearAppCache(context: Context) {
    try {
        val cacheDir = context.cacheDir
        cacheDir.deleteRecursively()
        Log.d("CacheClear", "Cache berhasil dihapus dari service")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
