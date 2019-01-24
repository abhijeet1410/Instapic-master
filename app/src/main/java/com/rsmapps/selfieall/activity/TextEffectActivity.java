package com.rsmapps.selfieall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.google.android.gms.ads.AdView;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.adapter.PaintAdapter;
import com.rsmapps.selfieall.adapter.TxtEffectAdapter;
import com.rsmapps.selfieall.helper.ShadowLayer;
import com.rsmapps.selfieall.helper.TextEffectPojo;
import com.rsmapps.selfieall.helper.Utils;
import com.rsmapps.selfieall.utility.AssetUtil;

import java.util.ArrayList;


public class TextEffectActivity extends Activity implements View.OnClickListener {

    private TxtEffectAdapter mAdapter;
    private PaintAdapter paintAdapter;
    private RecyclerView recyclerView, recyclerViewPaint, recyclerViewShadow;
    private RecyclerView.LayoutManager mLayoutManager;

    public static TextEffectPojo textEffectPojo;
    private   ArrayList<String> arrayListFont;
    private LinearLayout llyEditTxt, llyTxt, llyPaint, llyTxtGlow, llyBold,
            llyTxtPanel, llyPaintPanel, llyOne, llyTwo, llyShadowPanel;
    private ImageView imgCancel, imgSave;
    private TextView txtData, txtColor;
    private SeekBar seekBarTxt, seekBarPaint, seekBarShadow1, seekBarShadow2;
    private int fontPos, fontStylePos, colorType, color1, color2,shadowColor;

    private AlertDialog alertDialog1;
    private CharSequence[] values = {"  Normal ","  Bold ","  Italic ","  Bold Italic "};
    private CheckBox chkBox;
    private float dx, dy;

    //
    // public static Drawable drawable;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_effect);


        initComponents();
        initVariable();

        setupRecyclerViewFont();
        setupRecyclerViewPaint();
        setupRecyclerViewShadow();

        initText();

        color1 = getResources().getColor(R.color.Coral);
        color2 = getResources().getColor(R.color.HotPink);
//        colorShaodw =  color2;
        gradientColor(color1,color1);


