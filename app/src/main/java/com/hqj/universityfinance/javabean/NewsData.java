package com.hqj.universityfinance.javabean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by wang on 17-11-21.
 */

public class NewsData extends BmobBaseBean {

    private String title;
    private BmobFile picture;
    private String new_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public String getNew_url() {
        return new_url;
    }

    public void setNew_url(String new_url) {
        this.new_url = new_url;
    }
}
