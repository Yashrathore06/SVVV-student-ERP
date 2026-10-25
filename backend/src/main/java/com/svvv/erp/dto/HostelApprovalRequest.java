package com.svvv.erp.dto;

public class HostelApprovalRequest {
    private String allottedRoomNo;
    private String wardenName;
    private String status; // "APPROVED" or "REJECTED"

    public HostelApprovalRequest() {}

    public String getAllottedRoomNo() {
        return allottedRoomNo;
    }

    public void setAllottedRoomNo(String allottedRoomNo) {
        this.allottedRoomNo = allottedRoomNo;
    }

    public String getWardenName() {
        return wardenName;
    }

    public void setWardenName(String wardenName) {
        this.wardenName = wardenName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
