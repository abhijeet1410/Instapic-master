package com.savedPhotos.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.rsmapps.selfieall.R;

import java.util.List;


public class AdapterSlide extends PagerAdapter {

    private LayoutInflater inflater;
    private Activity context;
    private List<String> arrayList;

    public AdapterSlide(Activity context, List<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_hoarding, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);

        Bitmap b = BitmapFactory.decodeFile(arrayList.get(position));
        myImage.setImageBitmap(b);

        myImage.setOnTouchListener(new ImageMatrixTouchHandler(myImage.getContext()));

        //myImage.setImageResource(images.get(position));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}