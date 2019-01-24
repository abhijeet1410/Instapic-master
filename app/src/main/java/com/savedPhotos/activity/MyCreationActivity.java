package com.savedPhotos.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rsmapps.selfieall.activity.MainActivity;
import com.rsmapps.selfieall.helper.AppDialog;
import com.savedPhotos.adapter.AdapterMyCreation;
import com.savedPhotos.manager.AdsManager;
import com.savedPhotos.utility.FileUtil;
import com.google.android.gms.ads.AdView;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;

import java.io.File;
import java.util.ArrayList;


public class MyCreationActivity extends Activity implements View.OnClickListener {

    public static final String TAG = MyCreationActivity.class.getSimpleName();
    public static final String FROM = "from";

    private static final int REQUEST_READ_PERMISSION = 101;
    private static final int RC_SLIDE_ACTIVITY = 102;

    private AdapterMyCreation mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> arrayList = new ArrayList<>();

    private ImageView imgSave, imgCancel;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hoarding_my_creation);

        imgSave = (ImageView) findViewById(R.id.imgSave);
        imgCancel = (ImageView) findViewById(R.id.imgCancel);

        imgSave.setOnClickListener(this);
        imgCancel.setOnClickListener(this);

        if (getIntent().hasExtra(FROM) && getIntent().getStringExtra(FROM).equalsIgnoreCase(MainActivity.TAG))
            new AppDialog(this).show();

        setupRecyclerView();
        requestStoragePermission();

//        initBannerAdMOb();
        AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));
    }

    private void refreshAdapterMyCreation(){
        /**
         * Loading Saved Images from Gallery
         */
        arrayList.removeAll(arrayList);
        requestStoragePermission();
    }

    private void loadFiles(){

        try {
            File[] files = FileUtil.getFileList(this);
            Log.d("Files", "Size: "+ files.length);
            for (File file : files) {
                Log.d("===Files", "FileName:" + file.getName());
                arrayList.add(String.valueOf(Uri.fromFile(file)));
                //                arrayList.add(file.getAbsolutePath());
            }

          /* int f  =  (files.length-1);
            for (int i = f ; i >=0 ; i--) {
                Log.d("===Files", "FileName:" + files[i].getName());
                arrayList.add(files[i].getAbsolutePath());
            }*/

          ArrayList<String> reversedList = new ArrayList<>();
          for(int i = 0; i < arrayList.size(); i++){
              reversedList.add(arrayList.get((arrayList.size()-1)-i));
          }

        if(arrayList!=null && arrayList.size()>0){
            mAdapter.setData(reversedList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }else{
              arrayList = new ArrayList<>();
              mAdapter.setData(arrayList);
              recyclerView.setAdapter(mAdapter);
              mAdapter.notifyDataSetChanged();
              Toast.makeText(this, "No Saved Images", Toast.LENGTH_SHORT).show();
        }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        } else {
            loadFiles();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadFiles();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        /**
         * Checking weather user deleted any photos in SlideActivity
         */
        if(requestCode == RC_SLIDE_ACTIVITY){
            Boolean photoListUpdated = data.getBooleanExtra(SlideActivity.STATE_PHOTO_LIST_UPDATED,false);
            Log.i(TAG, "onActivityResult RC_SLIDE_ACTIVITY-photoListUpdated: " + photoListUpdated);
            if(photoListUpdated)
                refreshAdapterMyCreation();
        }
    }

    private void setupRecyclerView() {
        mAdapter = new AdapterMyCreation(this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        mLayoutManager = new GridLayoutManager(MyCreationActivity.this, 3, GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setHasFixedSize(true);
        //recyclerView.scrollToPosition(0);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new AdapterMyCreation.RecyclerTouchListener(this, recyclerView, new AdapterMyCreation.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                showDeletedDialog(position);
                showSlideActivity(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void showDeletedDialog(final int position){
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
            //alertDialogBuilder.setMessage("Do you want ?");
            alertDialogBuilder.setPositiveButton("Delete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            /*ResourceManager.bitmap.recycle();
                            ResourceManager.bitmap = null;*/
                            //saveHoarding();
                            Log.d("image location :",arrayList.get(position));


                            deleteFileFromGallery(arrayList.get(position),position);
                        }
                    });

            alertDialogBuilder.setNegativeButton("Show", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showSlideActivity(position);
                    //navigateToBack();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void showSlideActivity(int position) {
        Intent intent = new Intent(MyCreationActivity.this,SlideActivity.class);
        try {
            intent.putExtra("path", arrayList.get(position));
            intent.putExtra("pos", position);

            startActivityForResult(intent, RC_SLIDE_ACTIVITY);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Image does not exist", Toast.LENGTH_SHORT).show();

            if(arrayList == null){
                arrayList = new ArrayList<>();
            }else if(arrayList.size() < 1){
                arrayList.removeAll(arrayList);
            }

            mAdapter.setData(arrayList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == imgSave){
            finish();
        }else if(v == imgCancel){
            finish();
        }
    }

    private void deleteFileFromGallery(String path,int position){
        try {
            File fdelete = new File(path);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + path);

                    arrayList.remove(position);
                    mAdapter.notifyItemRemoved(position);

                } else {
                    System.out.println("file not Deleted :" + path);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initBannerAdMOb(){

        LinearLayout mainLayoutTop = (LinearLayout)findViewById(R.id.mainLayoutTop);
        AdsManager.getAdsManager().loadAdMobBannerAds(this,mainLayoutTop);

        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        AdsManager.getAdsManager().loadAdMobBannerAds(this,mainLayout);

        AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));
    }
}
