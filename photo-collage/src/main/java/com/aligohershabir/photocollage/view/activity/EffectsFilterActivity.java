package com.aligohershabir.photocollage.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aligohershabir.photocollage.utils.GLToolbox;
import com.aligohershabir.photocollage.utils.RecyclerItemClickListener;
import com.aligohershabir.photocollage.utils.TextureRenderer;
import com.aligohershabir.photocollage.adapter.effectFilterAdapters.FilterAdapterFactory;
import com.aligohershabir.photocollage.utils.ResourceManagerFilter;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.ads.AdView;
import com.aligohershabir.photocollage.utils.AdsUtil;
import com.aligohershabir.photocollage.R;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//import android.util.Log;

public class EffectsFilterActivity extends AppCompatActivity implements GLSurfaceView.Renderer, View.OnClickListener {

    public static final String TAG = EffectsFilterActivity.class.getSimpleName();

	private RecyclerView recList;
	int mCurrentEffect;
	private GLSurfaceView mEffectView;
	private int[] mTextures = new int[2];
	private EffectContext mEffectContext;
	private Effect mEffect;
	private TextureRenderer mTexRenderer = new TextureRenderer();
	private int mImageWidth;
	private int mImageHeight;
	private boolean mInitialized = false;
	private volatile boolean saveFrame;

	//
	private FrameLayout llyMain;
	//private LinearLayout llyEffect;
	private CrystalSeekbar crystalSeekbar;
	private ImageView imgPic, imgLockUnlock, imgSave, imgBack;
	private TextView txtTitle, txtSkipFilter, txtNext;
	private Bitmap bitmapOriginal, bitmapFilter,for_save;
	private float seekValue;
	private int selectedPosition;

	public void setCurrentEffect(int effect) {
		mCurrentEffect = effect;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_hoarding_img_effect_factory);

		initComponents();

		mEffectView = (GLSurfaceView) findViewById(R.id.effectsview);
		mEffectView.setEGLContextClientVersion(2);
		mEffectView.setRenderer(this);
		mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		mCurrentEffect = 0;

		LinearLayoutManager layoutManager
				= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

		recList = (RecyclerView) findViewById(R.id.rc_filter);
		recList.setHasFixedSize(true);
		recList.setLayoutManager(layoutManager);

		FilterAdapterFactory filterAdapter = new FilterAdapterFactory(this);
		recList.setAdapter(filterAdapter);

		recList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {

				selectedPosition = position;
				changeEffect(position);

//				setCurrentEffect(position);
//				mEffectView.requestRender();
			}
		}));


		AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));
	}

	private void changeEffect(int position){

		switch (position) {

			case 0:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 1:
				crystalSeekbar.setVisibility(View.VISIBLE);
				crystalSeekbar.setMinValue(0f);
				crystalSeekbar.setMaxValue(100f);
				seekValue = 0.5f;
				break;

			case 2:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 3:
				crystalSeekbar.setVisibility(View.VISIBLE);
				crystalSeekbar.setMinValue(0f);
				crystalSeekbar.setMaxValue(300f);
				seekValue = 2.0f;
				break;

			case 4:
				crystalSeekbar.setVisibility(View.VISIBLE);
				crystalSeekbar.setMinValue(40f);
				crystalSeekbar.setMaxValue(240f);
				seekValue = 1.4f;
				break;

			case 5:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 6:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 7:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 8:
				crystalSeekbar.setVisibility(View.VISIBLE);
				crystalSeekbar.setMinValue(0f);
				crystalSeekbar.setMaxValue(100f);
				seekValue = 0.8f;
				break;

			case 9:
				crystalSeekbar.setVisibility(View.VISIBLE);
				crystalSeekbar.setMinValue(0f);
				crystalSeekbar.setMaxValue(100f);
				seekValue = 0.5f;
				break;

//			case 10:
//				crystalSeekbar.setVisibility(View.GONE);
//				break;
//
//			case 11:
//				crystalSeekbar.setVisibility(View.GONE);
//				break;

			case 10:
				crystalSeekbar.setVisibility(View.VISIBLE);
				crystalSeekbar.setMinValue(0f);
				crystalSeekbar.setMaxValue(100f);
				seekValue = 1.0f;
				break;

			case 11:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 12:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 13:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 14:
				crystalSeekbar.setVisibility(View.GONE);
				break;

//			case 15:
//				crystalSeekbar.setVisibility(View.GONE);
////				mEffect = effectFactory.createEffect(EffectFactory.EFFECT_ROTATE);
////				mEffect.setParameter("angle", 180);
//				break;

//			case 18:
//				crystalSeekbar.setVisibility(View.VISIBLE);
//				crystalSeekbar.setMinValue(0f);
//				crystalSeekbar.setMaxValue(100f);
//				seekValue = 0.5f;
//				break;

			case 15:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 16:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 17:
				crystalSeekbar.setVisibility(View.VISIBLE);
				crystalSeekbar.setMinValue(0f);
				crystalSeekbar.setMaxValue(100f);
				seekValue = 0.9f;
				break;

			case 18:
				crystalSeekbar.setVisibility(View.GONE);
				break;

			case 19:
				crystalSeekbar.setVisibility(View.VISIBLE);
				crystalSeekbar.setMinValue(0f);
				crystalSeekbar.setMaxValue(100f);
				seekValue = 0.5f;
				break;

			default:
				crystalSeekbar.setVisibility(View.GONE);
				break;

		}

		setCurrentEffect(position);
		mEffectView.requestRender();

	}

	private void initComponents() {

//		bitmapOriginal = ResourceManagerFilter.bitmap;
		bitmapOriginal = com.aligohershabir.photocollage.utils.ResourceManager.bitmap;
//        Log.i(TAG, "bitmapOriginal : " + bitmapOriginal);

		llyMain = (FrameLayout) findViewById(R.id.llyMain);
		//llyEffect = (LinearLayout) findViewById(R.id.llyEffect);

		crystalSeekbar = (CrystalSeekbar)findViewById(R.id.rangeSeekbar1);
		crystalSeekbar.setVisibility(View.GONE);
		//crystalSeekbar.setPosition(255);
		//llyEffect.setAlpha(1f);
//
		// set final value listener
		crystalSeekbar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
			@Override
			public void finalValue(Number value) {

//				Log.d("===CRS=>", String.valueOf(value));

				float d = (float)Integer.parseInt(String.valueOf(value));

				seekValue = d/100;

//				Log.d("===CRS=>", String.valueOf(value)+"===float:"+seekValue);

				// int alpha = barOpacity.getProgress();
				//textOpacitySetting.setText(String.valueOf(alpha));
				//image.setAlpha(alpha);   //deprecated
				//image.setImageAlpha(alpha); //for API Level 16+
				//imgPic.setImageAlpha(progress);
				// imgPic.setImageAlpha(Integer.parseInt(String.valueOf(value)));
				//sandb.setAlpha(Integer.parseInt(String.valueOf(value)));
				//imgPic.setAlpha(f);

				//llyEffect.setAlpha(f);

				setCurrentEffect(selectedPosition);
				mEffectView.requestRender();
			}
		});


		imgBack = (ImageView) findViewById(R.id.imgBack);
		imgSave = (ImageView) findViewById(R.id.imgSave);
		imgSave.setVisibility(View.GONE);

		imgLockUnlock = (ImageView) findViewById(R.id.imgLockUnlock);
		imgLockUnlock.setBackgroundResource(R.drawable.btn_unlock_bg);

		//txtTitle = (TextView) findViewById(R.id.txtTitle);
		//txtTitle.setText("FILTER IMAGE");

		txtSkipFilter = (TextView) findViewById(R.id.txtSkip);
		txtNext = (TextView) findViewById(R.id.txtNext);

