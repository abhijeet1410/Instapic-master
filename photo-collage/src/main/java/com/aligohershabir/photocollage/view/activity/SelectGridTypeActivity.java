package com.aligohershabir.photocollage.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aligohershabir.photocollage.adapter.CollageAdapter;
import com.aligohershabir.photocollage.model.Grid;
import com.aligohershabir.photocollage.utils.AssetUtils;
import com.google.android.gms.ads.AdView;
import com.aligohershabir.photocollage.utils.AdsUtil;
import com.aligohershabir.photocollage.R;

import java.util.ArrayList;

//import android.util.Log;

public class SelectGridTypeActivity extends AppCompatActivity {

    public static final String TAG = SelectGridTypeActivity.class.getSimpleName();
    public static final int RC_SELECT_GRID_TYPE = 101;
    public static final String SELECTED_GRID_ID = "selectedGridId";
    public static final String SELECTED_GRID_CAN_HAVE_IMAGES = "gridCanHaveImages";

    private RecyclerView rvCollage;
    private CollageAdapter collageAdapter;
    ArrayList<Grid> grids = new ArrayList<>();

    private void getInfo(){
        rvCollage = findViewById(R.id.rvCollage);
    }

    private void setInfo(){
        collageAdapter = new CollageAdapter(SelectGridTypeActivity.this, grids, new CollageAdapter.OnItemClickedListener() {
            @Override
            public void onImageClicked(int position) {

                Intent dataIntent = new Intent();
                dataIntent.putExtra(SELECTED_GRID_ID,grids.get(position).getGridId());
                dataIntent.putExtra(SELECTED_GRID_CAN_HAVE_IMAGES,grids.get(position).getCanHaveImages());
                setResult(RESULT_OK,dataIntent);
                finish();
            }
        },false);

        rvCollage.setLayoutManager(new GridLayoutManager(this,4));
        rvCollage.setAdapter(collageAdapter);

        /*
         * getting collage grid images from assets
         * and notifying collageAdapter.
         */
        loadCollageData();
    }

    public static String[] oneImageGrid, twoImageGrid, threeImageGrid,
            fourImageGrid,fiveImageGrid,sixImageGrid,sevenImageGrid
            ,eightImageGrid,nineImageGrid;

    private void loadCollageData(){
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

        /*
         * Will be used to uniquely identify all Grids.
         */
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
        collageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_grid_type);

        getInfo();
        setInfo();

        AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));
    }
}
