package com.wheel.perfect.util;

import java.io.Serializable;

/**
 * Created by 孙科技 on 2018/5/3.
 */
public class TestBean implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private String name;
    private String imgUrl;
}
