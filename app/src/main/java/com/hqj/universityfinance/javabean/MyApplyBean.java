package com.hqj.universityfinance.javabean;

import java.io.Serializable;

/**
 * Created by wang on 17-10-26.
 */

public class MyApplyBean implements Serializable{

    private String studentId;
    private String projectId;
    private String status;
    private String score;
    private String job;
    private String prize;
    private String honor;
    private String reason;
    private String time;
    private String verifyTId;
    private int verifyResult;
    private String loanSum;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getHonor() {
        return honor;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVerifyTId() {
        return verifyTId;
    }

    public void setVerifyTId(String verifyTId) {
        this.verifyTId = verifyTId;
    }

    public int getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(int verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getLoanSum() {
        return loanSum;
    }

    public void setLoanSum(String loanSum) {
        this.loanSum = loanSum;
    }
}