        //AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_top));
        //AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //AdsManager.getAdsManager().showAdMobInterstitial();
    }

    private void initComponents(){

        imgCancel = (ImageView)findViewById(R.id.imgCancel);
        imgSave = (ImageView)findViewById(R.id.imgSave);

        seekBarTxt = (SeekBar)findViewById(R.id.seekBarTxt);
        seekBarTxt.setOnSeekBarChangeListener(barOpacityOnSeekBarChangeListener);

        seekBarPaint = (SeekBar)findViewById(R.id.seekBarPaint);
        seekBarPaint.setOnSeekBarChangeListener(txtViewOpacityOnSeekBarChangeListener);

        seekBarShadow1 = (SeekBar)findViewById(R.id.seekBarShadow1);
        seekBarShadow1.setOnSeekBarChangeListener(txtViewShadowOnSeekBarChangeListener1);

        seekBarShadow2 = (SeekBar)findViewById(R.id.seekBarShadow2);
        seekBarShadow2.setOnSeekBarChangeListener(txtViewShadowOnSeekBarChangeListener2);

        txtData = (TextView)findViewById(R.id.txtData);
        txtColor = (TextView)findViewById(R.id.txtColor);

        llyEditTxt = (LinearLayout)findViewById(R.id.llyEditTxt);
        llyTxt = (LinearLayout)findViewById(R.id.llyTxt);
        llyPaint = (LinearLayout)findViewById(R.id.llyPaint);
        llyOne = (LinearLayout)findViewById(R.id.llyOne);
        llyTwo = (LinearLayout)findViewById(R.id.llyTwo);
        llyTxtGlow = (LinearLayout)findViewById(R.id.llyTxtGlow);
        llyBold = (LinearLayout)findViewById(R.id.llyBold);

        llyTxtPanel = (LinearLayout)findViewById(R.id.llyTxtPanel);
        llyTxtPanel.setVisibility(View.VISIBLE);

        llyPaintPanel = (LinearLayout)findViewById(R.id.llyPaintPanel);
        llyPaintPanel.setVisibility(View.GONE);

        llyShadowPanel = (LinearLayout)findViewById(R.id.llyShadowPanel);
        llyShadowPanel.setVisibility(View.GONE);

        imgCancel.setOnClickListener(this);
        imgSave.setOnClickListener(this);

        llyEditTxt.setOnClickListener(this);
        llyTxt.setOnClickListener(this);
        llyPaint.setOnClickListener(this);
        llyOne.setOnClickListener(this);
        llyTwo.setOnClickListener(this);
        llyTxtGlow.setOnClickListener(this);
        llyBold.setOnClickListener(this);

        llyOne.setBackgroundResource(R.color.DeepPink);
        llyTwo.setBackgroundResource(R.color.LightSkyBlue);

        chkBox = (CheckBox)findViewById(R.id.chkBox);
        chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("Text","===checkboxxxxxxxxx");
                if(b){
                    gradientColor(color1,color2);
                }else{
                    gradientColor(color1,color1);
                }

            }
        });


        txtData.setDrawingCacheEnabled(true);

    }

    private void initVariable(){
        textEffectPojo = new TextEffectPojo();
        fontPos = 1;
        fontStylePos = 0;
        colorType = 1;
        dx = 5.0f;
        dy = 5.0f;
    }

    SeekBar.OnSeekBarChangeListener barOpacityOnSeekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener(){

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    txtData.setTextSize(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

            };

    SeekBar.OnSeekBarChangeListener txtViewOpacityOnSeekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener(){

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    //txtData.setAlpha(progress);
                    float alpha = (float)progress/255;
                    txtData.setAlpha(alpha);
                    textEffectPojo.setAlpha(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

            };

    SeekBar.OnSeekBarChangeListener txtViewShadowOnSeekBarChangeListener1 =
            new SeekBar.OnSeekBarChangeListener(){

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    //txtData.setAlpha(progress);
                    dx = (float)progress;
//                    txtData.setAlpha(alpha);
//                    textEffectPojo.setAlpha(progress);

                    txtData.setShadowLayer(
                            5f, // radius
                            dx, // dx
                            dy, // dy
                            color1 // shadow color
                    );

                    txtData.invalidate();

                    ShadowLayer shadowLayer = new ShadowLayer(5f,dx,dy,shadowColor);
                    textEffectPojo.setShadowLayer(shadowLayer);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

            };

    SeekBar.OnSeekBarChangeListener txtViewShadowOnSeekBarChangeListener2 =
            new SeekBar.OnSeekBarChangeListener(){

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    //txtData.setAlpha(progress);
//                    float alpha = (float)progress/255;
//                    txtData.setAlpha(alpha);
//                    textEffectPojo.setAlpha(progress);

                    //txtData.setAlpha(progress);
                    dy = (float)progress;
//                    txtData.setAlpha(alpha);
//                    textEffectPojo.setAlpha(progress);

                    txtData.setShadowLayer(
                            5, // radius
                            dx, // dx
                            dy, // dy
                            color1 // shadow color
                    );

                    txtData.invalidate();

                    ShadowLayer shadowLayer = new ShadowLayer(5f,dx,dy,shadowColor);
                    textEffectPojo.setShadowLayer(shadowLayer);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

            };

    private void initText(){
        txtData.setText("Enter Your Text!!!");
        txtData.setTextSize(20);
        textEffectPojo.setAlpha(255);

        shadowColor = color1;
        ShadowLayer shadowLayer = new ShadowLayer(0,0,0,shadowColor);
        textEffectPojo.setShadowLayer(shadowLayer);

        if(arrayListFont!=null && arrayListFont.size()>1) {
            Utils.setFont(TextEffectActivity.this, txtData, arrayListFont.get(1));
            updateTextEffectPojo();
        }

    }

    private void gradientColor(int color1, int color2){
        Shader shader = Utils.getShader(txtData.getTextSize(),color1,color2);
        txtData.getPaint().setShader(shader);
        txtData.invalidate();
        textEffectPojo.setShader(shader);
    }

    private void updateTextEffectPojo(){
        //textEffectPojo.setTextColor(color1);
        textEffectPojo.setText(txtData.getText().toString().trim());
        //textEffectPojo.setTextSize(seekBarTxt.getProgress());
        textEffectPojo.setTypeface(Utils.getCustomTypeface(TextEffectActivity.this, arrayListFont.get(fontPos)));
        //textEffectPojo.setAlpha(255);

        if(fontStylePos == 0){
            textEffectPojo.setTextStyle(Typeface.NORMAL);
        }else if(fontStylePos == 1){
            textEffectPojo.setTextStyle(Typeface.BOLD);
        }else if(fontStylePos == 2){
            textEffectPojo.setTextStyle(Typeface.ITALIC);
        }else if(fontStylePos == 3){
            textEffectPojo.setTextStyle(Typeface.BOLD_ITALIC);
        }

        Typeface typeface = Typeface.create(textEffectPojo.getTypeface(),textEffectPojo.getTextStyle());
        textEffectPojo.setTypeface(typeface);

    }

    private void setupRecyclerViewFont() {

        loadFont();

        mAdapter = new TxtEffectAdapter(this);
        mAdapter.setData(arrayListFont);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new TxtEffectAdapter.RecyclerTouchListener(this, recyclerView, new TxtEffectAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                fontPos = position;
                Utils.setFont(TextEffectActivity.this,txtData,arrayListFont.get(position));
                updateTextEffectPojo();

            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void loadFont(){
        String[] file = AssetUtil.getDataFromAsser(this,AssetUtil.FileType.CELEBRITY);
        arrayListFont = new ArrayList<>();
        for (int i = 0; i < file.length; i++) {
            arrayListFont.add("fonts/"+file[i]);
        }

//        arrayListFont = new ArrayList<>();
//
//        for (int i = 0; i <36 ; i++) {
//            int index = i+1;
//            arrayListFont.add("fonts/font"+ index+".ttf");
//        }

    }

    private void setupRecyclerViewPaint() {

        final String[] colorNames = getResources().getStringArray(R.array.colorNames);

        paintAdapter = new PaintAdapter(this);
        paintAdapter.setData(colorNames);
        recyclerViewPaint = (RecyclerView)findViewById(R.id.recyclerViewPaint);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPaint.setLayoutManager(mLayoutManager);
        recyclerViewPaint.setAdapter(paintAdapter);

        recyclerViewPaint.addOnItemTouchListener(new PaintAdapter.RecyclerTouchListener(this, recyclerViewPaint, new PaintAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                TypedArray ta = TextEffectActivity.this.getResources().obtainTypedArray(R.array.colors);
                int colorToUse = ta.getResourceId(position, 0);

                if(colorType == 1){
                    color1 = getResources().getColor(colorToUse);

                    llyOne.setBackgroundResource(colorToUse);
                    txtColor.setText(colorNames[position]);
                    txtColor.setTextColor(color1);
                    txtColor.setVisibility(View.GONE);
                    //txtData.setTextColor(color1);
                    if(chkBox.isChecked()){
                        gradientColor(color1, color2);
                    }else{
                        gradientColor(color1, color1);
                    }
                }else{
                    color2 = getResources().getColor(colorToUse);
                    llyTwo.setBackgroundResource(colorToUse);
                    if(chkBox.isChecked())
                        gradientColor(color1, color2);
                }

            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void setupRecyclerViewShadow() {

        final String[] colorNames = getResources().getStringArray(R.array.colorNames);
        final String[] colorCodes = getResources().getStringArray(R.array.colorCodes);

        paintAdapter = new PaintAdapter(this);
        paintAdapter.setData(colorNames);
        recyclerViewShadow = (RecyclerView)findViewById(R.id.recyclerViewShadow);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewShadow.setLayoutManager(mLayoutManager);
        recyclerViewShadow.setAdapter(paintAdapter);

        recyclerViewShadow.addOnItemTouchListener(new PaintAdapter.RecyclerTouchListener(this, recyclerViewShadow, new PaintAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                TypedArray ta = TextEffectActivity.this.getResources().obtainTypedArray(R.array.colors);
                int colorToUse = ta.getResourceId(position, 0);
//
//                if(colorType == 1){
//                    color1 = getResources().getColor(colorToUse);
//
//                    llyOne.setBackgroundResource(colorToUse);
//                    txtColor.setText(colorNames[position]);
//                    txtColor.setTextColor(color1);
//                    txtColor.setVisibility(View.GONE);
//                    //txtData.setTextColor(color1);
//                    if(chkBox.isChecked()){
//                        gradientColor(color1, color2);
//                    }else{
//                        gradientColor(color1, color1);
//                    }
//                }else{
//                    color2 = getResources().getColor(colorToUse);
//                    llyTwo.setBackgroundResource(colorToUse);
//                    if(chkBox.isChecked())
//                        gradientColor(color1, color2);
//                }

//               int  color1 = getResources().getColor(colorToUse);
//                txtData.setShadowLayer(
//                        1.5f, // radius
//                        5.0f, // dx
//                        5.0f, // dy
//                        color1 // shadow color
//                );

                shadowColor =  Color.parseColor(colorCodes[position]);
                txtData.setShadowLayer(
                        5f, // radius
                        dx, // dx
                        dy, // dy
                        shadowColor // shadow color
                );

                //txtData.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

                txtData.invalidate();
//
                ShadowLayer shadowLayer = new ShadowLayer(5f,dx,dy,shadowColor);
                textEffectPojo.setShadowLayer(shadowLayer);

            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public void onClick(View view) {

        if(view == imgCancel){
            //AdsManager.getAdsManager().showAdMobInterstitial();
            finish();
        }else if(view == imgSave){

//            txtData.buildDrawingCache();
//            Bitmap bitmap = txtData.getDrawingCache();
//            ResourceManager.bitmap = bitmap;
//
//            ResourceManager.drawable = new BitmapDrawable(getResources(),bitmap);
//            ResourceManager.bitmap = Bitmap.createBitmap(bitmap.getWidth(),
//                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);

            //AdsManager.getAdsManager().showAdMobInterstitial();
            setResult(RESULT_OK, null);
            finish();
        }else if(view == llyEditTxt){
            showCustomAlert();
        }else if(view == llyTxt){
            llyShadowPanel.setVisibility(View.GONE);
            llyPaintPanel.setVisibility(View.GONE);
            llyTxtPanel.setVisibility(View.VISIBLE);
        }else if(view == llyPaint){
            llyShadowPanel.setVisibility(View.GONE);
            llyTxtPanel.setVisibility(View.GONE);
            llyPaintPanel.setVisibility(View.VISIBLE);
        }else if(view == llyTxtGlow){
            llyShadowPanel.setVisibility(View.VISIBLE);
            llyPaintPanel.setVisibility(View.GONE);
            llyTxtPanel.setVisibility(View.GONE);
        }else if(view == llyBold){
            showCustomRadioButton(fontStylePos);
        }else if(view == llyOne){
            colorType = 1;
        }else if(view == llyTwo){
            colorType = 2;
        }

    }

    private void showCustomAlert(){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_edit_text, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        // userInput.setText(txtData.getText());
        // userInput.setSelection(txtData.getText().length());

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                txtData.setText(userInput.getText());
                                updateTextEffectPojo();
                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showCustomRadioButton(int selectedPos){

        AlertDialog.Builder builder = new AlertDialog.Builder(TextEffectActivity.this,AlertDialog.THEME_HOLO_LIGHT);

        builder.setTitle("Font Style");

        builder.setSingleChoiceItems(values, selectedPos, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        fontStylePos = 0;
                        txtData.setTypeface(textEffectPojo.getTypeface(),Typeface.NORMAL);
                        updateTextEffectPojo();

                        break;
                    case 1:
                        fontStylePos = 1;
                        txtData.setTypeface(textEffectPojo.getTypeface(),Typeface.BOLD);
                        updateTextEffectPojo();

                        break;
                    case 2:
                        fontStylePos = 2;
                        txtData.setTypeface(textEffectPojo.getTypeface(),Typeface.ITALIC);
                        updateTextEffectPojo();

                        break;
                    case 3:
                        fontStylePos = 3;
                        txtData.setTypeface(textEffectPojo.getTypeface(),Typeface.BOLD_ITALIC);
                        updateTextEffectPojo();

                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }


}
