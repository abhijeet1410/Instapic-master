package com.mirror.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.aligohershabir.photocollage.view.dialogFragments.AlertDialog;
import com.mirror.adapters.EffectAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.mirror.dialogFragments.PhotoSelectDialog;
import com.mirror.utils.BitmapUtils;
import libs.imageCropper.ImageCropperActivity;
import com.mirror.adapters.AdapterHoarding;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.activity.EffectsFilterActivity;
import com.rsmapps.selfieall.activity.MainActivity;
import com.rsmapps.selfieall.helper.ResourceManager;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import sticker.StickerImgView;
import sticker.StickerViewTouchListener;

//import android.util.Log;
//import com.yalantis.ucrop.UCrop;

public class MirrorActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = MirrorActivity.class.getSimpleName();

    private Toolbar tbShapes;

    private boolean isSave = false;

    private ArrayList<String> arrayListFrame;
    private Bitmap bitmap;
    private RecyclerView recyclerView;
    private ImageView img1, img2 , img3,img4 ,img5,img6,img7,img8;
    private View view;
    private Bitmap bitmapOriginal;
    private AdapterHoarding mAdapter;
    private HorizontalScrollView hsv,hsv2,hsv3,hsv4;
    private ScrollView sv1,sv2,sv3,sv4;
    private LinearLayout mirrroLayout2 , layoutVertical,layoutHorizontal;
    private LinearLayout llShapeBackground , llShapeFilter, llMirror,llCrop;
    private static final String CROPPED_IMAGE_NAME = "cropImage";
    private static final int GAUSSIAN_BLUR_RADIUS = 19;
    private static final int GAUSSIAN_BLUR_SIZE = 315;
    public static final String FILE_TYPE_PNG = ".png";

    private StickerImgView stickerView;
    private View mainStickerView;

    private ImageView imgSave;

    private PhotoSelectDialog photoSelectDialog =
            PhotoSelectDialog.getPhotoSelectDialog(new PhotoSelectDialog.OnPhotoSelectDialogResult() {
        @Override
        public void onPhotoSelectDialogResult(Bitmap bitmap, String bitmapType) {
            //sandb.setBitmap(bitmap);
            bitmapOriginal = bitmap;
            setBitmap(bitmapOriginal);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);
        /*
         * Setting Toolbar for Mirror Activity
         */
        initToolbar();

        imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(this);

        mirrroLayout2 = findViewById(R.id.mirrroLayout2);

        layoutVertical = findViewById(R.id.layoutVertical);
        layoutHorizontal = findViewById(R.id.layoutHorizontal);

        sv1 = findViewById(R.id.sv1);
        sv2 = findViewById(R.id.sv2);

        hsv = findViewById(R.id.hsvMirror);
        hsv2 = findViewById(R.id.hsv2);
        hsv3 = findViewById(R.id.hsv3);
        hsv4 = findViewById(R.id.hsv4);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);
        img8 = findViewById(R.id.img8);

        llShapeBackground = findViewById(R.id.llChangeImage);
        llShapeFilter = findViewById(R.id.llShapeFilter);
        llMirror = findViewById(R.id.llMirror);
        llCrop = findViewById(R.id.llCrop);
        mainStickerView = findViewById(R.id.sticker_view);
        llShapeBackground.setOnClickListener(this);
        llShapeFilter.setOnClickListener(this);
        llMirror.setOnClickListener(this);
        llCrop.setOnClickListener(this);

        setupReyclerView();

        initStickerView();
       initView();

        AdsUtil.showBannerAd(MirrorActivity.this, (AdView) findViewById(R.id.banner_adview_bottom));
    }

    private void initToolbar() {
        tbShapes = findViewById(R.id.tbShapes);
        setSupportActionBar(tbShapes);
    }


    private void initStickerView() {
        stickerView = new StickerImgView(this);
        stickerView.setStickerViewTouchListener(new StickerViewTouchListener() {
            @Override
            public void onStickerViewTouchTouch(View v, MotionEvent event) {

            }
        });
    }

    private void setupReyclerView() {
        loadFrame();
        mAdapter = new AdapterHoarding(this,4);
        recyclerView = findViewById(R.id.mirror_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            mAdapter.setData(arrayListFrame,3);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new EffectAdapter.RecyclerTouchListener(this, recyclerView, new EffectAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(MirrorActivity.this,position+"",Toast.LENGTH_SHORT).show();
                makeCustomMirror(position);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void makeCustomMirror(int position) {

        switch (position){
            case 0:
                layoutHorizontal.setVisibility(View.VISIBLE);
                layoutVertical.setVisibility(View.GONE);

                hsv.setScaleX(1f);
                hsv2.setScaleX(-1f);

//                Log.d("height 0 ",hsv.getHeight()+"");
//                Log.d("height measured 0 ",hsv.getMeasuredHeight()+"");

                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(-1f);
                hsv4.setScaleY(1f);

                mirrroLayout2.setVisibility(View.GONE);
                hsv.setVisibility(View.VISIBLE);
                hsv2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.GONE);
                hsv4.setVisibility(View.GONE);

//                Log.d("hsv left :",hsv.getLeft()+"");
//                Log.d("hsv right :",hsv.getRight()+"");
//                Log.d("hsv2 left :",hsv2.getLeft()+"");
//                Log.d("hsv2 right :",hsv2.getRight()+"");


              setSrollPosition(hsv);
              setSrollPosition(hsv2);


                /*sv1.setVisibility(View.VISIBLE);
                sv2.setVisibility(View.VISIBLE);

                sv3.setVisibility(View.GONE);
                sv4.setVisibility(View.GONE);*/


                break;
            case 1:
                layoutHorizontal.setVisibility(View.VISIBLE);
                layoutVertical.setVisibility(View.GONE);

                hsv.setVisibility(View.VISIBLE);
                hsv2.setVisibility(View.VISIBLE);



                hsv.setScaleX(-1f);
                hsv2.setScaleX(1f);

                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(-1f);
                hsv4.setScaleY(1f);

                mirrroLayout2.setVisibility(View.GONE);
                hsv3.setVisibility(View.GONE);
                hsv4.setVisibility(View.GONE);


                setSrollPosition(hsv);
                setSrollPosition(hsv2);


                break;
            case 2:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img5.setScaleX(-1);
                img5.setScaleY(1);
                img7.setScaleX(-1);
                img7.setScaleY(1);
                img6.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);




//                Log.d("sv left :",sv1.getTop()+"");
//                Log.d("sv right :",sv1.getBottom()+"");
//                Log.d("sv2 left :",sv2.getTop()+"");
//                Log.d("sv2 right :",sv2.getBottom()+"");

                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);

                /*hsv2.setVisibility(View.GONE);
                hsv.setScaleX(-1f);
                hsv3.setScaleX(-1f);
                hsv2.setScaleX(1f);
                hsv4.setScaleX(1f);

                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(-1f);
                hsv4.setScaleY(1f);

                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.GONE);*/

                break;
            case 3:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img5.setScaleX(-1);
                img7.setScaleX(-1);
                img5.setScaleY(-1);
                img7.setScaleY(-1);
                img6.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);



                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);



               /* hsv2.setVisibility(View.GONE);
                hsv.setScaleX(-1f);
                hsv3.setScaleX(-1f);
                hsv2.setScaleX(1f);
                hsv4.setScaleX(1f);

                hsv.setScaleY(-1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(1f);
                hsv4.setScaleY(1f);
                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.GONE);*/


                break;
            case 4:

                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img6.setVisibility(View.VISIBLE);
                img8.setVisibility(View.VISIBLE);
                img5.setScaleX(1);
                img6.setScaleX(-1);
                img7.setScaleX(1);
                img8.setScaleX(-1);
                img5.setScaleY(1);
                img7.setScaleY(1);
                img8.setScaleY(1);



                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);
               /* hsv.setScaleX(-1f);
                hsv2.setScaleX(1f);
                Log.d("height 4 ",hsv.getHeight()+"");
                Log.d("height measured 4 ",hsv.getMeasuredHeight()+"");
                hsv3.setScaleX(-1f);
                hsv4.setScaleX(1f);
                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(-1f);
                hsv4.setScaleY(-1f);

                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv.setVisibility(View.VISIBLE);
                hsv2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.VISIBLE);*/



                break;
            case 5:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img6.setVisibility(View.VISIBLE);
                img8.setVisibility(View.VISIBLE);
                img5.setScaleX(-1);
                img6.setScaleX(1);
                img7.setScaleX(-1);
                img8.setScaleX(1);
                img5.setScaleY(1);
                img6.setScaleY(1);
                img7.setScaleY(1);
                img8.setScaleY(1);



                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);
               /* hsv.setScaleX(1f);
                hsv2.setScaleX(-1f);

                hsv3.setScaleX(1f);
                hsv4.setScaleX(-1f);
                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);

                hsv3.setScaleY(-1f);
                hsv4.setScaleY(-1f);

                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv.setVisibility(View.VISIBLE);
                hsv2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.VISIBLE);*/

                break;
            case 6:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img6.setVisibility(View.VISIBLE);
                img8.setVisibility(View.VISIBLE);
                img5.setScaleX(1);
                img6.setScaleX(-1);
                img7.setScaleX(1);
                img8.setScaleX(-1);
                img5.setScaleY(-1);
                img6.setScaleY(-1);
                img7.setScaleY(-1);
                img8.setScaleY(-1);

                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);
               /* hsv.setScaleX(1f);
                hsv2.setScaleX(-1f);

                hsv3.setScaleX(1f);
                hsv4.setScaleX(-1f);
                hsv.setScaleY(-1f);
                hsv2.setScaleY(-1f);

                hsv3.setScaleY(1f);
                hsv4.setScaleY(1f);

                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv.setVisibility(View.VISIBLE);
                hsv2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.VISIBLE);*/


                break;
            case 7:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img6.setVisibility(View.VISIBLE);
                img8.setVisibility(View.VISIBLE);
                img5.setScaleX(-1);
                img6.setScaleX(1);
                img7.setScaleX(-1);
                img8.setScaleX(1);
                img5.setScaleY(-1);
                img6.setScaleY(-1);
                img7.setScaleY(-1);
                img8.setScaleY(-1);



                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);
               /* hsv.setScaleX(-1f);
                hsv2.setScaleX(1f);

                hsv3.setScaleX(-1f);
                hsv4.setScaleX(1f);
                hsv.setScaleY(-1f);
                hsv2.setScaleY(-1f);

                hsv3.setScaleY(1f);
                hsv4.setScaleY(1f);

                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv.setVisibility(View.VISIBLE);
                hsv2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.VISIBLE);*/


                break;
            case 8:
                layoutHorizontal.setVisibility(View.VISIBLE);
                layoutVertical.setVisibility(View.GONE);

                hsv.setVisibility(View.VISIBLE);
                hsv2.setVisibility(View.VISIBLE);

                hsv.setScaleX(-1f);
                hsv2.setScaleX(-1f);

                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(-1f);
                hsv4.setScaleY(1f);

                mirrroLayout2.setVisibility(View.GONE);
                hsv3.setVisibility(View.GONE);
                hsv4.setVisibility(View.GONE);


                setSrollPosition(hsv);
                setSrollPosition(hsv2);

               /* hsv.setScaleX(-1f);
                hsv2.setScaleX(-1f);

                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(-1f);
                hsv4.setScaleY(1f);

                mirrroLayout2.setVisibility(View.GONE);
                hsv.setVisibility(View.VISIBLE);
                hsv2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.GONE);
                hsv4.setVisibility(View.GONE);*/


                break;
            case 9:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img5.setScaleX(-1);
                img5.setScaleY(1);
                img7.setScaleX(-1);
                img7.setScaleY(-1);
                img6.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);


                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);

               /* hsv2.setVisibility(View.GONE);



                hsv.setScaleX(-1f);
                hsv3.setScaleX(-1f);
                hsv2.setScaleX(-1f);
                hsv4.setScaleX(1f);

                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(1f);
                hsv4.setScaleY(1f);
                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.GONE);*/


                break;
            case 10:
                layoutHorizontal.setVisibility(View.VISIBLE);
                layoutVertical.setVisibility(View.GONE);

                hsv.setScaleX(1f);
                hsv2.setScaleX(-1f);

