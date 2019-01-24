package com.aligohershabir.photocollage.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aligohershabir.photocollage.R;
import com.aligohershabir.photocollage.adapter.CollageAdapter;
import com.aligohershabir.photocollage.adapter.ColorAdapter;
import com.aligohershabir.photocollage.adapter.EffectAdapter;
import com.aligohershabir.photocollage.adapter.FrameAdapter;
import com.aligohershabir.photocollage.adapter.PatternAdapter;
import com.aligohershabir.photocollage.adapter.ShapeAdapter;
import com.aligohershabir.photocollage.view.dialogFragments.AlertDialog;
import com.aligohershabir.photocollage.view.dialogFragments.CollageOptionsDialog;
import com.aligohershabir.photocollage.view.fragment.CollageDragViewFragment;
import com.aligohershabir.photocollage.model.Color;
import com.aligohershabir.photocollage.model.Grid;
import com.aligohershabir.photocollage.model.GridImage;
import com.aligohershabir.photocollage.utils.AssetUtils;
import com.aligohershabir.photocollage.utils.BitmapUtils;
import com.aligohershabir.photocollage.utils.JsonUtils;
import com.aligohershabir.photocollage.utils.ResourceManager;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.ads.AdListener;
import com.aligohershabir.photocollage.model.Path;
import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity;
import com.aligohershabir.photocollage.utils.AdsUtil;
import com.xiaopo.flying.sticker.StickerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aligohershabir.photocollage.utils.sticker.StickerImgView;

