package com.savedPhotos.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rsmapps.selfieall.utility.Constant;
import com.savedPhotos.helper.ResourceManager;
import com.savedPhotos.utility.FileUtil;
import com.google.android.gms.ads.AdView;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ShareActivity.class.getSimpleName();

    private ImageView ivSelectedImage, ivShareWhatsApp, ivShareInstagram, ivShareMore;
    private LinearLayout llRateIt;

    private void getInfo(){
        /**
         * Use this to show image to be shared
         */
        ivSelectedImage = findViewById(R.id.ivSelectedImage);

        /**
         * To be used as Buttons
         */
        ivShareWhatsApp = findViewById(R.id.ivShareWhatsApp);
        ivShareInstagram = findViewById(R.id.ivShareInstagram);
        ivShareMore = findViewById(R.id.ivShareMore);
        llRateIt = findViewById(R.id.llRateIt);
    }

    private void setInfo(){
        /**
         * Initializing Bottom Banner Ad.
         */
        AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));

        /**
         * Getting All Saved Images from Gallery.
         */
        loadFiles();

        /**
         * Setting ImageView to show Image to be shared.
         */
        Bitmap bitmapToShare = BitmapFactory.decodeFile(photoPaths.get(currentPage));
        ivSelectedImage.setImageBitmap(bitmapToShare);

        /**
         * Setting Click listeners.
         */
        ivShareWhatsApp.setOnClickListener(this);
        ivShareInstagram.setOnClickListener(this);
        ivShareMore.setOnClickListener(this);
        llRateIt.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        getInfo();
        setInfo();
    }

    private String path;
    private int currentPage;
    private Uri photoUri;
    private ArrayList<String> photoPaths = new ArrayList<>();

    private void loadFiles(){

        try {

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                currentPage = bundle.getInt("pos");
            }

            photoPaths = new ArrayList<>();
            File[] files = FileUtil.getFileList(this);
            Log.d("Files", "Size: "+ files.length);
            for (File file : files) {
                Log.d("===Files", "FileName:" + file.getName());
                photoPaths.add(file.getAbsolutePath());
            }

            ArrayList<String> reversedList = new ArrayList<>();
            for(int i = 0; i < photoPaths.size(); i++){
                reversedList.add(photoPaths.get((photoPaths.size()-1)-i));
            }
            photoPaths.removeAll(photoPaths);
            photoPaths.addAll(reversedList);

            //arrayList.add("/storage/emulated/0/WhatsApp/Media/WhatsApp Images/IMG-20171128-WA0004.jpg");

//            if(arrayList!=null && arrayList.size()>0){
//                mAdapter.setData(arrayList);
//                recyclerView.setAdapter(mAdapter);
//            }

        }catch(Exception e){
            Log.d("===Exp", e.toString());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivShareWhatsApp:
                path = photoPaths.get(currentPage);
                photoUri = Uri.fromFile(new File(path));
                shareImage(photoUri,"com.whatsapp","WhatsApp");
                break;
            case R.id.ivShareInstagram:
                path = photoPaths.get(currentPage);
                photoUri = Uri.fromFile(new File(path));
                shareImage(photoUri,"com.instagram.android","Instagram");
                break;
            case R.id.ivShareMore:
                path = photoPaths.get(currentPage);
                photoUri = Uri.fromFile(new File(path));
                shareIt(photoUri);
                break;
            case R.id.llRateIt:
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(Constant.APP_URL)));
                break;
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
}
