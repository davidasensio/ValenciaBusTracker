package com.handysparksoft.valenciabustracker

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.play.core.review.ReviewManagerFactory
import com.handysparksoft.valenciabustracker.framework.InAppReviewManager
import com.handysparksoft.valenciabustracker.ui.theme.ValenciaBusTrackerTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startInAppReviewFlow(this)

        setContent {
            ValenciaBusTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Greeting("Android")
                        Button(onClick = {
                            BusStopTrackerService.startTheService(
                                this@MainActivity,
                                NotificationData("Title", "Text", "Subtext")
                            )
                        }) {
                            Text(text = "Start foreground service")
                        }
                    }
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
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ValenciaBusTrackerTheme {
        Greeting("Android")
    }
}
