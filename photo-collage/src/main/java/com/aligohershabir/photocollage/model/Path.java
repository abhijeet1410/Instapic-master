package com.aligohershabir.photocollage.model;

public class Path {


    private String image_url = "";
    private boolean isSelected = false;

    public Path() {
    }

    public Path(String image_url, boolean isSelected) {
        this.image_url = image_url;
        this.isSelected = isSelected;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
