package com.svvv.erp.dto;

import java.time.LocalDate;

public class PlacementDriveDto {
    private Long id;
    private String companyName;
    private String logoCode; // e.g., "TCS", "WIP", "INF", "CTS"
    private String jobRole;
    private Double packageCtc;
    private String serviceBond; // e.g., "1 Year", "No Bond", "2 Years"
    private Double minCgpa;
    private String allowedBranch;
    private Integer maxBacklogs;
    private LocalDate deadline;
    private String status;
    private String driveRef; // e.g., "#TCS-2026-IND"

    // Automated Central Criteria Evaluation fields
    private boolean eligible;
    private String eligibilityReason;
    private boolean cgpaPassed;
    private boolean branchPassed;
    private boolean backlogsPassed;

    private Double studentCgpa;
    private String studentBranch;
    private Integer studentBacklogs;

    private boolean alreadyApplied;
    private String applicationStatus; // e.g. "APPLIED", "SHORTLISTED", "REJECTED"
    private String appliedDate;

    public PlacementDriveDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogoCode() {
        return logoCode;
    }

    public void setLogoCode(String logoCode) {
        this.logoCode = logoCode;
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

    public String getServiceBond() {
        return serviceBond;
    }

    public void setServiceBond(String serviceBond) {
        this.serviceBond = serviceBond;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriveRef() {
        return driveRef;
    }

    public void setDriveRef(String driveRef) {
        this.driveRef = driveRef;
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public String getEligibilityReason() {
        return eligibilityReason;
    }

    public void setEligibilityReason(String eligibilityReason) {
        this.eligibilityReason = eligibilityReason;
    }

    public boolean isCgpaPassed() {
        return cgpaPassed;
    }

    public void setCgpaPassed(boolean cgpaPassed) {
        this.cgpaPassed = cgpaPassed;
    }

    public boolean isBranchPassed() {
        return branchPassed;
    }

    public void setBranchPassed(boolean branchPassed) {
        this.branchPassed = branchPassed;
    }

    public boolean isBacklogsPassed() {
        return backlogsPassed;
    }

    public void setBacklogsPassed(boolean backlogsPassed) {
        this.backlogsPassed = backlogsPassed;
    }

    public Double getStudentCgpa() {
        return studentCgpa;
    }

    public void setStudentCgpa(Double studentCgpa) {
        this.studentCgpa = studentCgpa;
    }

    public String getStudentBranch() {
        return studentBranch;
    }

    public void setStudentBranch(String studentBranch) {
        this.studentBranch = studentBranch;
    }

    public Integer getStudentBacklogs() {
        return studentBacklogs;
    }

    public void setStudentBacklogs(Integer studentBacklogs) {
        this.studentBacklogs = studentBacklogs;
    }

    public boolean isAlreadyApplied() {
        return alreadyApplied;
    }

    public void setAlreadyApplied(boolean alreadyApplied) {
        this.alreadyApplied = alreadyApplied;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }
}
