package com.aligohershabir.photocollage.model;

import android.graphics.Bitmap;

public class GridImage {

    private int id;
    private Bitmap bitmap;
    private Boolean isCompressed;

    public GridImage(int id, Bitmap bitmap, Boolean isCompressed) {
        this.id = id;
        this.bitmap = bitmap;
        this.isCompressed = isCompressed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Boolean getCompressed() {
        return isCompressed;
    }

    public void setCompressed(Boolean compressed) {
        isCompressed = compressed;
    }
}
