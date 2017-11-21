package com.hqj.universityfinance.javabean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by wang on 17-11-20.
 */

public class ProjectInfo extends BmobObject implements Serializable{

    private String z_id;
    private String z_name;
    private Integer z_status;
    private Integer z_sum;
    private String z_time;
    private String z_quota;
    private String z_describe;

    public String getZ_id() {
        return z_id;
    }

    public void setZ_id(String z_id) {
        this.z_id = z_id;
    }

    public String getZ_name() {
        return z_name;
    }

    public void setZ_name(String z_name) {
        this.z_name = z_name;
    }

    public Integer getZ_status() {
        return z_status;
    }

    public void setZ_status(Integer z_status) {
        this.z_status = z_status;
    }

    public Integer getZ_sum() {
        return z_sum;
    }

    public void setZ_sum(Integer z_sum) {
        this.z_sum = z_sum;
    }

    public String getZ_quota() {
        return z_quota;
    }

    public void setZ_quota(String z_quota) {
        this.z_quota = z_quota;
    }

    public String getZ_time() {
        return z_time;
    }

    public void setZ_time(String z_time) {
        this.z_time = z_time;
    }

    public String getZ_describe() {
        return z_describe;
    }

    public void setZ_describe(String z_describe) {
        this.z_describe = z_describe;
    }
}
