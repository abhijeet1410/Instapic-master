package com.rsmapps.selfieall.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdView;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.adapter.AdapterMyCreation;
import com.rsmapps.selfieall.helper.AppDialog;
import com.rsmapps.selfieall.utility.FileUtil;
import com.rsmapps.selfieall.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.InjectView;

public class MyCreationActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_READ_PERMISSION = 101;
    public static final String FROM = "from";

    private AdapterMyCreation mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> arrayList;

    @InjectView(R.id.imgSave)
    ImageView imgSave;
    @InjectView(R.id.imgCancel)
    ImageView imgCancel;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_creation);

        imgSave.setOnClickListener(this);
        imgCancel.setOnClickListener(this);

        setupRecyclerView();
        requestStoragePermission();

        //commented on client request
        //AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));

    }

    private void loadFiles() {
        try {
            File[] files = FileUtil.getFileList(this);
            for (int i = 0; i < files.length; i++) {
                arrayList.add(files[i].getAbsolutePath());
            }
            if (arrayList != null && arrayList.size() > 0) {
                Collections.reverse(arrayList);
                mAdapter.setData(arrayList);
                recyclerView.setAdapter(mAdapter);
            }
        } catch (Exception e) {
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        } else {
            if (getIntent().hasExtra(FROM) && getIntent().getStringExtra(FROM).equalsIgnoreCase(MainActivity.class.getSimpleName()))
                new AppDialog(this).show();
            loadFiles();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION && grantResults.length > 0) {
            if (getIntent().hasExtra(FROM) && getIntent().getStringExtra(FROM).equalsIgnoreCase(MainActivity.class.getSimpleName()))
                new AppDialog(this).show();

            boolean granted = true;
            for (Integer integer : grantResults) {
                if (integer != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                }
            }
            if (granted)
                loadFiles();
        }
    }

    private void setupRecyclerView() {
        arrayList = new ArrayList<>();
        mAdapter = new AdapterMyCreation();
        mLayoutManager = new GridLayoutManager(MyCreationActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new AdapterMyCreation.RecyclerTouchListener(this, recyclerView, new AdapterMyCreation.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(MyCreationActivity.this, SlideActivity.class);
                intent.putExtra("path", arrayList.get(position));
                intent.putExtra("pos", position);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == imgSave) {
            onBackPressed();
        } else if (v == imgCancel) {
            onBackPressed();
        }
    }
}
