package com.savedPhotos.manager;



import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
//import com.google.android.gms.ads.MobileAds;


/**
 * Created by Mohammed.Irfan on 26-09-2017.
 */

public class AdsManager {

    public static String TAG = AdsManager.class.getSimpleName();

    //ADMOB
    private static String ADMOB_APP_ID = "ca-app-pub-7038439386966357~2264913993";
    private static String ADMOB_BANNER = "ca-app-pub-7038439386966357/5171893334";
    private static String ADMOB_INTERSTITIAL = "ca-app-pub-7038439386966357/5295058552";

    private static AdsManager adsManager;
    //private static InterstitialAd mInterstitialAd;

    private AdsManager(){

    }
    public static AdsManager getAdsManager(){
        if(adsManager == null)
            adsManager = new AdsManager();
        return adsManager;
    }

    public static void initAdMobAds(Activity activity){
        //MobileAds.initialize(activity, ADMOB_APP_ID);
        initInterstitialAd(activity);
    }

    private static void initInterstitialAd(Activity activity){
        //Load InterstitialAd
//        mInterstitialAd = new InterstitialAd(activity);
//        mInterstitialAd.setAdUnitId(ADMOB_INTERSTITIAL);
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Load the next interstitial.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
    }

    public void showAdMobInterstitial(){

//        if(mInterstitialAd!=null){
//            if (mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//            } else {
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                Log.d(TAG, "The interstitial wasn't loaded yet.");
//            }
//        }

    }

    public  void loadAdMobBannerAds(final Activity activity,final LinearLayout mainLayout){

        LinearLayout.LayoutParams bannerParameters =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

//        AdView adView = new AdView(activity);
//        adView.setAdSize(AdSize.SMART_BANNER);
//
//        adView.setAdUnitId(ADMOB_BANNER);
//
//        // Add to main Layout
//        mainLayout.addView(adView, bannerParameters);
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//
//        mainLayout.setVisibility(View.GONE);
//
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                Log.i("Ads", "onAdLoaded");
//                mainLayout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//                Log.i("Ads", "onAdFailedToLoad");
//                //mainLayout.setVisibility(View.GONE);
//                if(mainLayout.getChildCount()>0){
//                   // mainLayout.removeAllViews();
//                    //initBannerStartApp(activity,mainLayout);
//                }
//               // onStickerSelect(activity,1,1,-1);
//
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the splash.
//                Log.i("Ads", "onAdOpened");
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//                Log.i("Ads", "onAdLeftApplication");
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when when the user is about to return
//                // to the app after tapping on an ad.
//                Log.i("Ads", "onAdClosed");
//                //onStickerSelect(activity,1,1,-1);
//            }
//        });

    }

    public void onStickerSelect(Activity activity, int position, int stickerType, int resultCode){
        Intent full = new Intent();
        full.putExtra("id", position);
        full.putExtra("StickerType", stickerType);
        activity.setResult(resultCode, full);
        activity.finish();
    }


}


