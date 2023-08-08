package com.d103.asaf.ui.library

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class CheckPermission(private val context: Context) {
    fun runtimeCheckPermission(context: Context?, vararg permissions: String?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission( context, permission!! ) != PackageManager.PERMISSION_GRANTED ) {
                    return false
                }
            }
        }
        return true
    }
}