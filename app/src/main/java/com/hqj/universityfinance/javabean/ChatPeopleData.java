package com.hqj.universityfinance.javabean;

/**
 * Created by wang on 17-11-26.
 */

public class ChatPeopleData extends BmobBaseBean {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String photo_url;
    private String name;
    private String summary;
    private String time;
}
