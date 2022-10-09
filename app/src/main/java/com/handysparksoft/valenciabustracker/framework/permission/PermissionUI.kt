package com.handysparksoft.valenciabustracker.framework.permission

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.handysparksoft.valenciabustracker.PostNotificationPermissionRationaleScreen
import timber.log.Timber

@Composable
fun PermissionUI(
    context: Context,
    permission: String,
    permissionAction: (PermissionAction) -> Unit
) {
    val permissionGranted = PermissionHelper.isPermissionGranted(
        context,
        permission
    )

    if (permissionGranted) {
        Timber.d("Permission already granted, exiting...")
        permissionAction(PermissionAction.Granted)
        return
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Timber.d("Permission accepted by user")
            permissionAction(PermissionAction.Granted)
        } else {
            Timber.d("Permission denied or skipped by user")
            permissionAction(PermissionAction.Denied)
        }
    }

    val showPermissionRationale = PermissionHelper.shouldShowPermissionRationale(
        context,
        permission
    )

    if (showPermissionRationale) {
        PostNotificationPermissionRationaleScreen(permissionRationaleAction = { permissionRationaleAction ->
            when (permissionRationaleAction) {
                PermissionRationaleAction.Granted -> {
                    Timber.d("Permission rationale Granted. Requesting permission for $permission")
                    launcher.launch(permission)
                }
                PermissionRationaleAction.Skipped -> {
                    Timber.d("Permission rationale skipped")
                    permissionAction(PermissionAction.Denied)
                }
            }
        })
        Timber.d("Showing permission rationale for $permission")
    } else {
        SideEffect {
            launcher.launch(permission)
        }
        Timber.d("Requesting permission for $permission")
    }
}
