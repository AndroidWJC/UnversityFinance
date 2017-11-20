package com.hqj.universityfinance.javabean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by wang on 17-11-20.
 */

public class ProjectInfo extends BmobObject {

    private String p_id;
    private String p_name;
    private Integer p_status;
    private Integer p_sum;
    private String p_time;
    private String p_quota;
    private String p_describe;

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public Integer getP_status() {
        return p_status;
    }

    public void setP_status(Integer p_status) {
        this.p_status = p_status;
    }

    public Integer getP_sum() {
        return p_sum;
    }

    public void setP_sum(Integer p_sum) {
        this.p_sum = p_sum;
    }

    public String getP_time() {
        return p_time;
    }

    public void setP_time(String p_time) {
        this.p_time = p_time;
    }

    public String getP_quota() {
        return p_quota;
    }

    public void setP_quota(String p_quota) {
        this.p_quota = p_quota;
    }

    public String getP_describe() {
        return p_describe;
    }

    public void setP_describe(String p_describe) {
        this.p_describe = p_describe;
    }

}
