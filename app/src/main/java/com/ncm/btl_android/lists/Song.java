package com.ncm.btl_android.lists;

public class Song {
    int id;
    int resourceId;
    String name;

    public Song(int id, String name, int resourceId) {
        this.id = id;
        this.resourceId = resourceId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}