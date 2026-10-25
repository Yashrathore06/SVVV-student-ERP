package com.svvv.erp.dto;

public class BonafideApplyRequest {

    private Long studentId;
    private String fatherName;
    private String purpose;
    private String remarks;

    public BonafideApplyRequest() {}

    public BonafideApplyRequest(Long studentId, String fatherName, String purpose, String remarks) {
        this.studentId = studentId;
        this.fatherName = fatherName;
        this.purpose = purpose;
        this.remarks = remarks;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
