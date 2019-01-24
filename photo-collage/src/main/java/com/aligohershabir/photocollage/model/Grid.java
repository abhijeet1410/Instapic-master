package com.aligohershabir.photocollage.model;

import android.graphics.drawable.Drawable;

public class Grid {

    public enum FileType {
        ONE_IMAGE_GRID(1), TWO_IMAGE_GRID(2), THREE_IMAGE_GRID(3), FOUR_IMAGE_GRID(4), FIVE_IMAGE_GRID(5), SIX_IMAGE_GRID(6),
        SEVEN_IMAGE_GRID(7),EIGHT_IMAGE_GRID(8),NINE_IMAGE_GRID(9);

        private int value;
        FileType(int value) {
            this.value = value;
        }
    }

    /*
     * Specific grid will be used according to this gridId
     */
    private int gridId;
    /*
     * Number of images this grid can have.
     */
    private int canHaveImages;

    /*
     * Path of image Drawable
     */
    private String fileDrawablePath;

    /*
     * drawable for grid type
     */
    private Drawable gridDrawable;

    public Grid(){

    }

    public Grid( int gridId, FileType fileType, String fileDrawablePath, Drawable drawable) {
        this.canHaveImages = fileType.value;
        this.fileDrawablePath = fileDrawablePath;
        this.gridDrawable = drawable;
        this.gridId = gridId;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public int getCanHaveImages() {
        return canHaveImages;
    }

    public void setCanHaveImages(int canHaveImages) {
        this.canHaveImages = canHaveImages;
    }

    public String getFileDrawablePath() {
        return fileDrawablePath;
    }

    public void setFileDrawablePath(String fileDrawablePath) {
        this.fileDrawablePath = fileDrawablePath;
    }

    public Drawable getGridDrawable() {
        return gridDrawable;
    }

    public void setGridDrawable(Drawable gridDrawable) {
        this.gridDrawable = gridDrawable;
    }

}
