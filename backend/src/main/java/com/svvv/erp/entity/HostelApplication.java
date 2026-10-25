package com.svvv.erp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hostel_applications")
public class HostelApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "hostel_wing", nullable = false, length = 100)
    private String hostelWing;

    @Column(name = "room_occupancy_type", nullable = false, length = 100)
    private String roomOccupancyType;

    @Column(name = "base_room_fee", nullable = false)
    private Double baseRoomFee;

    @Column(name = "climate_facility", nullable = false, length = 100)
    private String climateFacility;

    @Column(name = "climate_fee_addon", nullable = false)
    private Double climateFeeAddon;

    @Column(name = "total_payable_fee", nullable = false)
    private Double totalPayableFee;

    @Column(name = "guardian_contact", length = 50)
    private String guardianContact;

    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "status", length = 50)
    private String status = "APPLIED";

    @Column(name = "allotted_room_no", length = 100)
    private String allottedRoomNo;

    @Column(name = "warden_name", length = 150)
    private String wardenName;

    public HostelApplication() {
        this.applicationDate = LocalDateTime.now();
        this.status = "APPLIED";
    }

    public HostelApplication(Student student, String hostelWing, String roomOccupancyType,
                             Double baseRoomFee, String climateFacility, Double climateFeeAddon,
                             Double totalPayableFee, String guardianContact) {
        this.student = student;
        this.hostelWing = hostelWing;
        this.roomOccupancyType = roomOccupancyType;
        this.baseRoomFee = baseRoomFee;
        this.climateFacility = climateFacility;
        this.climateFeeAddon = climateFeeAddon;
        this.totalPayableFee = totalPayableFee;
        this.guardianContact = guardianContact;
        this.applicationDate = LocalDateTime.now();
        this.status = "APPLIED";
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
}