public class CollageActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = CollageActivity.class.getSimpleName();

    public static final int RC_PICK_IMAGES = 100;

    private boolean isSave = false;

    private static final String ASSETS_COLOR_JSON = "color/colors.json";

    private static final int REQUEST_WRITE_PERMISSION = 102;

    public static final int BASE_GRID_FOR_ONE_IMAGE = 0;
    public static final int BASE_GRID_FOR_TWO_IMAGES = 2;
    public static final int BASE_GRID_FOR_THREE_IMAGES = 4;
    public static final int BASE_GRID_FOR_FOUR_IMAGES = 25;
    public static final int BASE_GRID_FOR_FIVE_IMAGES = 26;
    public static final int BASE_GRID_FOR_SIX_IMAGES = 38;
    public static final int BASE_GRID_FOR_SEVEN_IMAGES = 43;
    public static final int BASE_GRID_FOR_EIGHT_IMAGES = 52;
    public static final int BASE_GRID_FOR_NINE_IMAGES = 55;

    private static final int[] GRIDS_FOR_ONE_IMAGES = {0};
    private static final int[] GRIDS_FOR_TWO_IMAGES = {1,2};
    private static final int[] GRIDS_FOR_THREE_IMAGES = {3,4,5,6,7,8,9,10,11,12,13,14};
    private static final int[] GRIDS_FOR_FOUR_IMAGES = {15,16,17,18,19,20,21,22,23,24,25};
    private static final int[] GRIDS_FOR_FIVE_IMAGES = {26,27,28,29,30,31,32,33,34,35,36,37};
    private static final int[] GRIDS_FOR_SIX_IMAGES = {38,39,40,41,42};
    private static final int[] GRIDS_FOR_SEVEN_IMAGES = {43,44,45,46,47,48,49,50};
    private static final int[] GRIDS_FOR_EIGHT_IMAGES = {51,52,53,54};
    private static final int[] GRIDS_FOR_NINE_IMAGES = {55};

    /*
     * Collage Activity Toolbar.
     */
    private Toolbar tbCollage;

    private ImageView ivForeground;

    /*
     * CollageActivity Footer UI-Elements
     */
    private LinearLayout llCollageGrid, llCollageShapes, llCollageBackgournd, llCollageFrames;

    private LinearLayout llRowLayout, llColLayout;

    /*
     * Grid Row Elements
     */
    private LinearLayout llGridRow1, llGridRow2, llGridRow3, llGridRow4;

    /*
     * Grid-Layout Row UI-Elements
     */
    private FrameLayout flGridR1C1, flGridR1C2, flGridR1C3
                        ,flGridR2C1,flGridR2C2,flGridR2C3
                        ,flGridR3C1,flGridR3C2,flGridR3C3
                        ,flGridR4C1,flGridR4C2,flGridR4C3;

    /*
     * Grid Col Elements
     */
    private LinearLayout llGridCol1, llGridCol2, llGridCol3;

    /*
     * Grid-Layout Col UI-Elements
     */
    private FrameLayout flGridC1R1, flGridC1R2, flGridC1R3
            ,flGridC2R1,flGridC2R2,flGridC2R3
            ,flGridC3R1,flGridC3R2,flGridC3R3;

    /*
     * For setting user-selected images(1 to 9).
     */
    private List<Path> images;

    /*
     * Position of selected Image returned from SelectGridTypeActivity
     */
    private int selectedGridId = 0;

    /*
     * Setting UI-Elements for Background Features
     */
    private RecyclerView rvColors, rvPatterns;
    private ImageView ivBackgroundSave, ivBackgroundCancel;
    private View layoutBackgroundChooser;

    /*
     * Setting UI-Elements for Frame-Feature
     */
    private RecyclerView rvFrames;
    private ImageView ivFrameSave, ivFrameCancel;
    private View layoutFrameChooser;

    /*
     * Setting UI-Elements for Grid-Feature
     */
    private RecyclerView rvGrids;
    private TextView tvGridsCancel;
    private LinearLayout llGridsShowAll, llGridsShowShapes;
    private View layoutGridChooser;

    /*
     * Setting UI-Elements for Shapes-Feature
     */
    private RecyclerView rvShapes;
    private TextView tvShapesCancel;
    private ImageView ivShapesStrokeColor;
    private CheckBox cbShapesStroke;
    private LinearLayout llShapesColorPicker;
    private View layoutShapesChooser;

    private RelativeLayout rlMainCollageView;

    /*
     * Transparent drawable for clearing background
     */
    private Drawable transPatternDrawable;

    /**
     * initial shape stroke drawable
     */
    private Drawable colorRedDrawable;

    /*
     * Listeners
     */
    private CompoundButton.OnCheckedChangeListener shapeStrokeCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            /*
             * Setting shape stroke Visibility.
             */
            setShapeStrokeVisibility(isChecked);
        }
    };

    /**
     * To set border color and equal border
     */
    private StickerView sticker_view;

    private ImageView imgSave;

    private void getInfo(){

        imgSave = findViewById(R.id.imgSave);

        sticker_view = findViewById(R.id.sticker_view);

        /*
         * Main layout which will be used to save final
         * image.
         */
        rlMainCollageView = findViewById(R.id.rlMainCollageView);

        /*
         * Getting Collage Activity Toolbar.
         */
        tbCollage = findViewById(R.id.tbCollage);

        ivForeground = findViewById(R.id.ivForeground);

        llRowLayout = findViewById(R.id.llRowLayout);
        llColLayout = findViewById(R.id.llColLayout);

        /*
         * Grid-Layout Rows
         */
        llGridRow1 = findViewById(R.id.llGridRow1);
        llGridRow2 = findViewById(R.id.llGridRow2);
        llGridRow3 = findViewById(R.id.llGridRow3);
        llGridRow4 = findViewById(R.id.llGridRow4);

        /*
         * Getting Row Grid-Layouts
         */
        flGridR1C1 = findViewById(R.id.flGridR1C1);
        flGridR1C2 = findViewById(R.id.flGridR1C2);
        flGridR1C3 = findViewById(R.id.flGridR1C3);

        flGridR2C1 = findViewById(R.id.flGridR2C1);
        flGridR2C2 = findViewById(R.id.flGridR2C2);
        flGridR2C3 = findViewById(R.id.flGridR2C3);

        flGridR3C1 = findViewById(R.id.flGridR3C1);
        flGridR3C2 = findViewById(R.id.flGridR3C2);
        flGridR3C3 = findViewById(R.id.flGridR3C3);

        flGridR4C1 = findViewById(R.id.flGridR4C1);
        flGridR4C2 = findViewById(R.id.flGridR4C2);
        flGridR4C3 = findViewById(R.id.flGridR4C3);

        /*
         * Grid-Layout Cols
         */
        llGridCol1 = findViewById(R.id.llGridCol1);
        llGridCol2 = findViewById(R.id.llGridCol2);
        llGridCol3 = findViewById(R.id.llGridCol3);

        /*
         * Getting Cols Grid-Layouts
         */
        flGridC1R1 = findViewById(R.id.flGridC1R1);
        flGridC1R2 = findViewById(R.id.flGridC1R2);
        flGridC1R3 = findViewById(R.id.flGridC1R3);

        flGridC2R1 = findViewById(R.id.flGridC2R1);
        flGridC2R2 = findViewById(R.id.flGridC2R2);
        flGridC2R3 = findViewById(R.id.flGridC2R3);

        flGridC3R1 = findViewById(R.id.flGridC3R1);
        flGridC3R2 = findViewById(R.id.flGridC3R2);
        flGridC3R3 = findViewById(R.id.flGridC3R3);

        /*
         * Getting UI-Elements for Shapes-Feature
         */
        rvShapes = findViewById(R.id.rvShapes);
        tvShapesCancel = findViewById(R.id.tvShapesCancel);
        ivShapesStrokeColor = findViewById(R.id.ivShapesStrokeColor);
        cbShapesStroke = findViewById(R.id.cbShapesStroke);
        llShapesColorPicker = findViewById(R.id.llShapesColorPicker);
        layoutShapesChooser = findViewById(R.id.layoutShapesChooser);

        /*
         * Getting UI-Elements for Grid-Feature
         */
        rvGrids = findViewById(R.id.rvGrids);

        tvGridsCancel = findViewById(R.id.tvGridsCancel);
        llGridsShowAll = findViewById(R.id.llGridsShowAll);
        llGridsShowShapes = findViewById(R.id.llGridsShowShapes);

        layoutGridChooser = findViewById(R.id.layoutGridChooser);

        /*
         * Getting UI-Elements for Background Feature
         */
        rvColors = findViewById(R.id.rvColors);
        rvPatterns = findViewById(R.id.rvPatterns);

        ivBackgroundSave = findViewById(R.id.ivBackgroundSave);
        ivBackgroundCancel = findViewById(R.id.ivBackgroundCancel);

        layoutBackgroundChooser = findViewById(R.id.layoutBackgroundChooser);

        /*
         * Getting UI-Elements for Frame-Feature
         */
        rvFrames = findViewById(R.id.rvFrames);
        ivFrameSave = findViewById(R.id.ivFrameSave);
        ivFrameCancel = findViewById(R.id.ivFrameCancel);
        layoutFrameChooser = findViewById(R.id.layoutFrameChooser);

        /*
         * Getting CollageActivity Footer UI-Elements
         */
        llCollageGrid = findViewById(R.id.llCollageGrid);
        llCollageShapes = findViewById(R.id.llCollageShapes);
        llCollageBackgournd = findViewById(R.id.llCollageBackgournd);
        llCollageFrames = findViewById(R.id.llCollageFrames);
    }

    private void setInfo(){

        imgSave.setOnClickListener(this);

        /*
         * Using StickerView to save final image
         */
        initStickerView();


        /*
         * Setting UI-Elements for Shape-Feature
         */
        loadShapesData();

        /*
         * Setting UI-Elements for Background-Feature.
         */
        loadPatternData();


        ivBackgroundSave.setOnClickListener(this);
        ivBackgroundCancel.setOnClickListener(this);

        /*
         * Setting UI-Elements for Frame-Feature.
         */
        loadFramesData();

        ivFrameSave.setOnClickListener(this);
        ivFrameCancel.setOnClickListener(this);

        /*
         * Getting user-selected images(1 to 9).
         */
        images = ResourceManager.paths;

        if(images != null && images.size() > 0) {
            initSelectedImageBitmap();
            setFirstGridBasedOnSelectedImagesCount(images.size());
        }else
            Toast.makeText(CollageActivity.this, "No Images Selected", Toast.LENGTH_SHORT).show();

        //Setting Grid-Feature
        loadGridData();


        /*
         * Setting Collage Activity Toolbar.
         */
        setSupportActionBar(tbCollage);

        /*
         * CollageActivity Footer UI-Elements
         */
        llCollageGrid.setOnClickListener(this);
        llCollageShapes.setOnClickListener(this);
        llCollageBackgournd.setOnClickListener(this);
        llCollageFrames.setOnClickListener(this);

        /*
         * etting UI-Elements for for Grid-Menu
         */
        tvGridsCancel.setOnClickListener(this);
        llGridsShowAll.setOnClickListener(this);
        llGridsShowShapes.setOnClickListener(this);

        /*
         * Setting UI-Elements for Shapes-Feature
         */
        tvShapesCancel.setOnClickListener(this);
        cbShapesStroke.setOnCheckedChangeListener(shapeStrokeCheckedChangeListener);
        colorRedDrawable = new ColorDrawable(getResources().getColor(R.color.red2));
        ivShapesStrokeColor.setImageDrawable(colorRedDrawable);
        llShapesColorPicker.setOnClickListener(this);

        /*
         * Setting Color-Part for BackgroundChooserView
         */
//        setColorPickerView();

        /*
         * Setting Pattern-Part for BackgroundChooserView
         */
        setPatternView();

        /*
         * Setting Frame-Feature
         */
        setFrameView();

        /*
         * Setting Shape-Feature
         */
        setShapePickerView();

        collageOptionsDialog = new CollageOptionsDialog().getInstance(CollageActivity.this, new CollageOptionsDialog.OnItemClickedListener() {
            @Override
            public void onEditClicked() {
                // TODO : Use Filter in Hoarding Image Module...
                com.aligohershabir.photocollage.utils.ResourceManager.bitmap = selectedCollageFragment.getBitmap();
                com.aligohershabir.photocollage.utils.ResourceManager.instaPic = true;

                Intent intent = new Intent(CollageActivity.this, EffectsFilterActivity.class);
                startActivityForResult(intent,RQ_EFFECTS_FILTER_ACTIVITY);
            }

            @Override
            public void onChangeClicked() {
                /*
                 * Start Activity with Gallery Intent
                 * to pick new image for selected Grid.
                 */
                requestGalleryPermission();
            }
        });

        /**
         * Setting background of every fragment.
         */
        setFrameBackground(android.R.color.white);
    }

    private void setFrameBackground(int white) {
        llRowLayout.setBackgroundColor(getResources().getColor(white));
        llColLayout.setBackgroundColor(getResources().getColor(white));
        llGridRow4.setBackgroundColor(getResources().getColor(white));
    }

    private void setFirstGridBasedOnSelectedImagesCount(int totalImagesSelected) {

        /*
         * cases represent number of images selected from ImagePickerActivity
         */
        switch (totalImagesSelected){
            case 1:
                handleSelectGridTypeActivityResult(BASE_GRID_FOR_ONE_IMAGE);
                break;
            case 2:
                handleSelectGridTypeActivityResult(BASE_GRID_FOR_TWO_IMAGES);
                break;
            case 3:
                handleSelectGridTypeActivityResult(BASE_GRID_FOR_THREE_IMAGES);
                break;
            case 4:
                handleSelectGridTypeActivityResult(BASE_GRID_FOR_FOUR_IMAGES);
                break;
            case 5:
                handleSelectGridTypeActivityResult(BASE_GRID_FOR_FIVE_IMAGES);
                break;
            case 6:
                handleSelectGridTypeActivityResult(BASE_GRID_FOR_SIX_IMAGES);
                break;
            case 7:
                handleSelectGridTypeActivityResult(BASE_GRID_FOR_SEVEN_IMAGES);
                break;
            case 8:
                handleSelectGridTypeActivityResult(BASE_GRID_FOR_EIGHT_IMAGES);
                break;
            case 9:
                handleSelectGridTypeActivityResult(BASE_GRID_FOR_NINE_IMAGES);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);

        getInfo();

        setInfo();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if(i == R.id.imgSave){
            AdsUtil.showInterstitialAd(CollageActivity.this, new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    saveAfterAdClosed();
                    AdsUtil.preloadInterstitialAd(CollageActivity.this);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    saveAfterAdClosed();
                    AdsUtil.preloadInterstitialAd(CollageActivity.this);
                }
            });
        }

        if (i == R.id.llCollageShapes) {
            handleCollageShapesEvent();

        } else if (i == R.id.llCollageBackgournd) {
            handleCollageBackgroundEvent();

        } else if (i == R.id.llCollageFrames) {
            AdsUtil.showInterstitialAd(CollageActivity.this, new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    handleCollageFramesEvent();
                    AdsUtil.preloadInterstitialAd(CollageActivity.this);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    handleCollageFramesEvent();
                    AdsUtil.preloadInterstitialAd(CollageActivity.this);
                }
            });


        } else if (i == R.id.ivBackgroundSave) {
            AdsUtil.showInterstitialAd(CollageActivity.this, new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    layoutBackgroundChooser.setVisibility(View.GONE);
                    AdsUtil.preloadInterstitialAd(CollageActivity.this);

                }

                @Override
                public void onAdClosed() {
                    super.onAdLoaded();
                    layoutBackgroundChooser.setVisibility(View.GONE);
                    AdsUtil.preloadInterstitialAd(CollageActivity.this);
                }
            });

        } else if (i == R.id.ivBackgroundCancel) {
            clearBackground();
            layoutBackgroundChooser.setVisibility(View.GONE);

        } else if (i == R.id.ivFrameSave) {
            layoutFrameChooser.setVisibility(View.GONE);

        } else if (i == R.id.ivFrameCancel) {
            ivForeground.setImageDrawable(null);
            layoutFrameChooser.setVisibility(View.GONE);

        } else if (i == R.id.llCollageGrid) {
            AdsUtil.showInterstitialAd(CollageActivity.this, new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    handleCollageGridEvent();
                    AdsUtil.preloadInterstitialAd(CollageActivity.this);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    handleCollageGridEvent();
                    AdsUtil.preloadInterstitialAd(CollageActivity.this);
                }
            });


        } else if (i == R.id.tvGridsCancel) {
            layoutGridChooser.setVisibility(View.GONE);

        } else if (i == R.id.llGridsShowAll) {
            Intent intentSelectGridTypeActivity = new Intent(CollageActivity.this, SelectGridTypeActivity.class);
            startActivityForResult(intentSelectGridTypeActivity, SelectGridTypeActivity.RC_SELECT_GRID_TYPE);

        } else if (i == R.id.llGridsShowShapes) {
            handleCollageShapesEvent();

        } else if (i == R.id.tvShapesCancel) {
            layoutShapesChooser.setVisibility(View.GONE);

        } else if (i == R.id.llShapesColorPicker) {
            handleColorPickerDialogEvent();

        }
    }

    private void changeBackgroundColor(LinearLayout l) {
        llCollageGrid.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        llCollageShapes.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        llCollageBackgournd.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        llCollageFrames.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        l.setBackgroundColor(getResources().getColor(R.color.seekBarBg));
    }

    /*
     * Handling CollageActivity Footer UI-Elements
     */
    private void handleCollageGridEvent(){
        if(layoutBackgroundChooser.getVisibility() == View.VISIBLE)
            layoutBackgroundChooser.setVisibility(View.GONE);

        if(layoutShapesChooser.getVisibility() == View.VISIBLE)
            layoutShapesChooser.setVisibility(View.GONE);

        if(layoutFrameChooser.getVisibility() == View.VISIBLE)
            layoutFrameChooser.setVisibility(View.GONE);

        if(layoutGridChooser.getVisibility() == View.VISIBLE)
            layoutGridChooser.setVisibility(View.GONE);
        else {
            changeBackgroundColor(llCollageGrid);
            layoutGridChooser.setVisibility(View.VISIBLE);
        }
    }

    private void handleCollageShapesEvent() {
        if(layoutGridChooser.getVisibility() == View.VISIBLE)
            layoutGridChooser.setVisibility(View.GONE);

        if(layoutFrameChooser.getVisibility() == View.VISIBLE)
            layoutFrameChooser.setVisibility(View.GONE);

        if(layoutBackgroundChooser.getVisibility() == View.VISIBLE)
            layoutBackgroundChooser.setVisibility(View.GONE);

        if(layoutShapesChooser.getVisibility() == View.VISIBLE)
            layoutShapesChooser.setVisibility(View.GONE);
        else {
            changeBackgroundColor(llCollageShapes);
            layoutShapesChooser.setVisibility(View.VISIBLE);
        }
    }

    private void handleCollageFramesEvent() {
        if(layoutGridChooser.getVisibility() == View.VISIBLE)
            layoutGridChooser.setVisibility(View.GONE);

        if(layoutBackgroundChooser.getVisibility() == View.VISIBLE)
            layoutBackgroundChooser.setVisibility(View.GONE);

        if(layoutShapesChooser.getVisibility() == View.VISIBLE)
            layoutShapesChooser.setVisibility(View.GONE);

        if(layoutFrameChooser.getVisibility() == View.VISIBLE)
            layoutFrameChooser.setVisibility(View.GONE);
        else {
            changeBackgroundColor(llCollageFrames);
            layoutFrameChooser.setVisibility(View.VISIBLE);
        }
    }

    private void handleCollageBackgroundEvent() {

        if(layoutGridChooser.getVisibility() == View.VISIBLE)
            layoutGridChooser.setVisibility(View.GONE);

        if(layoutFrameChooser.getVisibility() == View.VISIBLE)
            layoutFrameChooser.setVisibility(View.GONE);

        if(layoutShapesChooser.getVisibility() == View.VISIBLE)
            layoutShapesChooser.setVisibility(View.GONE);

        if(layoutBackgroundChooser.getVisibility() == View.VISIBLE)
            layoutBackgroundChooser.setVisibility(View.GONE);
        else {
            changeBackgroundColor(llCollageBackgournd);
            layoutBackgroundChooser.setVisibility(View.VISIBLE);
        }
    }

    int gridCanHaveImages;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }

        if(requestCode == SelectGridTypeActivity.RC_SELECT_GRID_TYPE){
            if(data != null) {

                selectedGridId = data.getIntExtra(SelectGridTypeActivity.SELECTED_GRID_ID,0);
                gridCanHaveImages = data.getIntExtra(SelectGridTypeActivity.SELECTED_GRID_CAN_HAVE_IMAGES,0);

                /*
                 * Checking if selected grid already exists in GridPickerView
                 * otherwise more/less images are needed to be selected.
                 */
                if(images.size() == gridCanHaveImages){
                    handleSelectGridTypeActivityResult(selectedGridId);
                    layoutGridChooser.setVisibility(View.GONE);
                }else{
                    startMultiImagePickerActivity(gridCanHaveImages);
                }
            }
        }

        /*
         * For MultiImagePickerActivity
         */
        if (requestCode == PickImageActivity.PICKER_REQUEST_CODE && resultCode == RESULT_OK) {

            ///
            ArrayList<String> pathList = new ArrayList<>();
                pathList = data.getExtras().getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
                if (pathList != null && !pathList.isEmpty()) {
                    StringBuilder sb=new StringBuilder("");
                    for(int i=0;i<pathList.size();i++) {
                        sb.append("Photo"+(i+1)+":"+pathList.get(i));
                        sb.append("\n");
                    }
                }
                ArrayList<Path> paths = new ArrayList<>();
                for(String s:pathList){
                    paths.add(new Path(s,false));
                }
                ResourceManager.paths = paths;
            ///

            images = ResourceManager.paths;
            initSelectedImageBitmap();
            if(images.size() != gridCanHaveImages) {

                AlertDialog alertDialog = new AlertDialog();

                alertDialog.getAlertDialog(CollageActivity.this, new AlertDialog.OnItemClickListener() {
                    @Override
                    public void onContinueClicked() {
                        setFirstGridBasedOnSelectedImagesCount(images.size());
                        if (grids != null)
                            setGridPickerView(grids);
                    }
                }).setTitle(getString(R.string.alert_title_warning))
                        .setDescription(getString(R.string.alert_desc_changes_will_be_lost))
                        .setSubtitle(getString(R.string.alert_subtitle_press_exit_or_cancel))
                        .show(getSupportFragmentManager(), com.aligohershabir.photocollage.view.dialogFragments.AlertDialog.TAG);
            }else {

                if (grids != null)
                    setGridPickerView(grids);

                handleSelectGridTypeActivityResult(selectedGridId);
                layoutGridChooser.setVisibility(View.GONE);
            }

           /* images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if(images.size() != gridCanHaveImages) {

                AlertDialog alertDialog = new AlertDialog();

                alertDialog.getAlertDialog(CollageActivity.this, new AlertDialog.OnItemClickListener() {
                    @Override
                    public void onContinueClicked() {
                        setFirstGridBasedOnSelectedImagesCount(images.size());
                        if (grids != null)
                            setGridPickerView(grids);
                    }
                }).setTitle("Warning!")
                        .setDescription("You have selected less images than expected. "
                        + "press CONTINUE to go with new images or CANCEL to discard changes.")
                        .show(getSupportFragmentManager(), com.collage.dialogFragments.AlertDialog.TAG);
            }else {

                if (grids != null)
                    setGridPickerView(grids);

                handleSelectGridTypeActivityResult(selectedGridId);
                layoutGridChooser.setVisibility(View.GONE);
            }*/
        }


        if(requestCode == RQ_EFFECTS_FILTER_ACTIVITY){
            com.aligohershabir.photocollage.utils.ResourceManager.instaPic = false;
            selectedCollageFragment.dragview(com.aligohershabir.photocollage.utils.ResourceManager.bitmap, true);
        }

        if (requestCode == SELECT_GALLERY) {
            try {

                Bitmap bitmapToChange =
                        MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                if(bitmapToChange.getWidth() > BitmapUtils.MAX_IMAGE_RESIZE ||
                        bitmapToChange.getHeight() > BitmapUtils.MAX_IMAGE_RESIZE) {
                    bitmapToChange = BitmapUtils.getResizedBitmap(bitmapToChange, BitmapUtils.MAX_IMAGE_RESIZE);
                }

                for(GridImage gridImage : gridImages){
                    if(gridImage.getId() == selectedCollageFragment.getGridImage().getId()){
                        gridImage.setBitmap(bitmapToChange);
                        gridImage.setCompressed(false);
                    }
                }

                selectedCollageFragment.dragview(bitmapToChange, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void handleSelectGridTypeActivityResult(final int selectedImage) {

        /*
         * Clearing patternDrawable by setting Transparent patternDrawable
         * before setting grid
         */
        clearBackground();

        switch (selectedImage){
            case 0:
                showZeroView();
                break;
            case 1:
                showOneView();
                break;
            case 2:
                showTwoView();
                break;
            case 3:
                showThreeView();
                break;
            case 4:
                showFourView();
                break;
            case 5:
                showFiveView();
                break;
            case 6:
                showSixView();
                break;
            case 7:
                showSevenView();
                break;
            case 8:
                showEightView();
                break;
            case 9:
                showNineView();
                break;
            case 10:
                showTenView();
                break;
            case 11:
                showElevenView();
                break;
            case 12:
                showTwelveView();
                break;
            case 13:
                showThirteenView();
                break;
            case 14:
                showFourteenView();
                break;
            case 15:
                showFifteenView();
                break;
            case 16:
                showSixteenView();
                break;
            case 17:
                showSeventeenView();
                break;
            case 18:
                showEighteenView();
                break;
            case 19:
                showNineteenView();
                break;
            case 20:
                showTwentyView();
                break;
            case 21:
                showTwentyOneView();
                break;
            case 22:
                showTwentyTwoView();
                break;
            case 23:
                showTwentyThreeView();
                break;
            case 24:
                showTwentyFourView();
                break;
            case 25:
                showTwentyFiveView();
                break;
            case 26:
                showTwentySixView();
                break;
            case 27:
                showTwentySevenView();
                break;
            case 28:
                showTwentyEightView();
                break;
            case 29:
                showTwentyNineView();
                break;
            case 30:
                showThirtyView();
                break;
            case 31:
                showThirtyOneView();
                break;
            case 32:
                showThirtyTwoView();
                break;
            case 33:
                showThirtyThreeView();
                break;
            case 34:
                showThirtyFourView();
                break;
            case 35:
                showThirtyFiveView();
                break;
            case 36:
                showThirtySixView();
                break;
            case 37:
                showThirtySevenView();
                break;
            case 38:
                showThirtyEightView();
                break;
            case 39:
                showThirtyNineView();
                break;
            case 40:
                showFortyView();
                break;
            case 41:
                showFortyOneView();
                break;
            case 42:
                showFortyTwoView();
                break;
            case 43:
                showFortyThreeView();
                break;
            case 44:
                showFortyFourView();
                break;
            case 45:
                showFortyFiveView();
                break;
            case 46:
                showFortySixView();
                break;
            case 47:
                showFortySevenView();
                break;
            case 48:
                showFortyEightView();
                break;
            case 49:
                showFortyNineView();
                break;
            case 50:
                showFiftyView();
                break;
            case 51:
                showFiftyOneView();
                break;
            case 52:
                showFiftyTwoView();
                break;
            case 53:
                showFiftyThreeView();
                break;
            case 54:
                showFiftyFourView();
                break;
            case 55:
                showFiftyFiveView();
                break;

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(selectedShape > 0){
                    setShapeForegroundForAllImages(selectedShape);
                }

                if(selectedPattern > 0){
                    setPatternBackgroundForAllFragments(selectedPattern);
                }
            }
        },500);

    }

    /*
     * Settings for Grid-Feature
     */
    private ArrayList<Grid> grids = new ArrayList<>();
    public static String[] oneImageGrid, twoImageGrid, threeImageGrid,
            fourImageGrid,fiveImageGrid,sixImageGrid,sevenImageGrid
            ,eightImageGrid,nineImageGrid;

    private void loadGridData(){
        String[] fileOneImageGrid = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.ONE_IMAGE_GRID);
        String[] fileTwoImageGrid = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.TWO_IMAGE_GRID);
        String[] fileThreeImageGrid = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.THREE_IMAGE_GRID);
        String[] fileFourImageGrid = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.FOUR_IMAGE_GRID);
        String[] fileFiveImageGrid = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.FIVE_IMAGE_GRID);
        String[] fileSixImageGrid = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.SIX_IMAGE_GRID);
        String[] fileSevenImageGrid = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.SEVEN_IMAGE_GRID);
        String[] fileEightImageGrid = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.EIGHT_IMAGE_GRID);
        String[] fileNineImageGrid = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.NINE_IMAGE_GRID);

        oneImageGrid = new String[fileOneImageGrid.length];
        twoImageGrid = new String[fileTwoImageGrid.length];
        threeImageGrid = new String[fileThreeImageGrid.length];
        fourImageGrid = new String[fileFourImageGrid.length];
        fiveImageGrid = new String[fileFiveImageGrid.length];
        sixImageGrid = new String[fileSixImageGrid.length];
        sevenImageGrid = new String[fileSevenImageGrid.length];
        eightImageGrid = new String[fileEightImageGrid.length];
        nineImageGrid = new String[fileNineImageGrid.length];

        int gridId = 0;
        for (int i = 0; i < fileOneImageGrid.length; i++) {
            oneImageGrid[i] = AssetUtils.FILE_ONE_IMAGE_GRID + "/" + fileOneImageGrid[i];
            grids.add(new Grid(gridId,Grid.FileType.ONE_IMAGE_GRID,oneImageGrid[i],AssetUtils.getDrawableFromAsset(this,oneImageGrid[i])));
            gridId++;
        }
        for (int i = 0; i < fileTwoImageGrid.length; i++) {
            twoImageGrid[i] = AssetUtils.FILE_TWO_IMAGE_GRID + "/" + fileTwoImageGrid[i];
            grids.add(new Grid(gridId,Grid.FileType.TWO_IMAGE_GRID,twoImageGrid[i],AssetUtils.getDrawableFromAsset(this,twoImageGrid[i])));
            gridId++;
        }
        for (int i = 0; i < fileThreeImageGrid.length; i++) {
            threeImageGrid[i] = AssetUtils.FILE_THREE_IMAGE_GRID + "/" + fileThreeImageGrid[i];
            grids.add(new Grid(gridId,Grid.FileType.THREE_IMAGE_GRID,threeImageGrid[i],AssetUtils.getDrawableFromAsset(this,threeImageGrid[i])));
            gridId++;
        }
        for (int i = 0; i < fileFourImageGrid.length; i++) {
            fourImageGrid[i] = AssetUtils.FILE_FOUR_IMAGE_GRID + "/" + fileFourImageGrid[i];
            grids.add(new Grid(gridId,Grid.FileType.FOUR_IMAGE_GRID,fourImageGrid[i],AssetUtils.getDrawableFromAsset(this,fourImageGrid[i])));
            gridId++;
        }
        for (int i = 0; i < fileFiveImageGrid.length; i++) {
            fiveImageGrid[i] = AssetUtils.FILE_FIVE_IMAGE_GRID + "/" + fileFiveImageGrid[i];
            grids.add(new Grid(gridId,Grid.FileType.FIVE_IMAGE_GRID,fiveImageGrid[i],AssetUtils.getDrawableFromAsset(this,fiveImageGrid[i])));
            gridId++;
        }
        for (int i = 0; i < fileSixImageGrid.length; i++) {
            sixImageGrid[i] = AssetUtils.FILE_SIX_IMAGE_GRID + "/" + fileSixImageGrid[i];
            grids.add(new Grid(gridId,Grid.FileType.SIX_IMAGE_GRID,sixImageGrid[i],AssetUtils.getDrawableFromAsset(this,sixImageGrid[i])));
            gridId++;
        }
        for (int i = 0; i < fileSevenImageGrid.length; i++) {
            sevenImageGrid[i] = AssetUtils.FILE_SEVEN_IMAGE_GRID + "/" + fileSevenImageGrid[i];
            grids.add(new Grid(gridId,Grid.FileType.SEVEN_IMAGE_GRID,sevenImageGrid[i],AssetUtils.getDrawableFromAsset(this,sevenImageGrid[i])));
            gridId++;
        }
        for (int i = 0; i < fileEightImageGrid.length; i++) {
            eightImageGrid[i] = AssetUtils.FILE_EIGHT_IMAGE_GRID + "/" + fileEightImageGrid[i];
            grids.add(new Grid(gridId,Grid.FileType.EIGHT_IMAGE_GRID,eightImageGrid[i],AssetUtils.getDrawableFromAsset(this,eightImageGrid[i])));
            gridId++;
        }

        for (int i = 0; i < fileNineImageGrid.length; i++) {
            nineImageGrid[i] = AssetUtils.FILE_NINE_IMAGE_GRID + "/" + fileNineImageGrid[i];
            grids.add(new Grid(gridId,Grid.FileType.NINE_IMAGE_GRID,nineImageGrid[i],AssetUtils.getDrawableFromAsset(this,nineImageGrid[i])));
            gridId++;
        }

