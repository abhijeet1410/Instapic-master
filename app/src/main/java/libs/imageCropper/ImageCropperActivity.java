package libs.imageCropper;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import libs.imageCropper.utils.ResourceManager;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.AdsUtil;

import com.rsmapps.selfieall.utility.BitmapUtil;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class ImageCropperActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ImageCropperActivity.class.getSimpleName();
    public static final String CROPPER_IMAGE_URI = "imageUri";
    public static final int RC_IMAGE_CROPPER_ACTIVITY = 231;

    private Uri imageUri;

    private CropImageView cropImageView;
    private ImageView ivBack, ivRotate, ivFlip;
    private TextView tvCrop;

    private void getInfo(){
        cropImageView = findViewById(R.id.cropImageView);

        ivBack = findViewById(R.id.ivBack);
        ivRotate = findViewById(R.id.ivRotate);
        ivFlip = findViewById(R.id.ivFlip);

        tvCrop = findViewById(R.id.tvCrop);
    }

    private void setInfo(){

        /**
         * initializing Banner Ad
         */
        initBannerAdView();

        String sUri = getIntent().getStringExtra(CROPPER_IMAGE_URI);

        if(sUri != null){
            this.imageUri = Uri.parse(getIntent().getStringExtra(CROPPER_IMAGE_URI));
            cropImageView.setGuidelines(CropImageView.Guidelines.ON);
            cropImageView.setImageBitmap(getBitmapFromUri(imageUri));
        }else{
            cropImageView.setGuidelines(CropImageView.Guidelines.ON);
            cropImageView.setImageBitmap(getBitmap(ResourceManager.bitmap));
        }

        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {

                Bitmap resultBitmap = result.getBitmap();
                Log.i(TAG, "onCropImageComplete resultBitmap: " + resultBitmap);

                Bitmap cropped = cropImageView.getCroppedImage();
                Log.i(TAG, "onCropImageComplete cropped: " + cropped);

                ResourceManager.bitmap = resultBitmap;

                setResult(RESULT_OK);
                finish();
            }
        });

        ivBack.setOnClickListener(this);
        ivRotate.setOnClickListener(this);
        ivFlip.setOnClickListener(this);

        tvCrop.setOnClickListener(this);
    }

    private void initBannerAdView() {
        AdsUtil.showBannerAd(this, (AdView) findViewById(R.id.banner_adview_bottom));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropper);

        getInfo();
        setInfo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivRotate:
                cropImageView.rotateImage(90);
                break;
            case R.id.ivFlip:
                cropImageView.flipImageHorizontally();
                break;
            case R.id.tvCrop:
                AdsUtil.showInterstitialAd(ImageCropperActivity.this, new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        cropImageView.getCroppedImageAsync();
                        AdsUtil.preloadInterstitialAd(ImageCropperActivity.this);

                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdLoaded();
                        cropImageView.getCroppedImageAsync();
                        AdsUtil.preloadInterstitialAd(ImageCropperActivity.this);
                    }
                });
                break;
        }
    }

    private Bitmap getBitmap(Bitmap bitmap) {
        return BitmapUtil.getResizedBitmap(bitmap, 800);
    }

    @Nullable
    private Bitmap getBitmapFromUri(Uri imageUri) {
        try {
            return BitmapUtil.getResizedBitmap(
                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri),
                    800);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
