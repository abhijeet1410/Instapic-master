package com.rsmapps.selfieall.model;

import java.io.Serializable;
import java.util.ArrayList;


public class Celebrity implements Serializable{
    String id = "";
    String name = "";
    String gender = "";
    ArrayList<String> images = new ArrayList<>();
    private int image = 0;

    public Celebrity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
