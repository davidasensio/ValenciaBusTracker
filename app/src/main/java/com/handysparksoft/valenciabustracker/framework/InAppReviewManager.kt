package com.handysparksoft.valenciabustracker.framework

import android.app.Activity
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.model.ReviewErrorCode
import timber.log.Timber

class InAppReviewManager(private val reviewManager: ReviewManager) {
    fun requestReviewFlow(activity: Activity) {
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                launchReviewFlow(activity, reviewInfo)
                Timber.d("addOnCompleteListener of requestReviewFlow function successful")
            } else {
                @ReviewErrorCode
                val reviewErrorCode = (task.exception as ReviewException).errorCode
                Timber.e(reviewErrorCode.toString())
            }
        }
    }

    private fun launchReviewFlow(activity: Activity, reviewInfo: ReviewInfo) {
        val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
        flow.addOnCompleteListener { _ ->
            // The flow has finished.
            Timber.d("addOnCompleteListener of launchReviewFlow function successful")
        }
    }
}
