package com.rsmapps.selfieall.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.adapter.AdapterSlide;
import com.rsmapps.selfieall.helper.ResourceManager;
import com.rsmapps.selfieall.utility.FileUtil;

import butterknife.InjectView;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by MDIRFAN on 4/1/17.
 */

public class SlideActivity extends BaseActivity implements View.OnClickListener{

    @InjectView(R.id.imgSave)
    ImageView imgSave;
    @InjectView(R.id.imgCancel)
    ImageView imgBack;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.llyFb)
    LinearLayout llyFb;
    @InjectView(R.id.llyWhatsApp)
    LinearLayout llyWhatsApp;
    @InjectView(R.id.llyHike)
    LinearLayout llyHike;
    @InjectView(R.id.llyInstagram)
    LinearLayout llyInstagram;
    @InjectView(R.id.llyMore)
    LinearLayout llyMore;

    private String path, shareText;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private List<String> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        initComponents();
        init();
    }

    private void loadFiles(){
        try {
            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                currentPage = bundle.getInt("pos");
            }
            arrayList = new ArrayList<>();
            File[] files = FileUtil.getFileList(this);
            for (int i = 0; i < files.length; i++) {
                arrayList.add(files[i].getAbsolutePath());
            }

            ArrayList<String> reversedList = new ArrayList<>();
            for(int i = 0; i < arrayList.size(); i++){
                reversedList.add(arrayList.get((arrayList.size()-1)-i));
            }
            arrayList.removeAll(arrayList);
            arrayList.addAll(reversedList);
        }catch(Exception e){
            Log.d("===Exp", e.toString());
        }
    }

    private void initComponents(){
        shareText = "\"Download the best Selfie With Celebrity from the following link:\\nhttps://play.google.com/store/apps/details?id="+ResourceManager.PACKAGE_NAME;
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            currentPage = bundle.getInt("pos");
            path = bundle.getString("path");
        }

        textView.setText(getString(R.string.app_name));
        imgSave.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgSave.setVisibility(View.GONE);

        llyFb.setOnClickListener(this);
        llyWhatsApp.setOnClickListener(this);
        llyHike.setOnClickListener(this);
        llyInstagram.setOnClickListener(this);
        llyMore.setOnClickListener(this);
    }

    private void init() {
        loadFiles();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new AdapterSlide(SlideActivity.this,arrayList));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        mPager.setCurrentItem(currentPage);
    }

    @Override
    public void onClick(View v) {
        if(v == imgSave){
            finish();
        }else if(v == imgBack){
            finish();
        }else if(v == llyFb){
            path = arrayList.get(mPager.getCurrentItem());
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.facebook.katana","Facebook");
        }else if(v == llyWhatsApp){
            path = arrayList.get(mPager.getCurrentItem());
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.whatsapp","WhatsApp");
        }else if(v == llyHike){
            path = arrayList.get(mPager.getCurrentItem());
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.bsb.hike","Hike");
        }else if(v == llyInstagram){
            path = arrayList.get(mPager.getCurrentItem());
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.instagram.android","Instagram");
        }else if(v == llyMore){
            path = arrayList.get(mPager.getCurrentItem());
            Uri uri = Uri.fromFile(new File(path));
            shareIt(uri);
        }
    }

    public void shareIt(Uri uri) {
        List<Intent> targetedShareIntents = new ArrayList<>();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/png");
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        for (final ResolveInfo app : activityList) {
            String packageName = app.activityInfo.packageName;
            Intent targetedShareIntent = new Intent(Intent.ACTION_SEND);
            targetedShareIntent.setType("image/png");
            if (TextUtils.equals(packageName, "com.twitter.android")) {
                targetedShareIntent.putExtra(Intent.EXTRA_TEXT,
                        "https://play.google.com/store/apps/details?id="+ ResourceManager.PACKAGE_NAME);
            } else {
                targetedShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                targetedShareIntent.putExtra(Intent.EXTRA_TEXT, "");
            }
            targetedShareIntent.setPackage(packageName);
            targetedShareIntents.add(targetedShareIntent);
        }
        Intent chooserIntent = Intent.createChooser(
                targetedShareIntents.remove(0), "Share Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                targetedShareIntents.toArray(new Parcelable[] {}));
        try {
            startActivity(chooserIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No sharing app  installed.", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, "No sharing app  installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareImage(Uri uri,String packageName, String appName) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, uri);
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        whatsappIntent.setType("image/png");
        whatsappIntent.setPackage(packageName);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, appName+" doesn't installed.", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, appName+" doesn't installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
