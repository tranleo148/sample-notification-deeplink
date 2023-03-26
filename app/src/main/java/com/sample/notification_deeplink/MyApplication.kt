package com.sample.notification_deeplink

import com.ads.control.admob.Admob
import com.ads.control.admob.AppOpenManager
import com.ads.control.ads.AperoAd
import com.ads.control.application.AdsMultiDexApplication
import com.ads.control.config.AdjustConfig
import com.ads.control.config.AperoAdConfig
import com.sample.notification_deeplink.activity.SplashActivity

class MyApplication : AdsMultiDexApplication() {
    //private val APPSFLYER_TOKEN = "2PUNpdyDTkedZTgeKkWCyB"
    private val ADJUST_TOKEN = "cc4jvudppczk"
    private val EVENT_PURCHASE_ADJUST = "gzel1k"
    private val EVENT_AD_IMPRESSION_ADJUST = "gzel1k"

    override fun onCreate() {
        super.onCreate()
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity::class.java)
        Admob.getInstance().setNumToShowAds(0)
        initAds()
    }

    private fun initAds() {
        val environment =
            if (BuildConfig.env_dev) AperoAdConfig.ENVIRONMENT_DEVELOP else AperoAdConfig.ENVIRONMENT_PRODUCTION
        aperoAdConfig = AperoAdConfig(this, AperoAdConfig.PROVIDER_ADMOB, environment)

        // Optional: setup Adjust event
        val adjustConfig = AdjustConfig(true, ADJUST_TOKEN)
        adjustConfig.eventAdImpression = EVENT_AD_IMPRESSION_ADJUST
        adjustConfig.eventNamePurchase = EVENT_PURCHASE_ADJUST
        aperoAdConfig.adjustConfig = adjustConfig

        // Optional: setup Appsflyer event
//        AppsflyerConfig appsflyerConfig = new AppsflyerConfig(true,APPSFLYER_TOKEN);
//        aperoAdConfig.setAppsflyerConfig(appsflyerConfig);

        // Optional: enable ads resume
        aperoAdConfig.idAdResume = BuildConfig.ads_open_app

        // Optional: setup list device test - recommended to use
        listTestDevice.add("EC25F576DA9B6CE74778B268CB87E431")
        aperoAdConfig.listDeviceTest = listTestDevice
        AperoAd.getInstance().init(this, aperoAdConfig, false)

        // Auto disable ad resume after user click ads and back to app
        Admob.getInstance().setDisableAdResumeWhenClickAds(true)
        // If true -> onNextAction() is called right after Ad Interstitial showed
        Admob.getInstance().setOpenActivityAfterShowInterAds(false)
    }
}