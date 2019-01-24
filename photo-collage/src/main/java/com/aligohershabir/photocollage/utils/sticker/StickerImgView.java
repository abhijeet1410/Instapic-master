package com.aligohershabir.photocollage.utils.sticker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.aligohershabir.photocollage.R;
import com.aligohershabir.photocollage.utils.FileUtil;
import com.aligohershabir.photocollage.utils.customViews.FontPojo;
import com.aligohershabir.photocollage.utils.customViews.ShadowLayer;
import com.aligohershabir.photocollage.utils.customViews.TextEffectPojo;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;

//import android.util.Log;

/**
 * Created by Mohammed.Irfan on 10-11-2017.
 */

public class StickerImgView {

    private static final String TAG = StickerImgView.class.getSimpleName();
    private  Activity activity;
    private StickerView stickerView;
    private int width, height;
    private StickerViewTouchListener stickerViewTouchListener;

    public StickerImgView(Activity activity){
        this.activity = activity;
        init();
    }

    private void init(){
        stickerView = (StickerView) activity.findViewById(R.id.sticker_view);
        stickerView.setIcons(getStickerViewIcons());
        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);

        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
//                Log.d(TAG, "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                addStickerViewIcons();
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
            }

            @Override
            public void onOutSideStickerClicked() {
                removeStickerViewIcons();
            }
        });

        ViewTreeObserver observer = stickerView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                width = stickerView.getWidth();
                height = stickerView.getHeight();
            }
        });

        stickerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d(TAG, "===onStciker Touch");
                if(stickerViewTouchListener!=null){
                    stickerViewTouchListener.onStickerViewTouchTouch(v,event);
                }
                return false;
            }
        });

    }

    private void removeStickerViewIcons(){
        stickerView.clearIcons();
    }

    private void addStickerViewIcons(){
        stickerView.setIcons(getStickerViewIcons());
    }

    @NonNull
    private List<BitmapStickerIcon> getStickerViewIcons() {
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(activity,
                R.drawable.sticker_delete_bg),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(activity,
                R.drawable.sticker_resize_bg),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(activity,
                R.drawable.sticker_flip_bg),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());

        return Arrays.asList(deleteIcon, zoomIcon, flipIcon);
    }

    public void addSticker(Drawable drawable) {
        stickerView.addSticker(new DrawableSticker(drawable));
        stickerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //releaseLock();
            }
        });
    }

    public void addTextSticker(Activity activity, FontPojo fontPojo) {
        final TextSticker sticker = new TextSticker(activity);
        sticker.setText(fontPojo.getText());  //text
        sticker.setTypeface(fontPojo.getTypeface());  //Font
        sticker.setTextColor(fontPojo.getTextColor());

        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        sticker.resizeText();
        stickerView.addSticker(sticker);
    }

    public void addTextSticker(Activity activity, TextEffectPojo textEffectPojo) {
        final TextSticker sticker = new TextSticker(activity);
        sticker.setText(textEffectPojo.getText());  //text
        sticker.setTypeface(textEffectPojo.getTypeface());  //Font
        sticker.setShader(textEffectPojo.getShader());  //shader
        sticker.setAlpha(textEffectPojo.getAlpha());  //alpha

        ShadowLayer shadowLayer = textEffectPojo.getShadowLayer();
        if(shadowLayer!=null && shadowLayer.getRadius()>0f &&
                shadowLayer.getDx()>0f && shadowLayer.getDy() >0f && shadowLayer.getColor()!=0l){

            sticker.setShadowLayer(shadowLayer.getRadius(),
                    shadowLayer.getDx(),
                    shadowLayer.getDy(),
                    shadowLayer.getColor());
        }

        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        sticker.resizeText();
        stickerView.addSticker(sticker);
    }

    public String save(Activity activity,Bitmap bitmap){
        File file = FileUtil.getNewFile(activity,FileUtil.FOLDER_NAME);
        if (file != null) {
            stickerView.save(file,bitmap);
            Toast.makeText(activity, "Your image is saved", Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isLock(){
        return stickerView.isLocked();
    }

    public void setLock(){
        stickerView.setLocked(true);
    }

    public void setUnLock(){
        stickerView.setLocked(false);
    }

    public void setStickerViewTouchListener(StickerViewTouchListener stickerViewTouchListener){
        this.stickerViewTouchListener = stickerViewTouchListener;
    }

}
