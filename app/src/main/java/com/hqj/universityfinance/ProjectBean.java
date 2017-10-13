package com.hqj.universityfinance;

/**
 * Created by wang on 17-10-13.
 */

public class ProjectBean {
    private String projectId;
    private String projectName;
    private int projectStatus;
    private String projectSum;
    private String projectTime;
    private String projectQuota;
    private String projectDescribe;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(int projectStatus) {
        this.projectStatus = projectStatus;
    }

    public boolean projectIsOpen() {
        return projectStatus == 1;
    }

    public String getProjectSum() {
        return projectSum;
    }

    public void setProjectSum(String projectSum) {
        this.projectSum = projectSum;
    }

    public String getProjectTime() {
        return projectTime;
    }

    public void setProjectTime(String projectTime) {
        this.projectTime = projectTime;
    }

    public String getProjectQuota() {
        return projectQuota;
    }

    public void setProjectQuota(String projectQuota) {
        this.projectQuota = projectQuota;
    }

    public String getProjectDescribe() {
        return projectDescribe;
    }

    public void setProjectDescribe(String projectDescribe) {
        this.projectDescribe = projectDescribe;
    }
}
