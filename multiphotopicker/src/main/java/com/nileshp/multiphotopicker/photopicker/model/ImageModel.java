package com.nileshp.multiphotopicker.photopicker.model;

public class ImageModel {
    int id;
    String name;
    String pathFile;
    String pathFolder;
    Boolean isSelected;

    public ImageModel(String name, String pathFile, String pathFolder) {
        this.name = name;
        this.pathFile = pathFile;
        this.pathFolder = pathFolder;
        this.isSelected = false;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getPathFile() {
        return this.pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathFolder() {
        return this.pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
