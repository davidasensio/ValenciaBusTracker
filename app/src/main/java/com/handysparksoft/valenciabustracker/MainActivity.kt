package com.handysparksoft.valenciabustracker

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.play.core.review.ReviewManagerFactory
import com.handysparksoft.valenciabustracker.framework.InAppReviewManager
import com.handysparksoft.valenciabustracker.framework.permission.PermissionAction
import com.handysparksoft.valenciabustracker.framework.permission.PermissionUI
import com.handysparksoft.valenciabustracker.ui.theme.ValenciaBusTrackerTheme
import timber.log.Timber
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startInAppReviewFlow(this)

        setContent {
            ValenciaBusTrackerTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }

    private fun startInAppReviewFlow(context: Context) {
        val askForAReview = Random.nextInt(InnAppReviewProbabilityLimit) == 1
        if (askForAReview) {
            (context as? Activity)?.let { activity ->
                InAppReviewManager(ReviewManagerFactory.create(context)).requestReviewFlow(activity)
            }
        }
    }

    companion object {
        const val InnAppReviewProbabilityLimit = 3
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PostNotificationPermissionUI()
    } else {
        ContentScreen(onStartServiceClick = { startService(context) })
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PostNotificationPermissionUI() {
    val context = LocalContext.current
    val permission = android.Manifest.permission.POST_NOTIFICATIONS
    var performPostNotificationPermissionRequest by remember { mutableStateOf(false) }

    if (performPostNotificationPermissionRequest) {
        PermissionUI(
            context = context,
            permission = permission,
            permissionAction = { permissionResult ->
                performPostNotificationPermissionRequest = false
                when (permissionResult) {
                    PermissionAction.Granted -> {
                        Timber.d("PermissionAction granted")
                        startService(context)
                    }
                    PermissionAction.Denied -> {
                        Timber.d("PermissionAction denied")
                    }
                }
            }
        )
    } else {
        ContentScreen(onStartServiceClick = { performPostNotificationPermissionRequest = true })
    }
}

@Composable
fun ContentScreen(onStartServiceClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = onStartServiceClick) {
            Text(text = stringResource(id = R.string.main_start_foreground_service))
        }
    }
}

private fun startService(context: Context) {
    BusStopTrackerService.startTheService(
        context,
        NotificationData(
            contentTitle = context.getString(R.string.foreground_notification_content_title),
            contentText = context.getString(R.string.foreground_notification_content_text),
            subText = context.getString(R.string.foreground_notification_subtext)
        )
    )
}

@Preview(showBackground = true)
@Composable
internal fun DefaultPreview() {
    ValenciaBusTrackerTheme {
        MainScreen()
    }
}
