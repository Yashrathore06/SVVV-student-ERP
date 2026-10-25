package com.svvv.erp.dto;

public class ElectiveCourseDto {
    private Long id;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private String domain;
    private String description;
    private Integer totalSeats;
    private Integer enrolledSeats;
    private Integer remainingSeats;
    private boolean isFull;
    private boolean isHighDemand;
    private String status; // "OPEN" or "CLOSED"
    
    // Classroom Venue on SVVV Campus
    private String venue; // e.g., "SRK Block — Room #310"
    private String facultyName;
    private String schedule; // e.g., "Mon, Wed, Fri • 11:30 AM - 12:30 PM"

    public ElectiveCourseDto() {}

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return id;
    }

    public Integer getFilledSeats() {
        return enrolledSeats;
    }

    public Integer getEnrolledCount() {
        return enrolledSeats;
    }

    public Double getCapacityPercentage() {
        if (totalSeats == null || totalSeats == 0 || enrolledSeats == null) return 0.0;
        return Math.round(((double) enrolledSeats / (double) totalSeats) * 1000.0) / 10.0;
    }

    public String getDepartment() {
        return "Computer Science & Engineering";
    }

    public String getInstructor() {
        return facultyName != null ? facultyName : "Faculty Lead";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getEnrolledSeats() {
        return enrolledSeats;
    }

    public void setEnrolledSeats(Integer enrolledSeats) {
        this.enrolledSeats = enrolledSeats;
    }

    public Integer getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(Integer remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public boolean isHighDemand() {
        return isHighDemand;
    }

    public void setHighDemand(boolean highDemand) {
        isHighDemand = highDemand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
