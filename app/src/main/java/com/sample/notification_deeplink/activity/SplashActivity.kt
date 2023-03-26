package com.sample.notification_deeplink.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApNativeAd
import com.ads.control.billing.AppPurchase
import com.ads.control.config.AperoAdConfig
import com.sample.notification_deeplink.BuildConfig
import com.sample.notification_deeplink.R
import com.sample.notification_deeplink.databinding.ActivitySplashBinding
import com.sample.notification_deeplink.services.MyFirebaseMessagingService

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    companion object {
        private const val TAG = "SplashActivity"
    }

    private var idAdSplash: String? = null
    private var deeplink: String? = null

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (AperoAd.getInstance().mediationProvider == AperoAdConfig.PROVIDER_ADMOB)
            idAdSplash = BuildConfig.ad_interstitial_splash
        AppPurchase.getInstance()
            .setBillingListener({ runOnUiThread { loadSplash() } }, 5000)
        AppPurchase.getInstance().setEventConsumePurchaseTest(findViewById(R.id.txtLoading))
        intent.extras?.getString(MyFirebaseMessagingService.KEY_LINK)?.run {
            deeplink = this
        }
    }

    private var adCallback: AperoAdCallback = object : AperoAdCallback() {
        override fun onAdFailedToLoad(i: ApAdError?) {
            super.onAdFailedToLoad(i)
            Log.d(TAG, "onAdLoaded")
        }

        override fun onAdLoaded() {
            super.onAdLoaded()
            Log.d(TAG, "onAdLoaded")
        }

        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
            super.onNativeAdLoaded(nativeAd)
        }

        override fun onNextAction() {
            super.onNextAction()
            Log.d(TAG, "onNextAction")
            startMain()
        }

        override fun onAdSplashReady() {
            super.onAdSplashReady()
        }

        override fun onAdClosed() {
            super.onAdClosed()
            Log.d(TAG, "onAdClosed")
        }
    }

    private fun loadSplash() {
        Log.d(TAG, "onCreate: show splash ads")
        AperoAd.getInstance().setInitCallback {
            AperoAd.getInstance().loadSplashInterstitialAds(
                this@SplashActivity,
                idAdSplash,
                30000,
                5000,
                true,
                adCallback
            )
        }
    }

    private fun startMain() {
        val intent = Intent(
            this,
            MainActivity::class.java
        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        deeplink?.run {
            intent.putExtra(MyFirebaseMessagingService.KEY_LINK, this)
        }
        startActivity(intent)
        //finish()
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "Splash onPause: ")
        AperoAd.getInstance().onCheckShowSplashWhenFail(this, adCallback, 1000)
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "Splash onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "Splash onStop: ")
    }

    override fun onDestroy() {
//        AppOpenManager.getInstance().removeFullScreenContentCallback();
        super.onDestroy()
    }
}