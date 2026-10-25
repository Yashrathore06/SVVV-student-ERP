package com.svvv.erp.dto;

import java.time.LocalDate;

public class HallTicketDto {
    private Long id;
    private Integer semester;
    private String semesterRoman;
    private String sessionName;
    private LocalDate commencementDate;
    private String commencementDateFormatted;
    private String examCenter;
    private String reportingTime;
    private Double attendancePercentage;
    private Boolean isFeeCleared;
    private Boolean isAttendanceEligible;
    private Boolean isHallTicketEnabled;
    private String hallTicketNumber;
    private String statusBadgeText;
    private String statusBadgeClass;

    public HallTicketDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public String getSemesterRoman() { return semesterRoman; }
    public void setSemesterRoman(String semesterRoman) { this.semesterRoman = semesterRoman; }

    public String getSessionName() { return sessionName; }
    public void setSessionName(String sessionName) { this.sessionName = sessionName; }

    public LocalDate getCommencementDate() { return commencementDate; }
    public void setCommencementDate(LocalDate commencementDate) { this.commencementDate = commencementDate; }

    public String getCommencementDateFormatted() { return commencementDateFormatted; }
    public void setCommencementDateFormatted(String commencementDateFormatted) { this.commencementDateFormatted = commencementDateFormatted; }

    public String getExamCenter() { return examCenter; }
    public void setExamCenter(String examCenter) { this.examCenter = examCenter; }

    public String getReportingTime() { return reportingTime; }
    public void setReportingTime(String reportingTime) { this.reportingTime = reportingTime; }

    public Double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(Double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public Boolean getIsFeeCleared() { return isFeeCleared; }
    public void setIsFeeCleared(Boolean isFeeCleared) { this.isFeeCleared = isFeeCleared; }

    public Boolean getIsAttendanceEligible() { return isAttendanceEligible; }
    public void setIsAttendanceEligible(Boolean isAttendanceEligible) { this.isAttendanceEligible = isAttendanceEligible; }

    public Boolean getIsHallTicketEnabled() { return isHallTicketEnabled; }
    public void setIsHallTicketEnabled(Boolean isHallTicketEnabled) { this.isHallTicketEnabled = isHallTicketEnabled; }

    public String getHallTicketNumber() { return hallTicketNumber; }
    public void setHallTicketNumber(String hallTicketNumber) { this.hallTicketNumber = hallTicketNumber; }

    public String getStatusBadgeText() { return statusBadgeText; }
    public void setStatusBadgeText(String statusBadgeText) { this.statusBadgeText = statusBadgeText; }

    public String getStatusBadgeClass() { return statusBadgeClass; }
    public void setStatusBadgeClass(String statusBadgeClass) { this.statusBadgeClass = statusBadgeClass; }
}
