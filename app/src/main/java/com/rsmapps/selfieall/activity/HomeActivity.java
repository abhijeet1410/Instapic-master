package com.rsmapps.selfieall.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aligohershabir.photocollage.model.Path;
import com.aligohershabir.photocollage.utils.ResourceManager;
import com.aligohershabir.photocollage.view.activity.CollageActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.mirror.utils.Constant;
import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = HomeActivity.class.getSimpleName();

    private static final int REQUEST_READ_PERMISSION = 101;

    public static final String APP_URL =
            "https://play.google.com/store/apps/details?id="+ com.rsmapps.selfieall.helper.ResourceManager.PACKAGE_NAME;

    public static final String APP_URL_PHOTO_MOVIE_MAKER =
            "https://play.google.com/store/apps/details?id=com.rsmapps.photovideomaker";

    private ActivityHomeBinding binding;

    private void setInfo(){
//        AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));

        binding.ivStartNow.setOnClickListener(this);
        binding.ivViewSavedPhotos.setOnClickListener(this);
        binding.ivDownload.setOnClickListener(this);
        binding.ivRateThisApp.setOnClickListener(this);
        binding.ivPhotoCollageEditor.setOnClickListener(this);

        binding.cvHomeAd.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        setInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**
             * New Controls
             */
            case R.id.ivInstaPic:
                AdsUtil.showInterstitialAd(HomeActivity.this, new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Intent pip = new Intent(HomeActivity.this, com.instapic.activities.PhotoSelectActivity.class);
                        pip.putExtra("SHAPE","shape");
                        pip.putExtra(Constant.FEATURE, Constant.PIP);
                        startActivity(pip);
                        AdsUtil.preloadInterstitialAd(HomeActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Intent pip = new Intent(HomeActivity.this, com.instapic.activities.PhotoSelectActivity.class);
                        pip.putExtra("SHAPE","shape");
                        pip.putExtra(Constant.FEATURE, Constant.PIP);
                        startActivity(pip);
                        AdsUtil.preloadInterstitialAd(HomeActivity.this);
                    }
                });
                break;
            case R.id.ivSavedPhotos:
                AdsUtil.showInterstitialAd(HomeActivity.this, new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        startActivity(new Intent(HomeActivity.this,
                                com.savedPhotos.activity.MyCreationActivity.class));
                        AdsUtil.preloadInterstitialAd(HomeActivity.this);

                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdLoaded();
                        startActivity(new Intent(HomeActivity.this,
                                com.savedPhotos.activity.MyCreationActivity.class));
                        AdsUtil.preloadInterstitialAd(HomeActivity.this);
                    }
                });
                break;
            case R.id.ivRateApp:
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(APP_URL)));
                break;
            case R.id.ivDownload:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(APP_URL_PHOTO_MOVIE_MAKER)));
                break;
//            case R.id.cvHomeAd:
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(APP_URL_PHOTO_MOVIE_MAKER)));
//                break;

            /**
             * Old Controls
             */
            case R.id.ivStartNow:
                AdsUtil.showInterstitialAd(HomeActivity.this, new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        startActivity(new Intent(HomeActivity.this, KohaliActivity.class));
                        AdsUtil.preloadInterstitialAd(HomeActivity.this);

                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdLoaded();
                        startActivity(new Intent(HomeActivity.this, KohaliActivity.class));
                        AdsUtil.preloadInterstitialAd(HomeActivity.this);
                    }
                });
                break;
            case R.id.ivPhotoCollageEditor:
                AdsUtil.showInterstitialAd(HomeActivity.this, new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        requestStoragePermission();
                        AdsUtil.preloadInterstitialAd(HomeActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        requestStoragePermission();
                        AdsUtil.preloadInterstitialAd(HomeActivity.this);
                    }
                });
                break;
            case R.id.ivViewSavedPhotos:
                AdsUtil.showInterstitialAd(HomeActivity.this, new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        startActivity(new Intent(HomeActivity.this,
                                com.savedPhotos.activity.MyCreationActivity.class));
                        AdsUtil.preloadInterstitialAd(HomeActivity.this);

                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdLoaded();
                        startActivity(new Intent(HomeActivity.this,
                                com.savedPhotos.activity.MyCreationActivity.class));
                        AdsUtil.preloadInterstitialAd(HomeActivity.this);
                    }
                });
                break;
            case R.id.ivRateThisApp:
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(APP_URL)));
                break;
        }
    }

    private void requestStoragePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        } else {
            startMultiImagePickerActivity();
        }

    }

    private void startMultiImagePickerActivity(){
        Intent mIntent = new Intent(this, PickImageActivity.class);
        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 9);
        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
        startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startMultiImagePickerActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
//            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
//            // do your logic here...
//            ResourceManager.images = images;
//            startActivity(new Intent(ChooserActivity.this, CollageActivity.class));
//        }
        super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here
        // so ImagePicker can work with fragment mode

        if(resultCode != RESULT_OK){
            return;
        }

        ArrayList<String> pathList;
        if (requestCode == PickImageActivity.PICKER_REQUEST_CODE) {
            pathList = data.getExtras().getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
            if (pathList != null && !pathList.isEmpty()) {
                StringBuilder sb=new StringBuilder("");
                for(int i=0;i<pathList.size();i++) {
                    sb.append("Photo"+(i+1)+":"+pathList.get(i));
                    sb.append("\n");
                }
//                Log.i(TAG, "pathList: " + sb.toString());
            }
            ArrayList<Path> paths = new ArrayList<>();
            for(String s:pathList){
                paths.add(new Path(s,false));
            }
            ResourceManager.paths = paths;
            startActivity(new Intent(HomeActivity.this, CollageActivity.class));
        }
    }
}
