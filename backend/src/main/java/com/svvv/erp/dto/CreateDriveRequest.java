package com.svvv.erp.dto;

import java.time.LocalDate;

public class CreateDriveRequest {
    private String companyName;
    private String jobRole;
    private Double packageCtc;
    private Double minCgpa;
    private String allowedBranch;
    private Integer maxBacklogs;
    private LocalDate deadline;

    public CreateDriveRequest() {}

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public Double getPackageCtc() {
        return packageCtc;
    }

    public void setPackageCtc(Double packageCtc) {
        this.packageCtc = packageCtc;
    }

    public Double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(Double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public String getAllowedBranch() {
        return allowedBranch;
    }

    public void setAllowedBranch(String allowedBranch) {
        this.allowedBranch = allowedBranch;
    }

    public Integer getMaxBacklogs() {
        return maxBacklogs;
    }

    public void setMaxBacklogs(Integer maxBacklogs) {
        this.maxBacklogs = maxBacklogs;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
