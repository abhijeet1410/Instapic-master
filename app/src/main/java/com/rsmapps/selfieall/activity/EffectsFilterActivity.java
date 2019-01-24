package com.rsmapps.selfieall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.instapic.activities.InstaPicActivity;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.rsmapps.selfieall.adapter.FilterAdapterFactory;
import com.rsmapps.selfieall.helper.ResourceManager;
import com.rsmapps.selfieall.utility.BitmapUtil;
import com.rsmapps.selfieall.utility.GLToolbox;
import com.rsmapps.selfieall.utility.RecyclerItemClickListener;
import com.rsmapps.selfieall.utility.TextureRenderer;

import butterknife.InjectView;

public class EffectsFilterActivity extends BaseActivity implements GLSurfaceView.Renderer, View.OnClickListener {

    public static final int EFFECTS_FILTER_ACTIVITY_OK = 1;

    int mCurrentEffect;
    private int[] mTextures = new int[2];
    private EffectContext mEffectContext;
    private Effect mEffect;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private int mImageWidth;
    private int mImageHeight;
    private boolean mInitialized = false;
    private volatile boolean saveFrame;

    private FrameLayout llyMain;
//    private CrystalSeekbar crystalSeekbar;
    private TextView txtTitle1, txtSkipFilter, txtNext;
    private Bitmap bitmapOriginal, bitmapFilter, for_save;
    private float seekValue;
    private int selectedPosition;

    @InjectView(R.id.effectsview)
    GLSurfaceView mEffectView;
    @InjectView(R.id.rc_filter)
    RecyclerView recList;

    @InjectView(R.id.sbProgress)
    SeekBar sbProgress;

    private String intentRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_effect_factory);

        intentRequest = getIntent().getStringExtra("request");

        initComponents();

        /**
         * Test Settings
         */
        mEffectView.setZOrderOnTop(true);
        mEffectView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mEffectView.getHolder().setFormat(PixelFormat.RGBA_8888);

        /**
         * Default Settings
         */
        mEffectView.setEGLContextClientVersion(2);
        mEffectView.setRenderer(this);
        mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mCurrentEffect = 0;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recList.setHasFixedSize(true);
        recList.setLayoutManager(layoutManager);

        FilterAdapterFactory filterAdapter = new FilterAdapterFactory(this);
        recList.setAdapter(filterAdapter);

        recList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedPosition = position;
                /**
                 * Resetting seek bar position to start
                 */
                sbProgress.setProgress(50);
                changeEffect(position);

            }
        }));
    }

    private void changeEffect(int position) {

        switch (position) {
            case 0:
                sbProgress.setVisibility(View.GONE);
                break;
            case 1:
                sbProgress.setVisibility(View.VISIBLE);
//                sbProgress.setMin(0);
                sbProgress.setMax(100);
                seekValue = 0.5f;
                break;
            case 2:
                sbProgress.setVisibility(View.GONE);
                break;
            case 3:
                sbProgress.setVisibility(View.VISIBLE);
//                crystalSeekbar.setMinValue(0f);
//                crystalSeekbar.setMaxValue(300f);
                sbProgress.setMax(300);
                seekValue = 2.0f;
                sbProgress.setProgress(150);
                break;
            case 4:
                sbProgress.setVisibility(View.VISIBLE);
//                crystalSeekbar.setMinValue(40f);
                sbProgress.setMax(240);
                seekValue = 1.4f;
                sbProgress.setProgress(120);
                break;
            case 5:
                sbProgress.setVisibility(View.GONE);
                break;
            case 6:
                sbProgress.setVisibility(View.GONE);
                break;
            case 7:
                sbProgress.setVisibility(View.GONE);
                break;
            case 8:
                sbProgress.setVisibility(View.VISIBLE);
//                crystalSeekbar.setMinValue(0f);
                sbProgress.setMax(100);
                seekValue = 0.8f;
                break;
            case 9:
                sbProgress.setVisibility(View.VISIBLE);
//                crystalSeekbar.setMinValue(0f);
                sbProgress.setMax(100);
                seekValue = 0.5f;
                break;
            case 10:
                sbProgress.setVisibility(View.VISIBLE);
//                crystalSeekbar.setMinValue(0f);
                sbProgress.setMax(100);
                seekValue = 1.0f;
                break;
            case 11:
                sbProgress.setVisibility(View.GONE);
                break;
            case 12:
                sbProgress.setVisibility(View.GONE);
                break;
            case 13:
                sbProgress.setVisibility(View.GONE);
                break;
            case 14:
                sbProgress.setVisibility(View.GONE);
                break;
            case 15:
                sbProgress.setVisibility(View.GONE);
                break;
            case 16:
                sbProgress.setVisibility(View.GONE);
                break;
            case 17:
                sbProgress.setVisibility(View.VISIBLE);
//                sbProgress.setMinValue(0f);
                sbProgress.setMax(100);
                seekValue = 0.9f;
                break;
            case 18:
                sbProgress.setVisibility(View.GONE);
                break;
            case 19:
                sbProgress.setVisibility(View.VISIBLE);
//                crystalSeekbar.setMinValue(0f);
                sbProgress.setMax(100);
                seekValue = 0.5f;
                break;
            default:
                sbProgress.setVisibility(View.GONE);
                break;
        }
        setCurrentEffect(position);
        mEffectView.requestRender();
    }

    public void setCurrentEffect(int effect) {
        mCurrentEffect = effect;
    }

    private void initComponents() {
        llyMain = (FrameLayout) findViewById(R.id.llyMain);
//        crystalSeekbar = (CrystalSeekbar) findViewById(R.id.rangeSeekbar1);
//        crystalSeekbar.setVisibility(View.GONE);
        sbProgress.setVisibility(View.GONE);
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float d = (float) seekBar.getProgress();
                seekValue = d / 100;
                setCurrentEffect(selectedPosition);
                mEffectView.requestRender();
            }
        });