//        Log.d(TAG, "Grid id not assigned to any grid : --should be total Grids + 1--: " + gridId);
//        Log.d(TAG, "Total grids : " + grids.size());
        setGridPickerView(grids);
    }

    private void setGridPickerView(ArrayList<Grid> grids){
        final ArrayList<Grid> gridsToShow = new ArrayList<>();

        /*
         * Setting required grid images
         */
        gridsToShow.removeAll(gridsToShow);
        for(Grid grid : grids){
            if(grid.getCanHaveImages() == images.size()){
                gridsToShow.add(grid);
            }
        }

        CollageAdapter collageAdapter = new CollageAdapter(CollageActivity.this, gridsToShow, new CollageAdapter.OnItemClickedListener() {
            @Override
            public void onImageClicked(int position) {
                //TODO : Do something with the selected Drawable.
                handleSelectGridTypeActivityResult(gridsToShow.get(position).getGridId());

            }
        },true);
        rvGrids.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvGrids.setAdapter(collageAdapter);
        collageAdapter.notifyDataSetChanged();

    }

    /*
     * Settings for Shapes-Feature
     */
    private int selectedShape = 0;
    public static String[] shape_images, shape_image_effects, shape_stroke_images;
    private void loadShapesData(){
        /*
         * Loading data for shape_images to show user.
         */
        String[] shapeFile = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.SHAPE);
        shape_images = new String[shapeFile.length];

        for (int i = 0; i < shapeFile.length; i++)
            shape_images[i] = AssetUtils.FILE_COLLAGE_SHAPES + "/" + shapeFile[i];

        /*
         * Loading data for shape_image_effects to set fragment.
         */
        String[] shapeEffectsFile = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.SHAPE_EFFECTS);
        shape_image_effects = new String[shapeEffectsFile.length];

        for (int i = 0; i < shapeEffectsFile.length; i++)
            shape_image_effects[i] = AssetUtils.FILE_COLLAGE_SHAPES_EFFECTS + "/" + shapeEffectsFile[i];

        /*
         * Loading data for shape_stroke_images to set fragment.
         */
        String[] shapeStrokeFile = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.SHAPE_STROKE);
        shape_stroke_images = new String[shapeStrokeFile.length];

        for (int i = 0; i < shapeStrokeFile.length; i++)
            shape_stroke_images[i] = AssetUtils.FILE_COLLAGE_SHAPES_STROKE + "/" + shapeStrokeFile[i];

    }

    private void setShapePickerView(){
        ShapeAdapter shapeAdapter = new ShapeAdapter(this);
        rvShapes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvShapes.setAdapter(shapeAdapter);
        shapeAdapter.notifyDataSetChanged();
        rvShapes.addOnItemTouchListener(new EffectAdapter.RecyclerTouchListener(this, rvShapes, new EffectAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                selectedShape = position;

                clearBackground();

                if(position == 0){
                    setShapeForegroundForAllImages(-1);
                }else{
                    // TODO : Do something here to set shape on fragment.
                    setShapeForegroundForAllImages(position);
                }

                if(selectedPattern > 0){
                    setPatternBackgroundForAllFragments(selectedPattern);
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));
    }

    private void setShapeStrokeVisibility(boolean enable){
        for(CollageDragViewFragment collageDragViewFragment : collageDragViewFragments){
            collageDragViewFragment.setEnableStroke(enable);
        }
    }

    private void setShapeForegroundForAllImages(int position) {

        if(position != -1) {

            Drawable shapeDrawable = AssetUtils.getDrawableFromAsset(CollageActivity.this, shape_image_effects[position]);
            Drawable shapeStrokeDrawable = AssetUtils.getDrawableFromAsset(CollageActivity.this, shape_stroke_images[position]);

            Drawable shapeMaskDrawable = AssetUtils.getDrawableFromAsset(CollageActivity.this,shape_mask_images[position]);
            Bitmap shapeMaskBitmap = ((BitmapDrawable)shapeMaskDrawable).getBitmap();

            for (CollageDragViewFragment collageDragViewFragment : collageDragViewFragments) {
                int height = collageDragViewFragment.getRelativeLayout().getHeight();
                int width = collageDragViewFragment.getRelativeLayout().getWidth();

//                Log.i(TAG, "getRelativeLayout Height : " + height);
//                Log.i(TAG, "getRelativeLayout Width : " + width);

//                Drawable shapeDrawableResize =
//                        com.collage.utils.BitmapUtils.resizeDrawable(CollageActivity.this, shapeDrawable, height, width);

                Bitmap colorBitmap = getColorBitmap(width,height, android.R.color.white);
                Bitmap maskedBitmap = makeMaskImage(colorBitmap, shapeMaskBitmap);

                if(selectedShape == 6
                        || selectedShape == 8
                        || selectedShape == 9
                        || selectedShape == 10){

                    Drawable shapeStrokeDrawableResized =
                            BitmapUtils.resizeDrawable(CollageActivity.this, shapeStrokeDrawable,height, width);

                    collageDragViewFragment.setShape(new BitmapDrawable(getResources(), maskedBitmap), shapeStrokeDrawableResized);
                }else{
                    collageDragViewFragment.setShape(new BitmapDrawable(getResources(), maskedBitmap), shapeStrokeDrawable);
                }

//                Drawable strokeDrawableResize =
//                        com.collage.utils.BitmapUtils.resizeDrawable(CollageActivity.this, shapeStrokeDrawable, height, width);

//                collageDragViewFragment.setShape(shapeDrawableResize, shapeStrokeDrawable);

            }
        }else{
            for(CollageDragViewFragment collageDragViewFragment : collageDragViewFragments)
                collageDragViewFragment.setShape(null, null);
        }
    }

    private Bitmap getColorBitmap(int width, int height, int colorId){
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(getResources().getColor(colorId));

        return bitmap;
    }

    private void setShapeStrokeColor(int color){

//        for(FrameLayout frameLayout : frameLayouts){
//            frameLayout.setForeground(com.collage.utils.BitmapUtils
//                    .getStrokeWithColor(CollageActivity.this, color, shapeDrawable, strokeDrawable));
//        }

        for(CollageDragViewFragment collageDragViewFragment : collageDragViewFragments){
            collageDragViewFragment.setStrokeColor(color);
        }
    }

    private void handleColorPickerDialogEvent() {
        ColorPickerDialogBuilder
                .with(CollageActivity.this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .noSliders()
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        ivShapesStrokeColor.setImageDrawable(new ColorDrawable(selectedColor));
                        setShapeStrokeColor(selectedColor);
                    }
                })
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        ivShapesStrokeColor.setImageDrawable(new ColorDrawable(selectedColor));
                        setShapeStrokeColor(selectedColor);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void setShapeBackgroundColor(final int color, Drawable backgroundDrawable){

        final Drawable shapeMaskDrawable = AssetUtils.getDrawableFromAsset(CollageActivity.this,shape_mask_images[selectedShape]);

        for(final CollageDragViewFragment collageDragViewFragment : collageDragViewFragments){

            if(backgroundDrawable != null && color == -1) {

                try {
                    int height = collageDragViewFragment.getRelativeLayout().getHeight();
                    int width = collageDragViewFragment.getRelativeLayout().getWidth();

//                    Log.i(TAG, "getRelativeLayout Height : " + height);
//                    Log.i(TAG, "getRelativeLayout Width : " + width);

                    Bitmap resizedBackgroundBitmap = resizeImage(((BitmapDrawable) backgroundDrawable).getBitmap(), width, height);

                    Bitmap preMaskBitmap = makeMaskImage(resizedBackgroundBitmap,
                            ((BitmapDrawable) shapeMaskDrawable).getBitmap());

                    Drawable finalDrawable = new BitmapDrawable(getResources(), preMaskBitmap);

                    collageDragViewFragment.setShapeBackgroundColor(color, finalDrawable);

                }catch (NullPointerException e){
                    e.printStackTrace();
                }

//                Bitmap patternBitmap = ((BitmapDrawable)com.collage.utils.BitmapUtils.resizeDrawable(CollageActivity.this,
//                        backgroundDrawable, height, width)).getBitmap();

            }else{
                collageDragViewFragment.setShapeBackgroundColor(color,null);
            }
        }
    }

    /**
     * Settings for Background-Feature (Color-Part)
     */
    private ArrayList<Color> colors = new ArrayList<>();
    @Nullable
    private ArrayList<Color> getColorData(){

        String testJson = JsonUtils.loadJSONFromAsset(CollageActivity.this, ASSETS_COLOR_JSON);

        try {
            JSONArray colorJsArr = new JSONArray(testJson);

            Log.i(TAG , colorJsArr.toString(4));

            colors.removeAll(colors);
            ArrayList<Color> localColors = new ArrayList<>();
            for(int i = 0; i < colorJsArr.length(); i++){
                localColors.add(new Color(colorJsArr.getJSONObject(i)));
            }

            return localColors;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    private void setColorPickerView() {
        colors.removeAll(colors);
        colors.addAll(getColorData());
        ColorAdapter colorAdapter = new ColorAdapter(this, colors, new ColorAdapter.OnItemClickedListener() {
            @Override
            public void onColorClicked(int pos, String hex) {
                setBackground(android.R.color.white, hex,null);

                /**
                 * Setting Border Color of StickerView to make border equal
                 * just a work around.
                 */
                if(hex.equals("#00000000"))
                    sticker_view.setBackgroundColor(getResources().getColor(android.R.color.white));

            }
        });
        rvColors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvColors.setAdapter(colorAdapter);
        colorAdapter.notifyDataSetChanged();
    }
    /**
     * Settings for Background-Feature (Pattern-Part)
     */
    private int selectedPattern = 0;
    public static String[] pattern_images, shape_mask_images;
    private void loadPatternData(){
        String[] patternFile = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.PATTERN);
        pattern_images = new String[patternFile.length];
        for (int i = 0; i < patternFile.length; i++) {
            pattern_images[i] = AssetUtils.FILE_PATTERN + "/" + patternFile[i];
        }

        /*
         * Loading data for shape_mask_images.
         */
        String[] shapeMaskFile = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.SHAPE_MASK);
        shape_mask_images = new String[shapeMaskFile.length];

        for (int i = 0; i < shapeMaskFile.length; i++)
            shape_mask_images[i] = AssetUtils.FILE_COLLAGE_SHAPES_MASK + "/" + shapeMaskFile[i];

        /*
         * Transparent drawable for clearing background
         */
        transPatternDrawable = AssetUtils.getDrawableFromAsset(CollageActivity.this, pattern_images[0]);
    }
    private void setPatternView() {
        PatternAdapter patternAdapter = new PatternAdapter(this);
        rvPatterns.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPatterns.setAdapter(patternAdapter);
        patternAdapter.notifyDataSetChanged();
        rvPatterns.addOnItemTouchListener(new EffectAdapter.RecyclerTouchListener(this, rvPatterns, new EffectAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                selectedPattern = position;

                setPatternBackgroundForAllFragments(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void setPatternBackgroundForAllFragments(int position) {
        Drawable patternDrawable = AssetUtils.getDrawableFromAsset(CollageActivity.this, pattern_images[position]);

        /*
         * Clearing patternDrawable by setting Transparent patternDrawable
         * before setting actual patternDrawable
         */
        clearBackground();

        if(position != 0) {
            /**
             * Setting Border Color of StickerView to make border equal
             * just a work around.
             */
            int height = sticker_view.getHeight();
            int width = sticker_view.getWidth();

            final Drawable shapeMaskDrawable = AssetUtils.getDrawableFromAsset(CollageActivity.this, shape_mask_images[selectedShape]);
            Bitmap resizedBackgroundBitmap = resizeImage(((BitmapDrawable) patternDrawable).getBitmap(), width, height);
            Bitmap preMaskBitmap = makeMaskImage(resizedBackgroundBitmap,
                    ((BitmapDrawable) shapeMaskDrawable).getBitmap());
            Drawable finalDrawable = new BitmapDrawable(getResources(), preMaskBitmap);

            sticker_view.setBackground(finalDrawable);
        }

        if(selectedShape == 0){
            for(FrameLayout frameLayout : frameLayouts){
                frameLayout.setBackground(patternDrawable);
            }
        }else {
            setBackground(android.R.color.white, null, patternDrawable);
        }
    }

    private void clearBackground() {

//        Drawable shapeMask = AssetUtils.getDrawableFromAsset(CollageActivity.this,shape_mask_images[0]);

//        Bitmap maskedShape = makeMaskImage(combineTwoBitmaps(((BitmapDrawable)shapeMask).getBitmap(),
//                makeMaskImage(resizeImage(((BitmapDrawable)transPatternDrawable).getBitmap()),
//                        ((BitmapDrawable)shapeMask).getBitmap())), ((BitmapDrawable)shapeMask).getBitmap());


        setBackground(android.R.color.white,null, transPatternDrawable
                /*BitmapUtils.getDrawableFromBimap(CollageActivity.this,maskedShape)*/);
        /**
         * Setting Border Color of StickerView to make border equal
         * just a work around.
         */
        sticker_view.setBackgroundColor(getResources().getColor(android.R.color.white));

    }

    private Bitmap combineTwoBitmaps(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public Bitmap makeMaskImage(Bitmap bitmap, Bitmap bitmap2) {

        int patternBitmapHeight = bitmap.getHeight();
        int patternBitmapWidth = bitmap.getWidth();

        Bitmap shapeBitmapResized;

        if(selectedShape == 6
                || selectedShape == 8
                || selectedShape == 9
                || selectedShape == 10){
            shapeBitmapResized = Bitmap.createScaledBitmap(bitmap2, patternBitmapWidth, patternBitmapHeight, false);
        }else{
            if(patternBitmapHeight > patternBitmapWidth){
                shapeBitmapResized = Bitmap.createScaledBitmap(bitmap2, patternBitmapWidth, patternBitmapWidth, false);
            }else{
                shapeBitmapResized = Bitmap.createScaledBitmap(bitmap2, patternBitmapHeight, patternBitmapHeight,false);
            }
        }

        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);

        int shapeBitmapResizedWidth = shapeBitmapResized.getWidth();
        int shapeBitmapResizedHeight = shapeBitmapResized.getHeight();

        int left = (bitmap.getWidth()-shapeBitmapResizedWidth)/2;
        int top = (bitmap.getHeight()-shapeBitmapResizedHeight)/2;
        canvas.drawBitmap(shapeBitmapResized, left, top, paint);

        paint.setXfermode(null);

        return createBitmap;
    }

    public Bitmap makeMaskImageOld(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        int left = (bitmap.getWidth()-bitmap2.getWidth())/2;
        int top = (bitmap.getHeight()-bitmap2.getHeight())/2;

        canvas.drawBitmap(bitmap2, left, top, paint);

//        canvas.drawBitmap(bitmap2, null, new Rect(left, top, 100, 100), paint);
        paint.setXfermode(null);

        return createBitmap;
    }

    private Bitmap resizeImage(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int height = BitmapFactory.decodeResource(getResources(), R.drawable.fr11).getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, null, new Rect(0, 0, height, height), null);
        return createBitmap;
    }

    private Bitmap resizeImage(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }

        if(width < 1 || height < 1){
//            Log.e(TAG, "Width : " + width + ", Height : " + height);
            return bitmap;
        }

        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, null, new Rect(0, 0, width, height), null);
        return createBitmap;
    }

    private void setBackground(int colorId, String hex, Drawable drawable){
        if(hex != null){

            /**
             * Setting Border Color of StickerView to make border equal
             * just a work around.
             */
            sticker_view.setBackgroundColor(android.graphics.Color.parseColor(hex));

            setBackgroundColor(hex);

            /*
             * Setting backgroundColor
             */
            setShapeBackgroundColor(android.graphics.Color.parseColor(hex),null);

        } else if(drawable != null){

            /*
             * TODO : Set pattern background here for Fragment.
             */
            setShapeBackgroundColor(-1,drawable);

            setBackgroundPattern(drawable);

        }else{

            /**
             * Setting Border Color of StickerView to make border equal
             * just a work around.
             */
            sticker_view.setBackgroundColor(getResources().getColor(colorId));

            setShapeBackgroundColor(getResources().getColor(colorId),null);

            setFrameBackground(colorId);
        }
    }

    private void setBackgroundPattern(Drawable drawable) {
        /*
         * Setting Color for all Row-Cells
         */
        flGridR1C1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR1C2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR1C3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR1C1.setBackground(drawable);
        flGridR1C2.setBackground(drawable);
        flGridR1C3.setBackground(drawable);

        flGridR2C1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR2C2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR2C3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR2C1.setBackground(drawable);
        flGridR2C2.setBackground(drawable);
        flGridR2C3.setBackground(drawable);

        flGridR3C1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR3C2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR3C3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR3C1.setBackground(drawable);
        flGridR3C2.setBackground(drawable);
        flGridR3C3.setBackground(drawable);

        /*
         * Setting Color for all Col-Cells
         */
        flGridC1R1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridC1R2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridC1R3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridC1R1.setBackground(drawable);
        flGridC1R2.setBackground(drawable);
        flGridC1R3.setBackground(drawable);

        flGridC2R1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridC2R2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridC2R3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridC2R1.setBackground(drawable);
        flGridC2R2.setBackground(drawable);
        flGridC2R3.setBackground(drawable);

        flGridC3R1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridC3R2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridC3R3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridC3R1.setBackground(drawable);
        flGridC3R2.setBackground(drawable);
        flGridC3R3.setBackground(drawable);

        /*
         * Setting Color for Bottom Row-Cells
         */
        flGridR4C1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR4C2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR4C3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        flGridR4C1.setBackground(drawable);
        flGridR4C2.setBackground(drawable);
        flGridR4C3.setBackground(drawable);
    }

    private void setBackgroundColor(String hex) {
        /*
         * Setting Color for all Row-Cells
         */
        flGridR1C1.setBackground(null);
        flGridR1C2.setBackground(null);
        flGridR1C3.setBackground(null);
        flGridR1C1.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridR1C2.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridR1C3.setBackgroundColor(android.graphics.Color.parseColor(hex));

        flGridR2C1.setBackground(null);
        flGridR2C2.setBackground(null);
        flGridR2C3.setBackground(null);
        flGridR2C1.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridR2C2.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridR2C3.setBackgroundColor(android.graphics.Color.parseColor(hex));

        flGridR3C1.setBackground(null);
        flGridR3C2.setBackground(null);
        flGridR3C3.setBackground(null);
        flGridR3C1.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridR3C2.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridR3C3.setBackgroundColor(android.graphics.Color.parseColor(hex));

        /*
         * Setting Color for all Col-Cells
         */
        flGridC1R1.setBackground(null);
        flGridC1R2.setBackground(null);
        flGridC1R3.setBackground(null);
        flGridC1R1.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridC1R2.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridC1R3.setBackgroundColor(android.graphics.Color.parseColor(hex));

        flGridC2R1.setBackground(null);
        flGridC2R2.setBackground(null);
        flGridC2R3.setBackground(null);
        flGridC2R1.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridC2R2.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridC2R3.setBackgroundColor(android.graphics.Color.parseColor(hex));

        flGridC3R1.setBackground(null);
        flGridC3R2.setBackground(null);
        flGridC3R3.setBackground(null);
        flGridC3R1.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridC3R2.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridC3R3.setBackgroundColor(android.graphics.Color.parseColor(hex));

        /*
         * Setting Color for Bottom Row-Cells
         */
        flGridR4C1.setBackground(null);
        flGridR4C2.setBackground(null);
        flGridR4C3.setBackground(null);
        flGridR4C1.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridR4C2.setBackgroundColor(android.graphics.Color.parseColor(hex));
        flGridR4C3.setBackgroundColor(android.graphics.Color.parseColor(hex));
    }

    /*
     * Settings for Frame-Feature
     */
    public static String[] frame_images;
    private void loadFramesData(){
        String[] frameFile = AssetUtils.getDataFromAsset(this, AssetUtils.FileType.FRAME);
        frame_images = new String[frameFile.length];
        for (int i = 0; i < frameFile.length; i++) {
            frame_images[i] = AssetUtils.FILE_COLLAGE_FRAMES + "/" + frameFile[i];
        }
    }
    private void setFrameView(){
        FrameAdapter frameAdapter = new FrameAdapter(this);
        rvFrames.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFrames.setAdapter(frameAdapter);
        frameAdapter.notifyDataSetChanged();
        rvFrames.addOnItemTouchListener(new EffectAdapter.RecyclerTouchListener(this, rvFrames, new EffectAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Drawable drawable = AssetUtils.getDrawableFromAsset(CollageActivity.this, frame_images[position]);
                ivForeground.setImageDrawable(drawable);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    /*
     * Settings for Grid-Feature
     */
    private ArrayList<CollageDragViewFragment> collageDragViewFragments = new ArrayList<>();
    private ArrayList<FrameLayout> frameLayouts = new ArrayList<>();

    /*
     * Called if only One(1) image is selected.
     */
    private void showZeroView() {
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);

        inflateGridCellsWithRandomImages();

    }

    private void showOneView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);

        inflateGridCellsWithRandomImages();
    }

    private void showTwoView() {
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);

        /*
         * FrameLayouts to be filled based on number of images selected
         * and Type of grid selected.
         */
        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR2C1);

        inflateGridCellsWithRandomImages();
    }

    private void showThreeView() {
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);

        inflateGridCellsWithRandomImages();

    }

    private void showFourView() {
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);
        llGridRow3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR3C1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR3C1);

        inflateGridCellsWithRandomImages();
    }

    private void showFiveView() {
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);
        ((LinearLayout.LayoutParams) llGridRow2.getLayoutParams()).weight = 0.5f;

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);

        inflateGridCellsWithRandomImages();
    }

    private void showSixView() {
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        ((LinearLayout.LayoutParams) llGridRow1.getLayoutParams()).weight = 0.5f;
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR2C1);

        inflateGridCellsWithRandomImages();
    }

    private void showSevenView() {
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR2C1);

        inflateGridCellsWithRandomImages();
    }

    private void showEightView() {
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol1,0.5f);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridC1R2,0.5f);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);

        inflateGridCellsWithRandomImages();
    }

    private void showNineView() {
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol1,0.5f);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridC2R1,0.5f);
        flGridC2R2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);

        inflateGridCellsWithRandomImages();
    }

    private void showTenView() {
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);
        /*
         * showing required rows.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol2,0.5f);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridC2R1,0.5f);
        flGridC2R2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);

        inflateGridCellsWithRandomImages();
    }

    private void showElevenView() {
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required rows.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol2,0.5f);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridC1R2,0.5f);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);

        inflateGridCellsWithRandomImages();
    }

    private void showTwelveView() {
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);

        inflateGridCellsWithRandomImages();
    }

    private void showThirteenView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required rows.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        llGridCol3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_3.
         */
        flGridC3R1.setVisibility(View.VISIBLE);


        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC3R1);

        inflateGridCellsWithRandomImages();
    }

    private void showFourteenView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required rows.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);

        inflateGridCellsWithRandomImages();
    }

    private void showFifteenView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol2,0.5f);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);

        inflateGridCellsWithRandomImages();
    }

    private void showSixteenView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol1,0.5f);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        flGridC1R3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC1R3);
        frameLayouts.add(flGridC2R1);

        inflateGridCellsWithRandomImages();
    }

    private void showSeventeenView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridRow1,0.5f);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        flGridR1C3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR1C3);
        frameLayouts.add(flGridR2C1);

        inflateGridCellsWithRandomImages();
    }

    private void showEighteenView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridRow2,0.5f);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        flGridR2C3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);
        frameLayouts.add(flGridR2C3);

        inflateGridCellsWithRandomImages();
    }

    private void showNineteenView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol1,0.5f);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);

        inflateGridCellsWithRandomImages();
    }

    private void showTwentyView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridRow1,0.5f);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        flGridR2C3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);
        frameLayouts.add(flGridR2C3);

        inflateGridCellsWithRandomImages();
    }

    private void showTwentyOneView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR1C2,0.5f);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR2C1,0.5f);
        flGridR2C2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);

        inflateGridCellsWithRandomImages();
    }

    private void showTwentyTwoView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR1C1,0.5f);
        flGridR1C2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);

        inflateGridCellsWithRandomImages();
    }

    private void showTwentyThreeView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR1C2,0.5f);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR2C2,0.5f);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);

        inflateGridCellsWithRandomImages();
    }

    private void showTwentyFourView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridRow2,0.5f);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR1C2,0.5f);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR2C2,0.5f);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);

        inflateGridCellsWithRandomImages();
    }

    private void showTwentyFiveView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);

        inflateGridCellsWithRandomImages();
    }

    private void showTwentySixView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        llGridCol3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC3R1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);
        frameLayouts.add(flGridC3R1);

        inflateGridCellsWithRandomImages();
    }

    private void showTwentySevenView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridC1R2,0.5f);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);

        inflateGridCellsWithRandomImages();
    }

    private void showTwentyEightView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        flGridC1R3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridC2R1,0.5f);
        flGridC2R2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC1R3);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);

        inflateGridCellsWithRandomImages();
    }

    private void showTwentyNineView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);

        inflateGridCellsWithRandomImages();
    }

    private void showThirtyView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol1,0.5f);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);

        inflateGridCellsWithRandomImages();
    }

    private void showThirtyOneView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol1,0.5f);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        flGridC1R3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC1R3);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);

        inflateGridCellsWithRandomImages();
    }

    private void showThirtyTwoView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);

        inflateGridCellsWithRandomImages();
    }

    private void showThirtyThreeView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        flGridC1R3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC1R3);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);

        inflateGridCellsWithRandomImages();
    }

    private void showThirtyFourView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        flGridR2C3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);
        frameLayouts.add(flGridR2C3);

        inflateGridCellsWithRandomImages();
    }

    private void showThirtyFiveView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        flGridR1C3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR1C3);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);

        inflateGridCellsWithRandomImages();
    }

    private void showThirtySixView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridRow1,0.5f);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        flGridR2C3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);
        frameLayouts.add(flGridR2C3);

        inflateGridCellsWithRandomImages();
    }

    private void showThirtySevenView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridRow2,0.5f);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        flGridR1C3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR1C3);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);

        inflateGridCellsWithRandomImages();
    }

    private void showThirtyEightView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        flGridR1C3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        flGridR2C3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR1C3);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);
        frameLayouts.add(flGridR2C3);

        inflateGridCellsWithRandomImages();
    }

    private void showThirtyNineView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);
        llGridRow4.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridRow4, 0.5f);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol2,0.5f);

        /*
         * showing required cols from row_2.
         */
        flGridC1R1.setVisibility(View.VISIBLE);

        /*
         *
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from row_4.
         */
        flGridR4C1.setVisibility(View.VISIBLE);
        flGridR4C2.setVisibility(View.VISIBLE);
        flGridR4C3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridR4C1);
        frameLayouts.add(flGridR4C2);
        frameLayouts.add(flGridR4C3);

        inflateGridCellsWithRandomImages();
    }

    private void showFortyView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);
        llGridRow4.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridRow4, 0.5f);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol1,0.5f);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Row_4.
         */
        flGridR4C1.setVisibility(View.VISIBLE);
        flGridR4C2.setVisibility(View.VISIBLE);
        flGridR4C3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridR4C1);
        frameLayouts.add(flGridR4C2);
        frameLayouts.add(flGridR4C3);

        inflateGridCellsWithRandomImages();
    }

    private void showFortyOneView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);
        setLayoutWeight(llRowLayout, 0.5f);
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        flGridR1C3.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        setLayoutWeight(llGridCol1, 0.5f);
        llGridCol2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from col_1
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from col_2
         */
        flGridC2R1.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR1C3);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);

        inflateGridCellsWithRandomImages();
    }

    private void showFortyTwoView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        flGridR1C3.setVisibility(View.VISIBLE);

        /*
         * showing required cols from row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        flGridR2C3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR1C3);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);
        frameLayouts.add(flGridR2C3);

        inflateGridCellsWithRandomImages();
    }

    private void showFortyThreeView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Rows.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        llGridCol3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        flGridC1R3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC3R1.setVisibility(View.VISIBLE);
        flGridC3R2.setVisibility(View.VISIBLE);
        flGridC3R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC1R3);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC3R1);
        frameLayouts.add(flGridC3R2);
        frameLayouts.add(flGridC3R3);

        inflateGridCellsWithRandomImages();
    }

    private void showFortyFourView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Rows.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        llGridCol3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC3R1.setVisibility(View.VISIBLE);
        flGridC3R2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);
        frameLayouts.add(flGridC3R1);
        frameLayouts.add(flGridC3R2);

        inflateGridCellsWithRandomImages();
    }

    private void showFortyFiveView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);
        llGridRow3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        flGridR2C3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridR3C1.setVisibility(View.VISIBLE);
        flGridR3C2.setVisibility(View.VISIBLE);
        flGridR3C3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);
        frameLayouts.add(flGridR2C3);
        frameLayouts.add(flGridR3C1);
        frameLayouts.add(flGridR3C2);
        frameLayouts.add(flGridR3C3);

        inflateGridCellsWithRandomImages();
    }

    private void showFortySixView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);
        llGridRow3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        flGridR1C3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridR3C1.setVisibility(View.VISIBLE);
        flGridR3C2.setVisibility(View.VISIBLE);
        flGridR3C3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR1C3);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR3C1);
        frameLayouts.add(flGridR3C2);
        frameLayouts.add(flGridR3C3);

        inflateGridCellsWithRandomImages();
    }

    private void showFortySevenView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);
        llGridRow3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        flGridR1C3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR2C2, 0.5f);

        /*
         * showing required cells from Col_1.
         */
        flGridR3C1.setVisibility(View.VISIBLE);
        flGridR3C2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR3C2, 0.5f);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR1C3);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);
        frameLayouts.add(flGridR3C1);
        frameLayouts.add(flGridR3C2);

        inflateGridCellsWithRandomImages();
    }

    private void showFortyEightView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);
        llGridRow3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        flGridR1C2.setVisibility(View.VISIBLE);
        flGridR1C3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridR3C1.setVisibility(View.VISIBLE);
        flGridR3C2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR1C3);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);
        frameLayouts.add(flGridR3C1);
        frameLayouts.add(flGridR3C2);

        inflateGridCellsWithRandomImages();
    }

    private void showFortyNineView(){
        resetAllViews();
        llRowLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Rows.
         */
        llGridRow1.setVisibility(View.VISIBLE);
        llGridRow2.setVisibility(View.VISIBLE);
        llGridRow3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Row_1.
         */
        flGridR1C1.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR1C1, 0.5f);
        flGridR1C2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Row_2.
         */
        flGridR2C1.setVisibility(View.VISIBLE);
        flGridR2C2.setVisibility(View.VISIBLE);
        flGridR2C3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Row_3.
         */
        flGridR3C1.setVisibility(View.VISIBLE);
        flGridR3C2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridR3C2, 0.5f);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridR1C1);
        frameLayouts.add(flGridR1C2);
        frameLayouts.add(flGridR2C1);
        frameLayouts.add(flGridR2C2);
        frameLayouts.add(flGridR2C3);
        frameLayouts.add(flGridR3C1);
        frameLayouts.add(flGridR3C2);

        inflateGridCellsWithRandomImages();
    }

    private void showFiftyView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        llGridCol3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        flGridC1R3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridC2R2, 0.5f);

        /*
         * showing required cells from Col_3.
         */
        flGridC3R1.setVisibility(View.VISIBLE);
        flGridC3R2.setVisibility(View.VISIBLE);
        setLayoutWeight(flGridC3R2, 0.5f);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC1R3);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC3R1);
        frameLayouts.add(flGridC3R2);

        inflateGridCellsWithRandomImages();
    }

    private void showFiftyOneView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        llGridCol3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_3.
         */
        flGridC3R1.setVisibility(View.VISIBLE);
        flGridC3R2.setVisibility(View.VISIBLE);
        flGridC3R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);
        frameLayouts.add(flGridC3R1);
        frameLayouts.add(flGridC3R2);
        frameLayouts.add(flGridC3R3);

        inflateGridCellsWithRandomImages();
    }

    private void showFiftyTwoView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        llGridCol3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        flGridC1R3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_3.
         */
        flGridC3R1.setVisibility(View.VISIBLE);
        flGridC3R2.setVisibility(View.VISIBLE);
        flGridC3R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC1R3);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC3R1);
        frameLayouts.add(flGridC3R2);
        frameLayouts.add(flGridC3R3);

        inflateGridCellsWithRandomImages();
    }

    private void showFiftyThreeView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        llGridCol3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        flGridC1R3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_3.
         */
        flGridC3R1.setVisibility(View.VISIBLE);
        flGridC3R2.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC1R3);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);
        frameLayouts.add(flGridC3R1);
        frameLayouts.add(flGridC3R2);

        inflateGridCellsWithRandomImages();
    }

    private void showFiftyFourView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        llGridCol3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_3.
         */
        flGridC3R1.setVisibility(View.VISIBLE);
        flGridC3R2.setVisibility(View.VISIBLE);
        flGridC3R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);
        frameLayouts.add(flGridC3R1);
        frameLayouts.add(flGridC3R2);
        frameLayouts.add(flGridC3R3);

        inflateGridCellsWithRandomImages();
    }

    private void showFiftyFiveView(){
        resetAllViews();
        llColLayout.setVisibility(View.VISIBLE);

        /*
         * showing required Cols.
         */
        llGridCol1.setVisibility(View.VISIBLE);
        llGridCol2.setVisibility(View.VISIBLE);
        llGridCol3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_1.
         */
        flGridC1R1.setVisibility(View.VISIBLE);
        flGridC1R2.setVisibility(View.VISIBLE);
        flGridC1R3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_2.
         */
        flGridC2R1.setVisibility(View.VISIBLE);
        flGridC2R2.setVisibility(View.VISIBLE);
        flGridC2R3.setVisibility(View.VISIBLE);

        /*
         * showing required cells from Col_3.
         */
        flGridC3R1.setVisibility(View.VISIBLE);
        flGridC3R2.setVisibility(View.VISIBLE);
        flGridC3R3.setVisibility(View.VISIBLE);

        frameLayouts.removeAll(frameLayouts);
        frameLayouts.add(flGridC1R1);
        frameLayouts.add(flGridC1R2);
        frameLayouts.add(flGridC1R3);
        frameLayouts.add(flGridC2R1);
        frameLayouts.add(flGridC2R2);
        frameLayouts.add(flGridC2R3);
        frameLayouts.add(flGridC3R1);
        frameLayouts.add(flGridC3R2);
        frameLayouts.add(flGridC3R3);

        inflateGridCellsWithRandomImages();
    }

    private static final int RQ_EFFECTS_FILTER_ACTIVITY = 109;
    private CollageDragViewFragment selectedCollageFragment;
    private CollageOptionsDialog collageOptionsDialog;
    private CollageDragViewFragment.OnItemClickListener onItemClickListener = new CollageDragViewFragment.OnItemClickListener() {
        @Override
        public void onDoubleClicked(CollageDragViewFragment fragment) {
            selectedCollageFragment = fragment;

//            Log.i(TAG, "onDoubleClicked: " + "onDoubleClicked -CollageDragViewFragment-");

            // TODO : Show pop-up here for each fragment. (Edit/Change/Cancel)
            try {
                collageOptionsDialog.show(getSupportFragmentManager(), CollageOptionsDialog.TAG);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    private ArrayList<GridImage> gridImages = new ArrayList<>();
//    private ArrayList<Bitmap> selectedImageBitmaps = new ArrayList<>();

    private void initSelectedImageBitmap(){

        /*
         * Removing any bitmaps if already in the arraylist
         */
        clearSelectedImageBitmaps();


        int id = 1;
        for(Path path : images){

            Bitmap inSampleSizeBitmap = BitmapUtils.getInSampleSizedBitmapFromPath(path,
                    BitmapUtils.MAX_IMAGE_SIZE);

            gridImages.add(new GridImage(id,inSampleSizeBitmap, false));

//            Log.e(TAG, "initSelectedImageBitmap ===options.outMimeType : " + options.outMimeType);

//            Bitmap finalBitmap = BitmapUtils.getCompressedBitmap(inSampleSizeBitmap, BitmapUtils.MAX_IMAGE_QUALITY);

//            if(options.outWidth > BitmapUtils.MAX_IMAGE_SIZE || options.outHeight> BitmapUtils.MAX_IMAGE_SIZE){
//                gridImages.add(new GridImage(id,BitmapUtils.getResizedBitmap(finalBitmap , BitmapUtils.MAX_IMAGE_SIZE)));
//                selectedImageBitmaps.add(getResizedBitmap(finalBitmap , MAX_IMAGE_SIZE)));
//            }else{
//                gridImages.add(new GridImage(id,finalBitmap));
//                selectedImageBitmaps.add(finalBitmap);
//            }

            /*
             * To set Image id for option to change selected
             * image by user.
             */
            id++;

        }

    }

    private void clearSelectedImageBitmaps() {

        if(gridImages.size() > 0){
            gridImages.removeAll(gridImages);
        }
//        if(selectedImageBitmaps.size() > 0) {
//            selectedImageBitmaps.removeAll(selectedImageBitmaps);
//        }
    }

    private void inflateGridCellsWithRandomImages() {

        collageDragViewFragments.removeAll(collageDragViewFragments);
        if(images != null) {

            /*
             * Setting CollageDragViewFragment by passing selected image path.
             */
            for(int i = 0; i < images.size(); i++){
                collageDragViewFragments.add(new CollageDragViewFragment()
                        .getInstance(onItemClickListener)
//                        .setBitmap(selectedImageBitmaps.get(i))
                        .setGridImage(gridImages.get(i))
                );
            }

        }else{
            startMultiImagePickerActivity(9);
        }

        loadFragments();

    }

    private void loadFragments() {
        /*
         * Clearing any images if previously set
         */
//        for(FrameLayout frameLayout : frameLayouts){
//            frameLayout.removeAllViews();
//        }

        /*
         * Showing CollageDragViewFragment in Visible Grid-Cell.
         */
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        for(int i = 0; i < frameLayouts.size();i++){

//            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
            ft.replace(frameLayouts.get(i).getId(), collageDragViewFragments.get(i), CollageDragViewFragment.TAG);

            /*
             * Setting Shape size in Fragment.
             */
            collageDragViewFragments.get(i).setContainer(frameLayouts.get(i));
        }
        ft.commit();


//        for(CollageDragViewFragment fragment = collageDragViewFragments){
//            fragment.getGridImage().getBitmap()
//        }
    }

    private void resetAllViews(){
        llRowLayout.setVisibility(View.GONE);
        /*
         * Hiding all Rows.
         */
        llGridRow1.setVisibility(View.GONE);
        setLayoutWeight(llGridRow1,1f);
        llGridRow2.setVisibility(View.GONE);
        setLayoutWeight(llGridRow2,1f);
        llGridRow3.setVisibility(View.GONE);
        setLayoutWeight(llGridRow3,1f);
        llGridRow4.setVisibility(View.GONE);
        setLayoutWeight(llGridRow4,1f);
        /*
         * Hiding extra cols from row_1.
         */
        flGridR1C1.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR1C1.getLayoutParams()).weight = 1f;
        flGridR1C2.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR1C2.getLayoutParams()).weight = 1f;
        flGridR1C3.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR1C3.getLayoutParams()).weight = 1f;
        /*
         * Hiding extra cols from row_2.
         */
        flGridR2C1.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR2C1.getLayoutParams()).weight = 1f;
        flGridR2C2.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR2C2.getLayoutParams()).weight = 1f;
        flGridR2C3.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR2C3.getLayoutParams()).weight = 1f;
        /*
         * Hiding extra cols from row_3.
         */
        flGridR3C1.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR3C1.getLayoutParams()).weight = 1f;
        flGridR3C2.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR3C2.getLayoutParams()).weight = 1f;
        flGridR3C3.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR3C3.getLayoutParams()).weight = 1f;
        /*
         * Hiding extra cols from row_4.
         */
        flGridR4C1.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR4C1.getLayoutParams()).weight = 1f;
        flGridR4C2.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR4C2.getLayoutParams()).weight = 1f;
        flGridR4C3.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridR4C3.getLayoutParams()).weight = 1f;
        ///////////////////////////////////////

        llColLayout.setVisibility(View.GONE);

        /*
         * Hiding all Cols.
         */
        llGridCol1.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) llGridCol1.getLayoutParams()).weight = 1f;
        llGridCol2.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) llGridCol2.getLayoutParams()).weight = 1f;
        llGridCol3.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) llGridCol3.getLayoutParams()).weight = 1f;

        /*
         * hiding all cells in col_1
         */
        flGridC1R1.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridC1R1.getLayoutParams()).weight = 1f;
        flGridC1R2.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridC1R2.getLayoutParams()).weight = 1f;
        flGridC1R3.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridC1R3.getLayoutParams()).weight = 1f;
        /*
         * hiding all cells in col_2
         */
        flGridC2R1.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridC2R1.getLayoutParams()).weight = 1f;
        flGridC2R2.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridC2R2.getLayoutParams()).weight = 1f;
        flGridC2R3.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridC2R3.getLayoutParams()).weight = 1f;
        /*
         * hiding all cells in col_3
         */
        flGridC3R1.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridC3R1.getLayoutParams()).weight = 1f;
        flGridC3R2.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridC3R2.getLayoutParams()).weight = 1f;
        flGridC3R3.setVisibility(View.GONE);
        ((LinearLayout.LayoutParams) flGridC3R3.getLayoutParams()).weight = 1f;
    }

    private void setLayoutWeight(View llView, float weight) {
        ((LinearLayout.LayoutParams) llView.getLayoutParams()).weight = weight;
    }

    private void startMultiImagePickerActivity(int noOfImagesToSelect){
        Intent mIntent = new Intent(this, PickImageActivity.class);
        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, noOfImagesToSelect);
        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
        startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);
    }

    /**
     * This will save image after 2-seconds so image is saved correctly.
     */
    private void saveAfterAdClosed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestStoragePermission();
            }
        }, 2*1000);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if(item.getItemId() == R.id.mSave){
//
//            AdsUtil.showInterstitialAd(CollageActivity.this, new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    saveAfterAdClosed();
//                    AdsUtil.preloadInterstitialAd(CollageActivity.this);
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                    saveAfterAdClosed();
//                    AdsUtil.preloadInterstitialAd(CollageActivity.this);
//                }
//            });
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.insta_pic_activity_menu,menu);
//        return true;
//    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            savePhoto();
        }
    }

    private static final int REQUEST_READ_PERMISSION = 101;
    private static final int SELECT_GALLERY = 201;
    private static final String[] GALLERY_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private void requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(GALLERY_PERMISSIONS, REQUEST_READ_PERMISSION);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent select = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(select, "Select Picture"), SELECT_GALLERY);
    }

    private StickerImgView stickerView;

    private void initStickerView() {
        stickerView = new StickerImgView(this);
//        stickerView.setStickerViewTouchListener(this);
    }

    private void savePhoto() {

        hideMenus();
        stickerView.save(CollageActivity.this,getFinalBitmap(rlMainCollageView));
        isSave = true;

//        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);
//        alertDialogBuilder.setMessage("Do you want to save?");
//        alertDialogBuilder.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//
//                        try {
//                            String path = stickerView.save(CollageActivity.this,getFinalBitmap(rlMainCollageView));
//                            isSave = true;
////                            if (path != null) {
////                                Intent intent = new Intent(CollageActivity.this, PreviewActivity.class);
////                                intent.putExtra("Path", path);
////                                startActivity(intent);
////                                finish();
////                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        /**
         * Show dialog if image is being saved for the first time.
         */
//        if(!isSave)
//            alertDialog.show();
//        else
    }

    private void hideMenus() {
        if(layoutGridChooser.getVisibility() == View.VISIBLE)
            layoutGridChooser.setVisibility(View.GONE);

        if(layoutFrameChooser.getVisibility() == View.VISIBLE)
            layoutFrameChooser.setVisibility(View.GONE);

        if(layoutBackgroundChooser.getVisibility() == View.VISIBLE)
            layoutBackgroundChooser.setVisibility(View.GONE);

        if(layoutShapesChooser.getVisibility() == View.VISIBLE)
            layoutShapesChooser.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_READ_PERMISSION && hasPermissions(GALLERY_PERMISSIONS)){
            openGallery();
        }

        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            savePhoto();
        }
    }

    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public Bitmap getFinalBitmap(View v) {
        Bitmap bitmapToSave = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(bitmapToSave);
        v.draw(can);
        return bitmapToSave;
    }

    @Override
    public void onBackPressed() {

        if(layoutBackgroundChooser.getVisibility() == View.VISIBLE)
            layoutBackgroundChooser.setVisibility(View.GONE);
        else if(layoutShapesChooser.getVisibility() == View.VISIBLE)
            layoutShapesChooser.setVisibility(View.GONE);
        else if(layoutFrameChooser.getVisibility() == View.VISIBLE)
            layoutFrameChooser.setVisibility(View.GONE);
        else if(layoutGridChooser.getVisibility() == View.VISIBLE)
            layoutGridChooser.setVisibility(View.GONE);
        else {


            if (isSave){
                super.onBackPressed();
            }else {
                AlertDialog alertDialog = new AlertDialog();

                alertDialog.getAlertDialog(CollageActivity.this, new AlertDialog.OnItemClickListener() {
                    @Override
                    public void onContinueClicked() {
                        CollageActivity.super.onBackPressed();
                    }
                }).setTitle(getString(R.string.alert_title_warning))
                        .setDescription(getString(R.string.alert_desc_changes_will_be_lost))
                        .setSubtitle(getString(R.string.alert_subtitle_press_exit_or_cancel))
                        .show(getSupportFragmentManager(), com.aligohershabir.photocollage.view.dialogFragments.AlertDialog.TAG);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseListeners();
        releaseVariables();
    }

    private void releaseVariables(){
        colorRedDrawable = null;

        /*
         * Clearing Bitmaps
         */
        clearSelectedImageBitmaps();
    }

    private void releaseListeners(){
        shapeStrokeCheckedChangeListener = null;
    }

}
