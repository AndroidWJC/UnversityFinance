package com.hqj.universityfinance.javabean;

/**
 * Created by wang on 17-11-22.
 */

public class ApplyTableInfo extends BmobBaseBean {

    private Integer s_id;
    private String z_id;
    private Integer a_status;
    private Float a_score;
    private String a_job;
    private String a_prize;
    private String a_honor;
    private String apply_reason;
    private Integer verify_t_id;
    private Integer verify_result;
    private String a_loan_sum;

    public String getA_honor() {
        return a_honor;
    }

    public void setA_honor(String a_honor) {
        this.a_honor = a_honor;
    }

    public String getA_job() {
        return a_job;
    }

    public void setA_job(String a_job) {
        this.a_job = a_job;
    }

    public String getA_loan_sum() {
        return a_loan_sum;
    }

    public void setA_loan_sum(String a_loan_sum) {
        this.a_loan_sum = a_loan_sum;
    }

    public String getA_prize() {
        return a_prize;
    }

    public void setA_prize(String a_prize) {
        this.a_prize = a_prize;
    }

    public Float getA_score() {
        return a_score;
    }

    public void setA_score(Float a_score) {
        this.a_score = a_score;
    }

    public Integer getA_status() {
        return a_status;
    }

    public void setA_status(Integer a_status) {
        this.a_status = a_status;
    }

    public String getApply_reason() {
        return apply_reason;
    }

    public void setApply_reason(String apply_reason) {
        this.apply_reason = apply_reason;
    }

    public Integer getS_id() {
        return s_id;
    }

    public void setS_id(Integer s_id) {
        this.s_id = s_id;
    }

    public Integer getVerify_t_id() {
        return verify_t_id;
    }

    public void setVerify_t_id(Integer verify_t_id) {
        this.verify_t_id = verify_t_id;
    }

    public Integer getVerify_result() {
        return verify_result;
    }

    public void setVerify_result(Integer verify_result) {
        this.verify_result = verify_result;
    }

    public String getZ_id() {
        return z_id;
    }

    public void setZ_id(String z_id) {
        this.z_id = z_id;
    }

}
