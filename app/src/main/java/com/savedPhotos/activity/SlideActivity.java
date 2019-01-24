package com.savedPhotos.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.savedPhotos.dialogFragments.AlertDialog;
import com.savedPhotos.adapter.AdapterSlide;
import com.savedPhotos.helper.ResourceManager;
import com.savedPhotos.manager.AdsManager;
import com.savedPhotos.utility.FileUtil;
import com.rsmapps.selfieall.R;
import com.savedPhotos.activity.ShareActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by MDIRFAN on 4/1/17.
 */

public class SlideActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = SlideActivity.class.getSimpleName();

    public static final String STATE_PHOTO_LIST_UPDATED = "photoListUpdated";

    private ImageView imgPreview;
    private ImageView imgSave, imgBack;
    private TextView textView;
    private String path;
    private LinearLayout llyFb, llyWhatsApp, llyHike, llyInstagram, llyMore;

    //
    private static ViewPager mPager;
    private static int currentPage = 0;
    private List<String> arrayList;

    private ImageView ivNextImage, ivShare, ivDelete, ivPrevImage;

    private void getInfo(){
        ivNextImage = findViewById(R.id.ivNextImage);
        ivShare = findViewById(R.id.ivShare);
        ivDelete = findViewById(R.id.ivDelete);
        ivPrevImage = findViewById(R.id.ivPrevImage);
    }

    private void setInfo(){
        initComponents();
        init();

        initBannerAdMOb();

        ivNextImage.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        ivPrevImage.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoarding_slide);

        getInfo();
        setInfo();
    }

    private void loadFiles(){

        try {

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                currentPage = bundle.getInt("pos");
            }

            arrayList = new ArrayList<>();
            File[] files = FileUtil.getFileList(this);
            Log.d("Files", "Size: "+ files.length);

            for (File file : files) {
                Log.d("===Files", "FileName:" + file.getName());
                arrayList.add(file.getAbsolutePath());
            }

            ArrayList<String> reversedList = new ArrayList<>();
            for(int i = 0; i < arrayList.size(); i++){
                reversedList.add(arrayList.get((arrayList.size()-1)-i));
            }
            arrayList.removeAll(arrayList);
            arrayList.addAll(reversedList);

            //arrayList.add("/storage/emulated/0/WhatsApp/Media/WhatsApp Images/IMG-20171128-WA0004.jpg");

//            if(arrayList!=null && arrayList.size()>0){
//                mAdapter.setData(arrayList);
//                recyclerView.setAdapter(mAdapter);
//            }

        }catch(Exception e){
            Log.d("===Exp", e.toString());
        }

    }

    private void initBannerAdMOb(){

        LinearLayout mainLayoutTop = (LinearLayout)findViewById(R.id.mainLayoutTop);
        AdsManager.getAdsManager().loadAdMobBannerAds(this,mainLayoutTop);

        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        AdsManager.getAdsManager().loadAdMobBannerAds(this,mainLayout);
    }

    private void initComponents(){

       // shareText = "\"Download the best Selfie With Celebrity from the following link:\\nhttps://play.google.com/store/apps/details?id=funapps.selfie.celebrity_icon";

        //imgPreview = (ImageView) findViewById(R.id.thumbnail);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            currentPage = bundle.getInt("pos");
            path = bundle.getString("path");
//            Bitmap b = BitmapFactory.decodeFile(path);
//            imgPreview.setImageBitmap(b);
            //imgPreview.setImageBitmap(Bitmap.createScaledBitmap(b, 300, 350, false));
        }

        imgSave = (ImageView) findViewById(R.id.imgSave);
        imgBack = (ImageView) findViewById(R.id.imgCancel);
        textView = (TextView) findViewById(R.id.textView);

        textView.setText(getString(R.string.app_name));

        imgSave.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        imgSave.setVisibility(View.GONE);

        llyFb = (LinearLayout)findViewById(R.id.llyFb);
        llyWhatsApp = (LinearLayout)findViewById(R.id.llyWhatsApp);
        llyHike = (LinearLayout)findViewById(R.id.llyHike);
        llyInstagram = (LinearLayout)findViewById(R.id.llyInstagram);
        llyMore = (LinearLayout)findViewById(R.id.llyMore);

        llyFb.setOnClickListener(this);
        llyWhatsApp.setOnClickListener(this);
        llyHike.setOnClickListener(this);
        llyInstagram.setOnClickListener(this);
        llyMore.setOnClickListener(this);
    }

    private AdapterSlide adapterSlide;
    private CircleIndicator indicator;

    private void init() {


        loadFiles();

//        for(int i=0;i<XMEN.length;i++)
//            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);

        adapterSlide = new AdapterSlide(SlideActivity.this,arrayList);
        mPager.setAdapter(adapterSlide);

        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        mPager.setCurrentItem(currentPage);

       /* // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);*/
    }

    @Override
    public void onBackPressed() {
        finishActivity();
        super.onBackPressed();
    }

    private void deleteFileFromGallery(String path,int position){
        try {
            File fdelete = new File(path);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    System.out.println("file Deleted :" + path);

                    /**
                     * Updating Gallery to after deleting image
                     */
                    UpdateGallery(path);

                    arrayList.remove(position);
                    adapterSlide.notifyDataSetChanged();
                    indicator.setViewPager(mPager);

                    if(arrayList.size() < 1){
                        setResult(RESULT_OK);
                        finishActivity();
                    }


                } else {
                    System.out.println("file not Deleted :" + path);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void UpdateGallery(String filePath) {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filePath))));
    }

    private void finishActivity() {
        Intent dataIntent = new Intent();
        dataIntent.putExtra(STATE_PHOTO_LIST_UPDATED, photoListUpdated);
        setResult(RESULT_OK, dataIntent);
        finish();
    }

    private Boolean photoListUpdated = false;

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ivNextImage:
                if(mPager.getCurrentItem() < arrayList.size()-1)
                    mPager.setCurrentItem(mPager.getCurrentItem()+1,true);
                break;
            case R.id.ivPrevImage:
                if(mPager.getCurrentItem() != 0)
                    mPager.setCurrentItem(mPager.getCurrentItem()-1,true);
                break;
            case R.id.ivShare:
                Intent startShareActivity = new Intent(SlideActivity.this, ShareActivity.class);
                startShareActivity.putExtra("path", arrayList.get(mPager.getCurrentItem()));
                startShareActivity.putExtra("pos", mPager.getCurrentItem());
                startActivity(startShareActivity);
                break;
            case R.id.ivDelete:

                AlertDialog alertDialog = new AlertDialog();

                alertDialog.getAlertDialog(SlideActivity.this, new AlertDialog.OnItemClickListener() {
                    @Override
                    public void onContinueClicked() {
                        photoListUpdated = true;
                        deleteFileFromGallery(arrayList.get(mPager.getCurrentItem()),mPager.getCurrentItem());
                    }
                }).setTitle(getString(R.string.alert_title_warning))
                        .setDescription(getString(R.string.alert_delete_description))
                        .show(getSupportFragmentManager(), AlertDialog.TAG);
                break;
        }

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
        // sharing implementation
        List<Intent> targetedShareIntents = new ArrayList<>();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        // sharingIntent.setType("text/plain");
        sharingIntent.setType("image/png");

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        for (final ResolveInfo app : activityList) {

            String packageName = app.activityInfo.packageName;
            Intent targetedShareIntent = new Intent(Intent.ACTION_SEND);
            targetedShareIntent.setType("image/png");

            if (TextUtils.equals(packageName, "com.twitter.android")) {
                targetedShareIntent.putExtra(Intent.EXTRA_TEXT,
                        "https://play.google.com/store/apps/details?id=funapps.selfie.celebrity");
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
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, ResourceManager.APP_URL);
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
