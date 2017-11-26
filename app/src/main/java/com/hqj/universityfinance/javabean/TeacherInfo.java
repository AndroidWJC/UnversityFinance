package com.hqj.universityfinance.javabean;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by wang on 17-11-26.
 */

public class TeacherInfo extends BmobBaseBean {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public Integer getT_id() {
        return t_id;
    }

    public void setT_id(Integer t_id) {
        this.t_id = t_id;
    }

    private Integer t_id;
    private String name;
    private String password;
    private BmobFile photo;
}