//        crystalSeekbar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
//            @Override
//            public void finalValue(Number value) {
//                float d = (float) Integer.parseInt(String.valueOf(value));
//                seekValue = d / 100;
//                setCurrentEffect(selectedPosition);
//                mEffectView.requestRender();
//            }
//        });
        txtSkipFilter = (TextView) findViewById(R.id.txtSkip);
        txtNext = (TextView) findViewById(R.id.txtNext);
        txtSkipFilter.setOnClickListener(this);
        txtNext.setOnClickListener(this);

        /**
         * Setting bitmap to perform
         * Filter feature.
         */
        setBitmap();
    }

    private Bitmap bitmapTemp;

    private void setBitmap() {

        if(intentRequest != null && (intentRequest.equals(InstaPicActivity.TAG)
                || intentRequest.equals(MainActivity.TAG)
                || intentRequest.equals(PhotoSelectActivity.TAG))){
            if(intentRequest.equals(PhotoSelectActivity.TAG)){
                bitmapOriginal = libs.imageCropper.utils.ResourceManager.bitmap;
            }else if(intentRequest.equals(MainActivity.TAG))
                bitmapOriginal = ResourceManager.bitmap;
            else if(intentRequest.equals(InstaPicActivity.TAG))
                bitmapOriginal = ResourceManager.dummyBitmap;


//            int size = getViewHeight(mEffectView);

            float mmInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());

            ViewGroup.LayoutParams layoutParams = mEffectView.getLayoutParams();
            layoutParams.width=(int)mmInPx;
            layoutParams.height=(int)mmInPx;
            mEffectView.setLayoutParams(layoutParams);
            mEffectView.requestLayout();

            bitmapTemp = BitmapUtil.resize(bitmapOriginal, (int)mmInPx, (int)mmInPx);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;

            mEffectView.setLayoutParams(params);
            mEffectView.requestLayout();

            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams)llyMain.getLayoutParams();
            params2.gravity = Gravity.CENTER;
            llyMain.setLayoutParams(params2);
            llyMain.requestLayout();

        }else{
            try {
                String strImagePath;
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    strImagePath = bundle.getString("IMG_PATH");
                    bitmapOriginal = BitmapFactory.decodeFile(strImagePath);
                    int rotateAngle = BitmapUtil.getCameraPhotoOrientation(this, Uri.parse(strImagePath), strImagePath);
                    bitmapOriginal = BitmapUtil.rotateBitmap(bitmapOriginal, rotateAngle);

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
                    //resizing end
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    private void loadTextures() {
        GLES20.glGenTextures(2, mTextures, 0);
        Bitmap bitmap;
        bitmap = bitmapOriginal;

        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLToolbox.initTexParams();
    }

    private void initEffect() {
        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        switch (mCurrentEffect) {
            case 0:
                break;
            case 1:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_AUTOFIX);
                mEffect.setParameter("scale", seekValue);
                break;
            case 2:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BLACKWHITE);
                mEffect.setParameter("black", .1f);
                mEffect.setParameter("white", .7f);
                break;
            case 3:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", seekValue);
                break;
            case 4:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", seekValue);
                break;
            case 5:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CROSSPROCESS);
                break;
            case 6:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
                break;
            case 7:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DUOTONE);
                mEffect.setParameter("first_color", Color.YELLOW);
                mEffect.setParameter("second_color", Color.DKGRAY);
                break;
            case 8:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", seekValue);
                break;
            case 9:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
                mEffect.setParameter("scale", seekValue);
                break;
            case 10:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
                mEffect.setParameter("strength", seekValue);
                break;
            case 11:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAYSCALE);
                break;
            case 12:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_LOMOISH);
                break;
            case 13:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_NEGATIVE);
                break;
            case 14:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_POSTERIZE);
                break;
            case 15:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SEPIA);
                break;
            case 16:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SHARPEN);
                break;
            case 17:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TEMPERATURE);
                mEffect.setParameter("scale", seekValue);
                break;
            case 18:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TINT);
                mEffect.setParameter("tint", Color.MAGENTA);
                break;
            case 19:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_VIGNETTE);
                mEffect.setParameter("scale", seekValue);
                break;
            default:
                break;
        }
    }

    private void applyEffect() {
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }

    private void renderResult() {
        if (mCurrentEffect != 0) {
            mTexRenderer.renderTexture(mTextures[1]);
        } else {
            saveFrame = true;
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            loadTextures();
            mInitialized = true;
        }
        if (mCurrentEffect != 0) {
            initEffect();
            applyEffect();
        }
        try {
            renderResult();
        }catch (RuntimeException ex){
            ex.printStackTrace();
            txtSkipFilter.callOnClick();
        }

        /**
         * Setting size of mEffectView to Bitmap's size
         */
        resizeGLSurfaceView();

        bitmapFilter = createBitmapFromGLSurface(0, 0, mEffectView.getWidth(), mEffectView.getHeight(), gl);

        bitmapFilter = Bitmap.createScaledBitmap(bitmapFilter, bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), false);

    }

    private void resizeGLSurfaceView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mEffectView.getLayoutParams().height = bitmapTemp.getHeight();
                mEffectView.getLayoutParams().width = bitmapTemp.getWidth();
                mEffectView.requestLayout();
            }
        });
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (mTexRenderer != null) {
            mTexRenderer.updateViewSize(width, height);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void onClick(View v) {
        if (v == txtSkipFilter) {
            ResourceManager.bitmap = bitmapOriginal;
            /*Intent intent = new Intent(EffectsFilterActivity.this, MainActivity.class);
			if(getIntent().hasExtra("url")){
				intent.putExtra("url",getIntent().getStringExtra("url"));
			}
			startActivity(intent);*/
            setResult(RESULT_OK, new Intent());
            finish();
        } else if (v == txtNext) {
            //ResourceManager.bitmap = bitmapFilter;
/*			Intent intent = new Intent(EffectsFilterActivity.this, MainActivity.class);
			if(getIntent().hasExtra("url")){
				intent.putExtra("url",getIntent().getStringExtra("url"));
			}
			startActivity(intent);*/
            //setResult(RESULT_OK,new Intent());
            //finish();

            //Resizing Start
            int orignalWidth = bitmapFilter.getWidth();
            int orignalHeight = bitmapFilter.getHeight();
            Rectangle orignalRect = new Rectangle();
            Rectangle targetRect = new Rectangle();
            orignalRect.setBounds(0, 0, orignalWidth, orignalHeight);
            targetRect.setBounds(0, 0, 1080, 1920);
            Rectangle output = getScaledDimension(orignalRect, targetRect);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapFilter, output.width, output.height, true);
            //bitmapOriginal.recycle();
            bitmapFilter = resizedBitmap;
            //resizing end

            if(getIntent().getStringExtra("request").equals(InstaPicActivity.TAG)){
                ResourceManager.dummyBitmap = bitmapFilter;
            }else{
                ResourceManager.bitmap = bitmapFilter;
            }
            setResult(RESULT_OK, new Intent());
            finish();

            /*AdsUtil.showInterstitialAd(EffectsFilterActivity.this, new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    ResourceManager.bitmap = bitmapFilter;
                    setResult(RESULT_OK, new Intent());
                    finish();
                    AdsUtil.preloadInterstitialAd(EffectsFilterActivity.this);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    ResourceManager.bitmap = bitmapFilter;
                    setResult(RESULT_OK, new Intent());
                    finish();
                    AdsUtil.preloadInterstitialAd(EffectsFilterActivity.this);
                }
            });*/
        }
    }
}
