package com.srj.icsinspection.model;

public class PhotoPathModel {

    private String path;

    public PhotoPathModel(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "\n" + getPath() + "\n";
    }
}
