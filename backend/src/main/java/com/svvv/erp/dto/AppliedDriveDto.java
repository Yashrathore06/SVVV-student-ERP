package com.svvv.erp.dto;

public class AppliedDriveDto {
    private Long applicationId;
    private Long companyId;
    private String companyName;
    private String logoCode;
    private String driveRef;
    private String roleApplied;
    private String appliedDate;
    private String currentStatus; // "APPLIED", "SHORTLISTED", "ASSESSMENT_CLEARED", "REJECTED"
    private String statusBadgeText; // e.g., "Shortlisted for Round 2", "Online Assessment Cleared"
    private String statusNote; // e.g. "Technical Interview: Sep 05, 10:30 AM"
    private boolean callLetterAvailable;
    private String callLetterRef;

    public AppliedDriveDto() {}

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public String getDriveRef() {
        return driveRef;
    }

    public void setDriveRef(String driveRef) {
        this.driveRef = driveRef;
    }

    public String getRoleApplied() {
        return roleApplied;
    }

    public void setRoleApplied(String roleApplied) {
        this.roleApplied = roleApplied;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getStatusBadgeText() {
        return statusBadgeText;
    }

    public void setStatusBadgeText(String statusBadgeText) {
        this.statusBadgeText = statusBadgeText;
    }

    public String getStatusNote() {
        return statusNote;
    }

    public void setStatusNote(String statusNote) {
        this.statusNote = statusNote;
    }

    public boolean isCallLetterAvailable() {
        return callLetterAvailable;
    }

    public void setCallLetterAvailable(boolean callLetterAvailable) {
        this.callLetterAvailable = callLetterAvailable;
    }

    public String getCallLetterRef() {
        return callLetterRef;
    }

    public void setCallLetterRef(String callLetterRef) {
        this.callLetterRef = callLetterRef;
    }
}