//                Log.d("height 0 ",hsv.getHeight()+"");
//                Log.d("height measured 0 ",hsv.getMeasuredHeight()+"");

                hsv.setScaleY(1f);
                hsv2.setScaleY(-1f);
                hsv3.setScaleY(-1f);
                hsv4.setScaleY(1f);

                mirrroLayout2.setVisibility(View.GONE);
                hsv.setVisibility(View.VISIBLE);
                hsv2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.GONE);
                hsv4.setVisibility(View.GONE);


                setSrollPosition(hsv);
                setSrollPosition(hsv2);
               /* hsv.setScaleX(1f);
                hsv2.setScaleX(-1f);

                hsv.setScaleY(1f);
                hsv2.setScaleY(-1f);
                hsv3.setScaleY(-1f);
                hsv4.setScaleY(1f);

                mirrroLayout2.setVisibility(View.GONE);
                hsv.setVisibility(View.VISIBLE);
                hsv2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.GONE);
                hsv4.setVisibility(View.GONE);*/

               /* sv1.setVisibility(View.VISIBLE);
                sv2.setVisibility(View.VISIBLE);

                sv3.setVisibility(View.GONE);
                sv4.setVisibility(View.GONE);*/
                break;
            case 11:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img5.setScaleX(-1);
                img5.setScaleY(1);
                img7.setScaleX(1);
                img7.setScaleY(1);
                img6.setVisibility(View.GONE);
                img8.setVisibility(View.GONE);



                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);
               /* hsv2.setVisibility(View.GONE);

                hsv.setScaleX(-1f);
                hsv3.setScaleX(1f);
                hsv2.setScaleX(-1f);
                hsv4.setScaleX(1f);

                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(-1f);
                hsv4.setScaleY(1f);
                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.GONE);*/


                break;
            case 12:

                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img6.setVisibility(View.VISIBLE);
                img8.setVisibility(View.VISIBLE);
                img5.setScaleX(1);
                img6.setScaleX(-1);
                img7.setScaleX(1);
                img8.setScaleX(-1);
                img5.setScaleY(1);
                img6.setScaleY(1);
                img7.setScaleY(-1);
                img8.setScaleY(-1);


                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);
               /* hsv2.setVisibility(View.VISIBLE);

                hsv.setScaleX(-1f);
                hsv3.setScaleX(-1f);
                hsv2.setScaleX(1f);
                hsv4.setScaleX(1f);

                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(1f);
                hsv4.setScaleY(1f);
                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.VISIBLE);*/

                break;
            case 13:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img6.setVisibility(View.VISIBLE);
                img8.setVisibility(View.VISIBLE);
                img5.setScaleX(1);
                img6.setScaleX(-1);
                img7.setScaleX(-1);
                img8.setScaleX(1);
                img5.setScaleY(1);
                img7.setScaleY(-1);
                img8.setScaleY(-1);



                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);
               /* hsv2.setVisibility(View.VISIBLE);
                hsv.setScaleX(-1f);
                hsv3.setScaleX(1f);
                hsv2.setScaleX(1f);
                hsv4.setScaleX(-1f);

                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(1f);
                hsv4.setScaleY(1f);
                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.VISIBLE);*/
                break;
            case 14:
                layoutHorizontal.setVisibility(View.GONE);
                layoutVertical.setVisibility(View.VISIBLE);
                img6.setVisibility(View.VISIBLE);
                img8.setVisibility(View.VISIBLE);
                img5.setScaleX(-1);
                img6.setScaleX(1);
                img7.setScaleX(1);
                img8.setScaleX(-1);
                img5.setScaleY(1);
                img7.setScaleY(-1);
                img8.setScaleY(-1);


                setVerticalSrollPosition(sv1);
                setVerticalSrollPosition(sv2);
               /* hsv2.setVisibility(View.VISIBLE);
                hsv3.setScaleX(-1f);
                hsv.setScaleX(1f);
                hsv4.setScaleX(1f);
                hsv2.setScaleX(-1f);

                hsv.setScaleY(1f);
                hsv2.setScaleY(1f);
                hsv3.setScaleY(1f);
                hsv4.setScaleY(1f);
                mirrroLayout2.setVisibility(View.VISIBLE);
                hsv3.setVisibility(View.VISIBLE);
                hsv4.setVisibility(View.VISIBLE);*/


                break;
        }
    }

    private void setSrollPosition(View hsv) {
        try {
            int hsvx;
            if (hsv.getLeft() == 0){
                hsvx = (hsv.getRight()/2)-70;
            }else {
                hsvx = (hsv.getLeft()/2)-70;
            }
//            Log.d("center point :",hsvx+"");
            //(int)hsv.getX() + hsv.getWidth()  / 2;
            hsv.scrollTo(hsvx,0);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setVerticalSrollPosition(View hsv) {
        try {
            //if (hsv.getVisibility() == View.VISIBLE){
                int hsvx;
                if (hsv.getTop() == 0){
                    hsvx = (hsv.getBottom()/2)+50;
                }else {
                    hsvx = (hsv.getTop()/2)+50;
                }
//                Log.d("center point :",hsvx+"");
                //(int)hsv.getX() + hsv.getWidth()  / 2;
                hsv.scrollTo(0,hsvx);
           // }

        }catch (Exception e){
            e.printStackTrace();
        }

       /* try {
            int hsvx;
           hsvx =  (int)(hsv.getY()/2);
            Log.d("center point :",hsvx+"");
            //(int)hsv.getX() + hsv.getWidth()  / 2;
            hsv.scrollTo(hsvx,0);
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    private void loadFrame(){
        //Load Frame
        arrayListFrame = new ArrayList<>();

        for (int i = 1; i <= 15 ; i++) {
            arrayListFrame.add("Frame/m"+ i+".png"+"");
            // Log.d("shape url :",arrayListFrame.get(0)+"");
        }
    }
    private void initView() {
        try {
            String strImagePath;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                strImagePath = bundle.getString("IMG_PATH");
                bitmapOriginal = BitmapFactory.decodeFile(strImagePath);

//                int rotateAngle = BitmapUtil.getCameraPhotoOrientation(this, Uri.parse(strImagePath), strImagePath);
//                bitmapOriginal = BitmapUtil.rotateBitmap(bitmapOriginal, rotateAngle);

                //Resizing Start
                int orignalWidth = bitmapOriginal.getWidth();
                int orignalHeight = bitmapOriginal.getHeight();
                Rectangle orignalRect = new Rectangle();
                Rectangle targetRect = new Rectangle();
                orignalRect.setBounds(0, 0, orignalWidth, orignalHeight);
                targetRect.setBounds(0, 0, 1080, 1920);
                Rectangle output = getScaledDimension(orignalRect, targetRect);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapOriginal, output.width, output.height, true);
                //bitmapOriginal.recycle();
                bitmapOriginal = resizedBitmap;

                setBitmap(bitmapOriginal);
            }else {
                bitmapOriginal = ResourceManager.bitmap;
                setBitmap(bitmapOriginal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        hsv.setOnTouchListener(hsvTouch);


        hsv2.setOnTouchListener(hsv2Touch);

        sv1.setOnTouchListener(svTouch);
        sv2.setOnTouchListener(sv2Touch);



        layoutHorizontal.setVisibility(View.VISIBLE);
        layoutVertical.setVisibility(View.GONE);

        hsv.setScaleX(1f);
        hsv2.setScaleX(-1f);

//        Log.d("height 0 ",hsv.getHeight()+"");
//        Log.d("height measured 0 ",hsv.getMeasuredHeight()+"");

        hsv.setScaleY(1f);
        hsv2.setScaleY(1f);
        hsv3.setScaleY(-1f);
        hsv4.setScaleY(1f);

        mirrroLayout2.setVisibility(View.GONE);
        hsv.setVisibility(View.VISIBLE);
        hsv2.setVisibility(View.VISIBLE);
        hsv3.setVisibility(View.GONE);
        hsv4.setVisibility(View.GONE);


        setSrollPosition(hsv);
        setSrollPosition(hsv2);
    }


    View.OnTouchListener hsv4Touch =  new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //event.setLocation(-1*event.getX(), event.getY());
            //hsv.dispatchTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
//                    Log.d("event2 x :",event.getX()+"");
//                    Log.d("event2 y :",event.getY()+"");

                    // hsv2.scrollTo((int) (-1*event.getX()),(int) (event.getY()));

                    break;
            }
            hsv2.setOnTouchListener(null);
            hsv3.setOnTouchListener(null);
            hsv.setOnTouchListener(null);

            hsv2.dispatchTouchEvent(event);
            hsv3.dispatchTouchEvent(event);
            hsv.dispatchTouchEvent(event);

            hsv2.setOnTouchListener(hsv2Touch);
            hsv3.setOnTouchListener(hsv3Touch);
            hsv.setOnTouchListener(hsvTouch);


            return false;
        }
    };


    View.OnTouchListener hsv3Touch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //event.setLocation(-1*event.getX(), event.getY());
            //hsv.dispatchTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
//                    Log.d("event2 x :",event.getX()+"");
//                    Log.d("event2 y :",event.getY()+"");

                    // hsv2.scrollTo((int) (-1*event.getX()),(int) (event.getY()));

                    break;
            }


            hsv2.setOnTouchListener(null);
            hsv.setOnTouchListener(null);
            hsv4.setOnTouchListener(null);


            hsv2.dispatchTouchEvent(event);
            hsv.dispatchTouchEvent(event);
            hsv4.dispatchTouchEvent(event);

            hsv2.setOnTouchListener(hsv2Touch);
            hsv.setOnTouchListener(hsvTouch);
            hsv4.setOnTouchListener(hsv4Touch);


            return false;
        }
    };


    View.OnTouchListener hsv2Touch =  new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //event.setLocation(-1*event.getX(), event.getY());
            //hsv.dispatchTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
//                    Log.d("event2 x :",event.getX()+"");
//                    Log.d("event2 y :",event.getY()+"");

                    // hsv2.scrollTo((int) (-1*event.getX()),(int) (event.getY()));

                    break;
            }


            hsv.setOnTouchListener(null);
            hsv3.setOnTouchListener(null);
            hsv4.setOnTouchListener(null);


            hsv.dispatchTouchEvent(event);
            hsv3.dispatchTouchEvent(event);
            hsv4.dispatchTouchEvent(event);

            hsv.setOnTouchListener(hsvTouch);
            hsv3.setOnTouchListener(hsv3Touch);
            hsv4.setOnTouchListener(hsv4Touch);


            return false;
        }
    };


  View.OnTouchListener hsvTouch =   new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //event.setLocation(-1*event.getX(), event.getY());
            //hsv.dispatchTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
//                    Log.d("event x :",event.getX()+"");
//                    Log.d("hsv x :",hsv.getX()+"");

                    //hsv.performClick();

                    //event.setLocation( (-1*event.getX()),(-1)*event.getY());

                    // hsv2.scrollTo((int) (-1*event.getX()),(int) (event.getY()));
                    break;
            }

            hsv2.setOnTouchListener(null);
            hsv3.setOnTouchListener(null);
            hsv4.setOnTouchListener(null);

            hsv2.dispatchTouchEvent(event);
            hsv3.dispatchTouchEvent(event);
            hsv4.dispatchTouchEvent(event);

            hsv2.setOnTouchListener(hsv2Touch);
            hsv3.setOnTouchListener(hsv3Touch);
            hsv4.setOnTouchListener(hsv4Touch);


            return false;
        }
    };

  // scrollview touch listener
  View.OnTouchListener svTouch =   new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
          //event.setLocation(-1*event.getX(), event.getY());
          //hsv.dispatchTouchEvent(event);
          switch (event.getAction()) {
              case MotionEvent.ACTION_MOVE:
//                  Log.d("event x :",event.getX()+"");
//                  Log.d("hsv x :",hsv.getX()+"");

                  //hsv.performClick();

                  //event.setLocation( (-1*event.getX()),(-1)*event.getY());

                  // hsv2.scrollTo((int) (-1*event.getX()),(int) (event.getY()));
                  break;
          }

          sv2.setOnTouchListener(null);
          sv2.dispatchTouchEvent(event);
          sv2.setOnTouchListener(sv2Touch);

          return false;
      }
  };


    View.OnTouchListener sv2Touch =   new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //event.setLocation(-1*event.getX(), event.getY());
            //hsv.dispatchTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
//                    Log.d("event x :",event.getX()+"");
//                    Log.d("hsv x :",hsv.getX()+"");

                    //hsv.performClick();

                    //event.setLocation( (-1*event.getX()),(-1)*event.getY());

                    // hsv2.scrollTo((int) (-1*event.getX()),(int) (event.getY()));
                    break;
            }

            sv1.setOnTouchListener(null);
            sv1.dispatchTouchEvent(event);
            sv1.setOnTouchListener(svTouch);

            return false;
        }
    };

    private void setBitmap(Bitmap bitmap) {
        Bitmap bm = bitmap;
        img1.setImageBitmap(bm);
        img2.setImageBitmap(bm);
        img3.setImageBitmap(bm);
        img4.setImageBitmap(bm);

        img5.setImageBitmap(bm);
        img6.setImageBitmap(bm);
        img7.setImageBitmap(bm);
        img8.setImageBitmap(bm);

    }

    public Bitmap resize(Bitmap bitmap) {
        float wd = (float) bitmap.getWidth();
        float he = (float) bitmap.getHeight();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float wr = (float) displaymetrics.widthPixels;
        float hr = (float) (displaymetrics.heightPixels - 300);
        float rat1 = wd / he;
        float rat2 = he / wd;
        if (wd > wr) {
            wd = wr;
            he = wd * rat2;
        } else if (he > hr) {
            he = hr;
            wd = he * rat1;
        } else {
            wd = wr;
            he = wd * rat2;
        }
        return Bitmap.createScaledBitmap(bitmap, (int) wd, (int) he, false);
    }

    public Rectangle getScaledDimension(Rectangle imgSize, Rectangle boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        Rectangle rectangle = new Rectangle();
        rectangle.setBounds(0, 0, new_width, new_height);
        return rectangle;
    }

    public Bitmap flipImage(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if(type == 1) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if(type == 2) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imgSave:
                AdsUtil.showInterstitialAd(MirrorActivity.this, new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        savePhoto();
                        AdsUtil.preloadInterstitialAd(MirrorActivity.this);
                    }
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        savePhoto();
                        AdsUtil.preloadInterstitialAd(MirrorActivity.this);
                    }
                });
                break;
            case R.id.llChangeImage:
                AdsUtil.showInterstitialAd(MirrorActivity.this, new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        openDialog();
                        AdsUtil.preloadInterstitialAd(MirrorActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        openDialog();
                        AdsUtil.preloadInterstitialAd(MirrorActivity.this);
                    }
                });


                break;

            case R.id.llShapeFilter:
                handleFiltersEvent();
//                AdsUtil.showInterstitialAd(MirrorActivity.this, new AdListener() {
//                    @Override
//                    public void onAdClosed() {
//                        super.onAdClosed();
//                        handleFiltersEvent();
//                        changeBackgroundColor(llShapeFilter);
//                        AdsUtil.preloadInterstitialAd(MirrorActivity.this);
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(int i) {
//                        super.onAdFailedToLoad(i);
//                        handleFiltersEvent();
//                        changeBackgroundColor(llShapeFilter);
//                        AdsUtil.preloadInterstitialAd(MirrorActivity.this);
//                    }
//                });


                break;

            case R.id.llMirror:
                changeBackgroundColor(llMirror);
                break;

            case R.id.llCrop:
               changeBackgroundColor(llCrop);
//                startCropActivity(BitmapUtils.getBitmapUri(MirrorActivity.this,ResourceManager.bitmap));
//                startCropActivity(BitmapUtils.getBitmapUri(MirrorActivity.this,bitmapOriginal));
                startCropActivity(bitmapOriginal);
                break;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    private void openDialog() {
        try {
            changeBackgroundColor(llShapeBackground);
            photoSelectDialog.show(getSupportFragmentManager(), PhotoSelectDialog.TAG);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startCropActivity(/*Uri imageUri*/Bitmap bitmap){
//        String destinationFileName = CROPPED_IMAGE_NAME + FILE_TYPE_PNG;
//
//        if(imageUri != null) {
//            File file = new File(getCacheDir(), destinationFileName);
//            UCrop uCrop = UCrop.of(imageUri, Uri.fromFile(file));
//
//            uCrop.start(MirrorActivity.this);
//        }else
//            Log.e("mirror", "Invalid image Uri for Crop." + imageUri);

//        if(imageUri != null) {

        libs.imageCropper.utils.ResourceManager.bitmap = bitmap;
        Intent startImageCropperActivity = new Intent(MirrorActivity.this, ImageCropperActivity.class);
//        startImageCropperActivity.putExtra(ImageCropperActivity.CROPPER_IMAGE_URI,String.valueOf(imageUri));
        startActivityForResult(startImageCropperActivity, ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY);

//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .start(this);
//        }
//            Log.e(TAG, "Invalid image Uri for Crop." + null);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void changeBackgroundColor(LinearLayout l) {
        llShapeBackground.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        llShapeFilter.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        llMirror.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        llCrop.setBackgroundColor(getResources().getColor(android.R.color.transparent));


        l.setBackgroundColor(getResources().getColor(R.color.seekBarBg));
    }

    public static Bitmap overlay(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, new Matrix(), null);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, null);
        return createBitmap;
    }

    private void handleFiltersEvent() {
        Intent intent = new Intent(MirrorActivity.this, EffectsFilterActivity.class);
        // TODO : Image path may not be right...
        try {

            ResourceManager.bitmap = bitmapOriginal;
                //intent.putExtra("IMG_PATH", bundle.getString("IMG_PATH"));
            intent.putExtra("request", MainActivity.TAG);
            startActivityForResult(intent,EffectsFilterActivity.EFFECTS_FILTER_ACTIVITY_OK);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.insta_pic_activity_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.mSave:
//                AdsUtil.showInterstitialAd(MirrorActivity.this, new AdListener() {
//                    @Override
//                    public void onAdClosed() {
//                        super.onAdClosed();
//                        savePhoto();
//                        AdsUtil.preloadInterstitialAd(MirrorActivity.this);
//                    }
//                    @Override
//                    public void onAdFailedToLoad(int i) {
//                        super.onAdFailedToLoad(i);
//                        savePhoto();
//                        AdsUtil.preloadInterstitialAd(MirrorActivity.this);
//                    }
//                });
//                break;
////            case R.id.mShare:
////                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
////                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void savePhoto() {
        /*
         * This line will save Image
         * and display a Toast message
         * "IYour image is saved"
         */
        String path = stickerView.save(MirrorActivity.this,getFinalBitmap(mainStickerView));

        if (path != null){
            isSave = true;
        }
    }

    public Bitmap getFinalBitmap(View v) {
        Bitmap bitmapToSave = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(bitmapToSave);
        v.draw(can);
        return bitmapToSave;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode != RESULT_OK) {
            // For Free-Hand Crop Library
            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = CropImage.getActivityResult(data).getError();
                error.printStackTrace();
            }
            return;
        }

        switch (requestCode) {
//            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:    // New  Crop library
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                Uri resultUri = result.getUri();
//                handleCropResult(resultUri);
//                break;
            case ImageCropperActivity.RC_IMAGE_CROPPER_ACTIVITY:    // New  Crop library
                handleCropResult();
                break;
//            case UCrop.REQUEST_CROP:    // Old  Crop library
//                handleCropResult(data);
//                break;
            case EffectsFilterActivity.EFFECTS_FILTER_ACTIVITY_OK:

                // TODO : Apply Filter changes to current image
                bitmapOriginal = ResourceManager.bitmap;
                setBitmap(ResourceManager.bitmap);
                break;
        }
    }

    private void handleCropResult(){

        try {
            bitmapOriginal = libs.imageCropper.utils.ResourceManager.bitmap;

            Display metrics = getWindowManager().getDefaultDisplay();
            int height = metrics.getHeight();
            int width = metrics.getWidth();

            bitmapOriginal = BitmapUtils.resize(bitmapOriginal, width, height);

            setBitmap(bitmapOriginal);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleCropResult(Uri imageUri){
        if(imageUri == null)
            return;
        String imgPath = imageUri.getPath();

        try {
            bitmapOriginal = BitmapFactory.decodeFile(imgPath);

            int rotateAngle = BitmapUtils.getCameraPhotoOrientation(this, Uri.parse(imgPath), imgPath);
            bitmapOriginal = BitmapUtils.rotateBitmap(bitmapOriginal, rotateAngle);

            Display metrics = getWindowManager().getDefaultDisplay();
            int height = metrics.getHeight();
            int width = metrics.getWidth();

            bitmapOriginal = BitmapUtils.resize(bitmapOriginal, width, height);

            // imgPic.setImageBitmap(bitmap);
            setBitmap(bitmapOriginal);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        if (isSave){
            super.onBackPressed();
        }else {
            AlertDialog alertDialog = new AlertDialog();

            alertDialog.getAlertDialog(MirrorActivity.this, new AlertDialog.OnItemClickListener() {
                @Override
                public void onContinueClicked() {
                    MirrorActivity.super.onBackPressed();
                }
            }).setTitle(getString(R.string.alert_title_warning))
                    .setDescription(getString(R.string.alert_desc_changes_will_be_lost))
                    .setSubtitle(getString(R.string.alert_subtitle_press_exit_or_cancel))
                    .show(getSupportFragmentManager(), com.aligohershabir.photocollage.view.dialogFragments.AlertDialog.TAG);
        }
    }

//    private void handleCropResult(Intent data){
//        if(data == null)
//            return;
//
//        final Uri resultUri = UCrop.getOutput(data);
//
//        if(resultUri == null)
//            return;
//
//        String imgPath = resultUri.getPath();
//
//        try {
//            bitmapOriginal = BitmapFactory.decodeFile(imgPath);
//
//            int rotateAngle = BitmapUtils.getCameraPhotoOrientation(this, Uri.parse(imgPath), imgPath);
//            bitmapOriginal = BitmapUtils.rotateBitmap(bitmapOriginal, rotateAngle);
//
//            Display metrics = getWindowManager().getDefaultDisplay();
//            int height = metrics.getHeight();
//            int width = metrics.getWidth();
//
//            bitmapOriginal = BitmapUtils.resize(bitmapOriginal, width, height);
//
//            // imgPic.setImageBitmap(bitmap);
//            setBitmap(bitmapOriginal);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
