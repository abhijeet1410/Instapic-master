package com.aligohershabir.photocollage.utils;

import android.app.Activity;
import android.view.View;

import com.aligohershabir.photocollage.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AdsUtil {

    public static final String TEST_DEVICE_ID = "0D7046D9278C5272E1C84EAF94C6E901";
    public static final boolean TEST_MODE = false;
    private static InterstitialAd mInterstitialAd = null;

    public static void showBannerAd(Activity activity, final AdView adView) {
        adView.setVisibility(View.GONE);
        AdRequest.Builder builder = new AdRequest.Builder();
        if (TEST_MODE)
            builder = builder.addTestDevice(TEST_DEVICE_ID);
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
        AdRequest adRequest = builder.build();
         adView.loadAd(adRequest);
    }

    public static void preloadInterstitialAd(final Activity activity) {
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId(activity.getString(R.string.admob_interstitial_id));

        AdRequest.Builder builder = new AdRequest.Builder();
        if (TEST_MODE)
            builder = builder.addTestDevice(TEST_DEVICE_ID);
        AdRequest adRequest = builder.build();
        mInterstitialAd.loadAd(adRequest);
    }

    public static void showInterstitialAd(Activity activity,AdListener adListener) {
        if (mInterstitialAd == null || !mInterstitialAd.isLoaded()) {
            preloadInterstitialAd(activity);
            adListener.onAdFailedToLoad(0);
        } else {
            mInterstitialAd.setAdListener(adListener);
            mInterstitialAd.show();
        }
    }
}
