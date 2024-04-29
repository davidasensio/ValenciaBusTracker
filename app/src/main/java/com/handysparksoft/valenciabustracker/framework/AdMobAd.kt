@file:SuppressLint("VisibleForTests")

package com.handysparksoft.valenciabustracker.framework

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.handysparksoft.valenciabustracker.BuildConfig
import timber.log.Timber

object AdMobAd {
    const val ADMOB_APP_TEST_ID = "ca-app-pub-3940256099942544~3347511713"

    const val ADMOB_AD_TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
    const val ADMOB_AD_TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
    const val ADMOB_AD_TEST_INTERSTITIAL_VIDEO_ID = "ca-app-pub-3940256099942544/8691691433"
    const val ADMOB_AD_TEST_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917"
    const val ADMOB_AD_TEST_REWARDED_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/5354046379"
    const val ADMOB_AD_TEST_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110"
    const val ADMOB_AD_TEST_NATIVE_VIDEO_ID = "ca-app-pub-3940256099942544/1044960115"
    const val ADMOB_AD_TEST_APP_OPEN_ID = "ca-app-pub-3940256099942544/3419835294"

    sealed class AdMobType(private val testAdId: String) {
        data class AdBanner(val adId: String) : AdMobType(ADMOB_AD_TEST_BANNER_ID)
        data class AdInterstitial(val adId: String) : AdMobType(ADMOB_AD_TEST_INTERSTITIAL_ID)
        data class AdInterstitialVideo(val adId: String) : AdMobType(ADMOB_AD_TEST_INTERSTITIAL_VIDEO_ID)
        data class AdRewardedInterstitial(val adId: String) : AdMobType(ADMOB_AD_TEST_REWARDED_INTERSTITIAL_ID)
        data class AdRewardedOptIn(val adId: String) : AdMobType(ADMOB_AD_TEST_REWARDED_ID)
        data class AdNative(val adId: String) : AdMobType(ADMOB_AD_TEST_NATIVE_ID)
        data class AdNativeVideo(val adId: String) : AdMobType(ADMOB_AD_TEST_NATIVE_VIDEO_ID)
        data class AdAppOpen(val adId: String) : AdMobType(ADMOB_AD_TEST_APP_OPEN_ID)

        fun getId(): String = if (BuildConfig.DEBUG) {
            testAdId
        } else {
            when (this) {
                is AdBanner -> adId
                is AdInterstitial -> adId
                is AdInterstitialVideo -> adId
                is AdRewardedInterstitial -> adId
                is AdRewardedOptIn -> adId
                is AdNative -> adId
                is AdNativeVideo -> adId
                is AdAppOpen -> adId
            }
        }
    }

    fun loadInterstitialAd(
        context: Context,
        adId: String,
        onLoaded: (InterstitialAd) -> Unit
    ) {
        InterstitialAd.load(
            context,
            adId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    super.onAdLoaded(ad)
                    onLoaded(ad)
                    Timber.d("Interstitial ad load success")
                }

                override fun onAdFailedToLoad(ad: LoadAdError) {
                    super.onAdFailedToLoad(ad)
                    Timber.d("Interstitial ad load failure")
                }
            }
        )
    }

    fun loadRewardedAd(
        context: Context,
        adId: String,
        onLoaded: (RewardedAd) -> Unit
    ) {
        RewardedAd.load(
            context,
            adId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    onLoaded(ad)
                    Timber.d("Interstitial ad load success")
                }

                override fun onAdFailedToLoad(ad: LoadAdError) {
                    super.onAdFailedToLoad(ad)
                    Timber.d("Interstitial ad load failure")
                }
            }
        )
    }

    fun loadRewardedInterstitialAd(
        context: Context,
        adId: String,
        onLoaded: (RewardedInterstitialAd) -> Unit
    ) {
        RewardedInterstitialAd.load(
            context,
            adId,
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    super.onAdLoaded(ad)
                    onLoaded(ad)
                    Timber.d("Interstitial ad load success")
                }

                override fun onAdFailedToLoad(ad: LoadAdError) {
                    super.onAdFailedToLoad(ad)
                    Timber.d("Interstitial ad load failure")
                }
            }
        )
    }

    @Composable
    fun AdBannerViewCompose(
        adId: String,
        modifier: Modifier = Modifier,
        adSize: AdSize = AdSize.BANNER
    ) {
        val isInEditMode = LocalInspectionMode.current
        if (isInEditMode) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(horizontal = 2.dp, vertical = 6.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Ad Place"
            )
        } else {
            AndroidView(modifier = modifier, factory = { context ->
                AdView(context).apply {
                    setAdSize(adSize)
                    this.adUnitId = adId
                    this.loadAd(AdRequest.Builder().build())
                }
            })
        }
    }
}
