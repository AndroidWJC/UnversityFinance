package com.hqj.universityfinance;


import android.graphics.PointF;
import android.view.WindowManager;

/**
 * Created by wang on 17-9-9.
 */

public class BannerBean {

    private String title;
    private String title2;
    private String icon;
    private String intentUrl;

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setIntentUrl(String intentUrl) {
        this.intentUrl = intentUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public String getIntentUrl() {
        return intentUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }
}