//		imgPic = (ImageView) findViewById(R.id.imgPic);


		//imgBack.setOnClickListener(this);
		imgSave.setOnClickListener(this);
		imgLockUnlock.setOnClickListener(this);

		imgBack.setVisibility(View.INVISIBLE);
		imgLockUnlock.setVisibility(View.GONE);


//		if(ResourceManager.instaPic){
//			txtNext.setText("Save");
//			txtSkipFilter.setText("");
//		}else {
//			txtSkipFilter.setOnClickListener(this);
//		}
		txtSkipFilter.setOnClickListener(this);
		txtNext.setOnClickListener(this);


	}

	private void loadTextures() {
		// Generate textures
		GLES20.glGenTextures(2, mTextures, 0);

		// Load input bitmap
		Bitmap bitmap;// = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
		bitmap = bitmapOriginal;

		mImageWidth = bitmap.getWidth();
		mImageHeight = bitmap.getHeight();
		mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

		// Upload to texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);


		// Set texture parameters
		GLToolbox.initTexParams();
		//bitmap.recycle();
	}

	private void initEffect() {
		EffectFactory effectFactory = mEffectContext.getFactory();
		if (mEffect != null) {
			mEffect.release();
		}
		/**
		 * Initialize the correct effect based on the selected menu/action item
		 */
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

//		case 10:
//			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
//			mEffect.setParameter("vertical", true);
//			break;
//
//		case 11:
//			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
//			mEffect.setParameter("horizontal", true);
//			break;

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

//		case 15:
//			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_ROTATE);
//			mEffect.setParameter("angle", 180);
//			break;

//		case 18:
//			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
//			mEffect.setParameter("scale", seekValue);
//			break;

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
			// if no effect is chosen, just render the original bitmap
			mTexRenderer.renderTexture(mTextures[1]);
		} else {
			saveFrame=true;
			// render the result of applyEffect()
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

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mEffectView.getLayoutParams().height = bitmapOriginal.getHeight();
				mEffectView.getLayoutParams().width = bitmapOriginal.getWidth();
				mEffectView.requestLayout();
			}
		});

		bitmapFilter = createBitmapFromGLSurface(0,0,mEffectView.getWidth(),mEffectView.getHeight(),gl);
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

		if (v == imgBack) {
			finish();
		} else if(v == txtSkipFilter){
			if(com.aligohershabir.photocollage.utils.ResourceManager.instaPic){
				finish();
			}

		}else if(v == txtNext){
			com.aligohershabir.photocollage.utils.ResourceManager.bitmap = bitmapFilter;

			if(com.aligohershabir.photocollage.utils.ResourceManager.instaPic){
				setResult(RESULT_OK);
				finish();
			}
		}

	}

}
