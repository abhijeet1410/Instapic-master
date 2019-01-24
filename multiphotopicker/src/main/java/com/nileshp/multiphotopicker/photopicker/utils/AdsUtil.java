package com.nileshp.multiphotopicker.photopicker.utils;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nileshp.multiphotopicker.R;

public class AdsUtil {

    public static final String TEST_DEVICE_ID = "0D7046D9278C5272E1C84EAF94C6E901";
    public static final boolean TEST_MODE = false;
    public static final boolean ADS_ENABLED = true;
    private static InterstitialAd mInterstitialAd = null;

    public static void showBannerAd(Activity activity, final AdView adView) {
        if(!ADS_ENABLED){
            adView.setVisibility(View.GONE);
            return;
        }
        adView.setVisibility(View.GONE);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });

        AdRequest.Builder builder = new AdRequest.Builder();
        if (TEST_MODE)
            builder = builder.addTestDevice(TEST_DEVICE_ID);
        AdRequest adRequest = builder.build();
        adView.loadAd(adRequest);
    }

    public static void preloadInterstitialAd(final Activity activity) {
        if(!ADS_ENABLED){
            return;
        }
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId(activity.getString(R.string.admob_interstitial_id));

        AdRequest.Builder builder = new AdRequest.Builder();
        if (TEST_MODE)
            builder = builder.addTestDevice(TEST_DEVICE_ID);
        AdRequest adRequest = builder.build();
        mInterstitialAd.loadAd(adRequest);
    }

    public static void showInterstitialAd(Activity activity, AdListener adListener) {
        if(!ADS_ENABLED){
            adListener.onAdFailedToLoad(0);
            return;
        }
        if (mInterstitialAd == null || !mInterstitialAd.isLoaded()) {
            preloadInterstitialAd(activity);
            adListener.onAdFailedToLoad(0);
        } else {
            mInterstitialAd.setAdListener(adListener);
            mInterstitialAd.show();
        }
    }
}

