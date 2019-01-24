package com.rsmapps.selfieall.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.helper.AppDialog;
import com.rsmapps.selfieall.helper.ResourceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CreatedActivity extends Activity implements View.OnClickListener {

    private LinearLayout llyFb, llyWhatsApp, llyHike, llyInstagram, llyMore;
    private ImageView imgSave, imgCancel,savedImg;
    private String path, shareText;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_created);
        initComponents();

        shareText = "\"Download the best Selfie With Celebrity from the following link:\\nhttps://play.google.com/store/apps/details?id="+ ResourceManager.PACKAGE_NAME;

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            path = bundle.getString("Path");
            Bitmap b = BitmapFactory.decodeFile(path);
            savedImg.setImageBitmap(b);
        }
        new AppDialog(this).show();
    }

    private void initComponents(){
        imgSave = (ImageView) findViewById(R.id.imgSave);
        imgCancel = (ImageView) findViewById(R.id.imgCancel);
        savedImg = (ImageView) findViewById(R.id.savedImg);

        imgSave.setOnClickListener(this);
        imgCancel.setOnClickListener(this);
        savedImg.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        if(v == imgSave){
            Intent intent = new Intent(this,StartActivity.class);
            startActivity(intent);
            finish();
        }else if(v == imgCancel){
            finish();
        }else if(v == llyFb){
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.facebook.katana","Facebook");
        }else if(v == llyWhatsApp){
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.whatsapp","WhatsApp");
        }else if(v == llyHike){
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.bsb.hike","Hike");
        }else if(v == llyInstagram){
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.instagram.android","Instagram");
        }else if(v == llyMore){
            Uri uri = Uri.fromFile(new File(path));
            shareIt(uri);
        }else if(v == savedImg){
            Intent intent = new Intent(CreatedActivity.this,PreviewActivity.class);
            intent.putExtra("path", path);
            startActivity(intent);
        }
    }

    public void shareIt(Uri uri) {
        List<Intent> targetedShareIntents = new ArrayList<>();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/png");

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        for (final ResolveInfo app : activityList) {
            String packageName = app.activityInfo.packageName;
            Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
            targetedShareIntent.setType("image/png");

            if (TextUtils.equals(packageName, "com.twitter.android")) {
                targetedShareIntent.putExtra(Intent.EXTRA_TEXT,
                        "https://play.google.com/store/apps/details?id="+ResourceManager.PACKAGE_NAME);
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
