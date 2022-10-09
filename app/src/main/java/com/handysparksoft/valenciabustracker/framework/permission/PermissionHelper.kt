package com.handysparksoft.valenciabustracker.framework.permission

import android.app.Activity
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

object PermissionHelper {
    fun isPermissionGranted(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED

    fun shouldShowPermissionRationale(context: Context, permission: String) =
        (context as? Activity)?.shouldShowRequestPermissionRationale(permission) == true
}

enum class PermissionAction { Granted, Denied }
enum class PermissionRationaleAction { Granted, Skipped }
