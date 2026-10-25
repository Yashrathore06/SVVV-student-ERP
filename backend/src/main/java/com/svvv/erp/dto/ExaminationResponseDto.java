package com.svvv.erp.dto;

import java.util.List;

public class ExaminationResponseDto {
    private Long studentId;
    private String studentName;
    private String enrollmentNo;
    private String branch;
    private Integer currentSemester;
    private String currentSemesterRoman;
    private String sessionTitle;

    // KPI 1: CGPA
    private Double cgpa;
    private String cgpaDifferenceText; // "+0.18 vs Sem V • First Class with Distinction Track"
    private Double cgpaProgressPercent; // e.g. 84.2

    // KPI 2: SGPA Last Sem
    private Double sgpaLastSem;
    private Integer totalCreditsSecured;
    private Integer totalCreditsRequired;
    private Double sgpaProgressPercent; // e.g. 85.0

    // KPI 3: Backlogs
    private Integer activeBacklogs;
    private String backlogStatusTag; // "Clean Record • All Cleared" or "Active Backlogs Pending"
    private String backlogTagClass; // "green" or "red"
    private String degreeEligibilityText; // "Eligible for Degree with Honors"

    // Hall Ticket details
    private HallTicketDto hallTicket;

    // Semester Results list
    private List<SemesterResultDto> semesterResults;

    public ExaminationResponseDto() {}

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEnrollmentNo() { return enrollmentNo; }
    public void setEnrollmentNo(String enrollmentNo) { this.enrollmentNo = enrollmentNo; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public Integer getCurrentSemester() { return currentSemester; }
    public void setCurrentSemester(Integer currentSemester) { this.currentSemester = currentSemester; }

    public String getCurrentSemesterRoman() { return currentSemesterRoman; }
    public void setCurrentSemesterRoman(String currentSemesterRoman) { this.currentSemesterRoman = currentSemesterRoman; }

    public String getSessionTitle() { return sessionTitle; }
    public void setSessionTitle(String sessionTitle) { this.sessionTitle = sessionTitle; }

    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }

    public String getCgpaDifferenceText() { return cgpaDifferenceText; }
    public void setCgpaDifferenceText(String cgpaDifferenceText) { this.cgpaDifferenceText = cgpaDifferenceText; }

    public Double getCgpaProgressPercent() { return cgpaProgressPercent; }
    public void setCgpaProgressPercent(Double cgpaProgressPercent) { this.cgpaProgressPercent = cgpaProgressPercent; }

    public Double getSgpaLastSem() { return sgpaLastSem; }
    public void setSgpaLastSem(Double sgpaLastSem) { this.sgpaLastSem = sgpaLastSem; }

    public Integer getTotalCreditsSecured() { return totalCreditsSecured; }
    public void setTotalCreditsSecured(Integer totalCreditsSecured) { this.totalCreditsSecured = totalCreditsSecured; }

    public Integer getTotalCreditsRequired() { return totalCreditsRequired; }
    public void setTotalCreditsRequired(Integer totalCreditsRequired) { this.totalCreditsRequired = totalCreditsRequired; }

    public Double getSgpaProgressPercent() { return sgpaProgressPercent; }
    public void setSgpaProgressPercent(Double sgpaProgressPercent) { this.sgpaProgressPercent = sgpaProgressPercent; }

    public Integer getActiveBacklogs() { return activeBacklogs; }
    public void setActiveBacklogs(Integer activeBacklogs) { this.activeBacklogs = activeBacklogs; }

    public String getBacklogStatusTag() { return backlogStatusTag; }
    public void setBacklogStatusTag(String backlogStatusTag) { this.backlogStatusTag = backlogStatusTag; }

    public String getBacklogTagClass() { return backlogTagClass; }
    public void setBacklogTagClass(String backlogTagClass) { this.backlogTagClass = backlogTagClass; }

    public String getDegreeEligibilityText() { return degreeEligibilityText; }
    public void setDegreeEligibilityText(String degreeEligibilityText) { this.degreeEligibilityText = degreeEligibilityText; }

    public HallTicketDto getHallTicket() { return hallTicket; }
    public void setHallTicket(HallTicketDto hallTicket) { this.hallTicket = hallTicket; }

    public List<SemesterResultDto> getSemesterResults() { return semesterResults; }
    public void setSemesterResults(List<SemesterResultDto> semesterResults) { this.semesterResults = semesterResults; }
}
