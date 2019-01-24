package com.rsmapps.selfieall.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rsmapps.selfieall.helper.ResourceManager;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuPopup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.helper.AppDialog;

import butterknife.InjectView;

/**
 * Created by MDIRFAN on 4/1/17.
 */

public class PreviewActivity extends BaseActivity implements View.OnClickListener, DroppyMenuPopup.OnDismissCallback, DroppyClickCallbackInterface {

    @InjectView(R.id.thumbnail)
    ImageView imgPreview;
    @InjectView(R.id.imgSave)
    ImageView imgSave;
    @InjectView(R.id.imgCancel)
    ImageView imgBack;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.txtViewSavedImg)
    TextView txtViewSavedImg;

    private String path, shareText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initComponents();
        new AppDialog(this).show();
    }

    private void initComponents(){
        StringBuilder builder = new StringBuilder("Download the best Selfie With Celebrity from the following link:");
        builder.append("\n\n");
        builder.append("https://play.google.com/store/apps/details?id="+ ResourceManager.PACKAGE_NAME);
        shareText = "\"Download the best Selfie With Celebrity from the following link:\\nhttps://play.google.com/store/apps/details?id=funapps.selfie.celebrity_icon";
        shareText = builder.toString();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            path = bundle.getString("Path");
            Bitmap b = BitmapFactory.decodeFile(path);
            imgPreview.setImageBitmap(b);
        }

        textView.setText(getString(R.string.app_name));

        imgSave.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtViewSavedImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == imgSave){
            Uri uri = Uri.fromFile(new File(path));
            shareIt(uri);
        }else if(v == imgBack){
            finish();
        }else if(v == txtViewSavedImg){
            Intent intent = new Intent(this,MyCreationActivity.class);
            startActivity(intent);
            finish();
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
                targetedShareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            } else {
                targetedShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                targetedShareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            }
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

    @Override
    public void call(View v, int id) {
        if(id == R.id.droppy1){
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.facebook.katana","Facebook");
        }else if(id == R.id.llyWhatsApp){
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.whatsapp","WhatsApp");
        }else if(id == R.id.llyHike){
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.bsb.hike","Hike");
        }else if(id == R.id.llyInstagram){
            Uri uri = Uri.fromFile(new File(path));
            shareImage(uri,"com.instagram.android","Instagram");
        }else if(id == R.id.llyMore){
            Uri uri = Uri.fromFile(new File(path));
            shareIt(uri);
        }
    }

    @Override
    public void call() {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MyCreationActivity.class);
        startActivity(intent);
        finish();
    }
}
