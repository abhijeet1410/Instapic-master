package com.rsmapps.selfieall.model;

import java.io.Serializable;


public class Industry implements Serializable {
    String id = "";
    String name = "";
    String genderwise = "";
    private String image;


    public Industry() {
    }

    public Industry(String id, String name) {
        this.id = id;
        this.name = name;
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

    public String getGenderwise() {
        return genderwise;
    }

    public void setGenderwise(String genderwise) {
        this.genderwise = genderwise;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
