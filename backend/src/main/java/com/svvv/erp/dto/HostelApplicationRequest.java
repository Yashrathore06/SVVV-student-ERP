package com.svvv.erp.dto;

public class HostelApplicationRequest {
    private Long studentId;
    private String hostelWing;
    private String roomOccupancyType;
    private Double baseRoomFee;
    private String climateFacility;
    private Double climateFeeAddon;
    private Double totalPayableFee;
    private String guardianContact;

    public HostelApplicationRequest() {}

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getHostelWing() {
        return hostelWing;
    }

    public void setHostelWing(String hostelWing) {
        this.hostelWing = hostelWing;
    }

    public String getRoomOccupancyType() {
        return roomOccupancyType;
    }

    public void setRoomOccupancyType(String roomOccupancyType) {
        this.roomOccupancyType = roomOccupancyType;
    }

    public Double getBaseRoomFee() {
        return baseRoomFee;
    }

    public void setBaseRoomFee(Double baseRoomFee) {
        this.baseRoomFee = baseRoomFee;
    }

    public String getClimateFacility() {
        return climateFacility;
    }

    public void setClimateFacility(String climateFacility) {
        this.climateFacility = climateFacility;
    }

    public Double getClimateFeeAddon() {
        return climateFeeAddon;
    }

    public void setClimateFeeAddon(Double climateFeeAddon) {
        this.climateFeeAddon = climateFeeAddon;
    }

    public Double getTotalPayableFee() {
        return totalPayableFee;
    }

    public void setTotalPayableFee(Double totalPayableFee) {
        this.totalPayableFee = totalPayableFee;
    }

    public String getGuardianContact() {
        return guardianContact;
    }

    public void setGuardianContact(String guardianContact) {
        this.guardianContact = guardianContact;
    }
}
