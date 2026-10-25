package com.svvv.erp.dto;

public class PlacementApplyRequest {
    private Long studentId;
    private Long companyId;

    public PlacementApplyRequest() {}

    public PlacementApplyRequest(Long studentId, Long companyId) {
        this.studentId = studentId;
        this.companyId = companyId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
