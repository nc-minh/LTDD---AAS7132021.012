package com.ncm.btl_android.lists;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private int id;
    private String name;
    private String timer;

    public Data() {
    }

    public Data(int id, String name, String timer) {
        this.id = id;
        this.name = name;
        this.timer = timer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    //    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                '}';
//    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("timer", timer);

        return result;
    }
}
