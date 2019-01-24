package com.rsmapps.selfieall.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.InjectView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;


/**
 * Created by MDIRFAN on 4/1/17.
 */

public class StartActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.llyStart)
    LinearLayout llyStart;
    @InjectView(R.id.llyGallery)
    LinearLayout  llyGallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        AdsUtil.preloadInterstitialAd(this);
        //AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_top));
        //AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));
    }

    @Override
    public void onBackPressed() {
        open();
    }

    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setMessage("Are you sure, You want to exit from App.");
        alertDialogBuilder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view == llyStart){
            Intent intent = new Intent(this,PhotoSelectActivity.class);
            if(getIntent().hasExtra("url")){
                intent.putExtra("url",getIntent().getStringExtra("url"));
            }
            startActivity(intent);

           /* AdsUtil.showInterstitialAd(StartActivity.this,new AdListener(){
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    Intent intent = new Intent(StartActivity.this,PhotoSelectActivity.class);
                    if(getIntent().hasExtra("url")){
                        intent.putExtra("url",getIntent().getStringExtra("url"));
                    }
                    startActivity(intent);
                    AdsUtil.preloadInterstitialAd(StartActivity.this);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    Intent intent = new Intent(StartActivity.this,PhotoSelectActivity.class);
                    if(getIntent().hasExtra("url")){
                        intent.putExtra("url",getIntent().getStringExtra("url"));
                    }
                    startActivity(intent);
                    AdsUtil.preloadInterstitialAd(StartActivity.this);
                }
            });*/

        }else  if(view == llyGallery){
            Intent intent = new Intent(this,MyCreationActivity.class);
            if(getIntent().hasExtra("url")){
                intent.putExtra("url",getIntent().getStringExtra("url"));
            }
            startActivity(intent);
           /* AdsUtil.showInterstitialAd(StartActivity.this,new AdListener(){
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    Intent intent = new Intent(StartActivity.this,MyCreationActivity.class);
                    if(getIntent().hasExtra("url")){
                        intent.putExtra("url",getIntent().getStringExtra("url"));
                    }
                    startActivity(intent);
                    AdsUtil.preloadInterstitialAd(StartActivity.this);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    Intent intent = new Intent(StartActivity.this,MyCreationActivity.class);
                    if(getIntent().hasExtra("url")){
                        intent.putExtra("url",getIntent().getStringExtra("url"));
                    }
                    startActivity(intent);
                    AdsUtil.preloadInterstitialAd(StartActivity.this);
                }
            });*/
        }
    }
}
