package com.handysparksoft.valenciabustracker

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.handysparksoft.valenciabustracker.framework.AdMobAd
import com.handysparksoft.valenciabustracker.framework.AdMobAd.AdMobType.AdBanner
import com.handysparksoft.valenciabustracker.framework.AdMobAd.AdMobType.AdInterstitial
import com.handysparksoft.valenciabustracker.framework.AdMobAd.AdMobType.AdInterstitialVideo
import com.handysparksoft.valenciabustracker.framework.AdMobAd.AdMobType.AdRewardedInterstitial
import com.handysparksoft.valenciabustracker.framework.AdMobAd.AdMobType.AdRewardedOptIn
import com.handysparksoft.valenciabustracker.ui.CombinedPreviews
import com.handysparksoft.valenciabustracker.ui.theme.ValenciaBusTrackerTheme
import timber.log.Timber

class AdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ValenciaBusTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdScreen()
                }
            }
        }
    }
}

@Composable
fun AdScreen() {
    val adBanner1 = AdBanner(stringResource(id = R.string.admob_ad_banner_1_id))
    val adInterstitial = AdInterstitial(stringResource(id = R.string.admob_ad_interstitial_1_id))
    val adInterstitialVideo = AdInterstitialVideo(stringResource(id = R.string.admob_ad_interstitial_1_id))
    val adRewardedInterstitial = AdRewardedInterstitial(stringResource(id = R.string.admob_ad_rewarded_optin_1_id))
    val adRewardedOptIn = AdRewardedOptIn(stringResource(id = R.string.admob_ad_rewarded_interstitial_1_id))

    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Choose a sample Ad",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                LoadAdInterstitialButton(adInterstitial)
                LoadAdInterstitialVideoButton(adInterstitialVideo)
                LoadAdRewardedInterstitialButton(adRewardedInterstitial)
                LoadAdRewardedOptInButton(adRewardedOptIn)
            }
            AdMobAd.AdBannerViewCompose(
                adId = adBanner1.getId(),
                modifier = Modifier.defaultMinSize(minHeight = 50.dp)
            )
        }
    }
}

@Composable
fun LoadAdInterstitialButton(ad: AdInterstitial) {
    LoadAdButton(ad = ad, onClick = { activity ->
        AdMobAd.loadInterstitialAd(activity, ad.getId()) { interstitialAd ->
            interstitialAd.show(activity)
        }
    })
}

@Composable
fun LoadAdInterstitialVideoButton(ad: AdInterstitialVideo) {
    LoadAdButton(ad = ad, onClick = { activity ->
        AdMobAd.loadInterstitialAd(activity, ad.getId()) { interstitialVideoAd ->
            interstitialVideoAd.show(activity)
        }
    })
}

@Composable
fun LoadAdRewardedOptInButton(ad: AdRewardedOptIn) {
    LoadAdButton(ad = ad, onClick = { activity ->
        AdMobAd.loadRewardedAd(activity, ad.getId()) { rewardedAd ->
            rewardedAd.show(activity) { rewardItem ->
                Timber.d("Reward Item: ${rewardItem.amount} - ${rewardItem.type}")
            }
        }
    })
}

@Composable
fun LoadAdRewardedInterstitialButton(ad: AdRewardedInterstitial) {
    LoadAdButton(ad = ad, onClick = { activity ->
        AdMobAd.loadRewardedInterstitialAd(activity, ad.getId()) { rewardedInterstitialAd ->
            rewardedInterstitialAd.show(activity) { rewardItem ->
                Timber.d("Reward Item Interstitial: ${rewardItem.amount} - ${rewardItem.type}")
            }
        }
    })
}

@Composable
private fun LoadAdButton(ad: AdMobAd.AdMobType, onClick: (activity: Activity) -> Unit) {
    val context = LocalContext.current
    val text = when (ad) {
        is AdBanner -> "Load Ad Banner"
        is AdInterstitial -> "Load Ad Interstitial"
        is AdInterstitialVideo -> "Load Ad Interstitial Video"
        is AdRewardedInterstitial -> "Load Ad Rewarded Interstitial"
        is AdRewardedOptIn -> "Load Ad Rewarded (Opt-In)"
        is AdMobAd.AdMobType.AdNative -> "Load Ad Native"
        is AdMobAd.AdMobType.AdNativeVideo -> "Load Ad Native Video"
        is AdMobAd.AdMobType.AdAppOpen -> "Load Ad App Open"
    }

    Button(onClick = {
        (context as? Activity)?.let { onClick(context) }
    }) {
        Text(text = text)
    }
}

@CombinedPreviews
@Composable
private fun AdScreenPreview() {
    ValenciaBusTrackerTheme {
        AdScreen()
    }
}

@Preview
@Composable
private fun AdViewComposePreview() {
    val adBannerTestId = AdMobAd.ADMOB_AD_TEST_INTERSTITIAL_ID
    AdMobAd.AdBannerViewCompose(adId = adBannerTestId)
}
