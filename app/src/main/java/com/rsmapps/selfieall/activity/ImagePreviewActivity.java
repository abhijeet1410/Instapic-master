package com.rsmapps.selfieall.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.helper.Utils;

public class ImagePreviewActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ImageView me = findViewById(R.id.me);
        ImageView celebrity = findViewById(R.id.celebrity);
        View next = findViewById(R.id.next);
        if (getIntent().getStringExtra("url").startsWith("celebrity/image")){
            Drawable drawable = Utils.getDrawableFromAsset(this, getIntent().getStringExtra("url"));
            celebrity.setImageDrawable(drawable);
        }else {
            Glide.with(this).load(getIntent().getStringExtra("url")).apply(new RequestOptions().placeholder(R.drawable.actor_placeholder).error(R.drawable.actor_placeholder)).into(celebrity);
        }
        Glide.with(this).load(getIntent().getStringExtra("IMG_PATH")).apply(new RequestOptions().placeholder(R.drawable.actor_placeholder).error(R.drawable.actor_placeholder).diskCacheStrategy(DiskCacheStrategy.NONE)).into(me);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImagePreviewActivity.this, EffectsFilterActivity.class);
                if (getIntent().hasExtra("url")) {
                    intent.putExtra("url", getIntent().getStringExtra("url"));
                }
                intent.putExtra("IMG_PATH", getIntent().getStringExtra("IMG_PATH"));
                startActivity(intent);
                finish();
            }
        });
    }



}
