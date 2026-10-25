package com.svvv.erp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hall_tickets")
public class HallTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private Integer semester;

    @Column(name = "session_name")
    private String sessionName;

    @Column(name = "commencement_date")
    private LocalDate commencementDate;

    @Column(name = "exam_center")
    private String examCenter;

    @Column(name = "reporting_time")
    private String reportingTime;

    @Column(name = "attendance_percentage")
    private Double attendancePercentage;

    @Column(name = "is_fee_cleared")
    private Boolean isFeeCleared;

    @Column(name = "hall_ticket_number")
    private String hallTicketNumber;

    public HallTicket() {}

    public HallTicket(Student student, Integer semester, String sessionName, LocalDate commencementDate, String examCenter, String reportingTime, Double attendancePercentage, Boolean isFeeCleared, String hallTicketNumber) {
        this.student = student;
        this.semester = semester;
        this.sessionName = sessionName;
        this.commencementDate = commencementDate;
        this.examCenter = examCenter;
        this.reportingTime = reportingTime;
        this.attendancePercentage = attendancePercentage;
        this.isFeeCleared = isFeeCleared;
        this.hallTicketNumber = hallTicketNumber;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public String getSessionName() { return sessionName; }
    public void setSessionName(String sessionName) { this.sessionName = sessionName; }

    public LocalDate getCommencementDate() { return commencementDate; }
    public void setCommencementDate(LocalDate commencementDate) { this.commencementDate = commencementDate; }

    public String getExamCenter() { return examCenter; }
    public void setExamCenter(String examCenter) { this.examCenter = examCenter; }

    public String getReportingTime() { return reportingTime; }
    public void setReportingTime(String reportingTime) { this.reportingTime = reportingTime; }

    public Double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(Double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public Boolean getIsFeeCleared() { return isFeeCleared; }
    public void setIsFeeCleared(Boolean isFeeCleared) { this.isFeeCleared = isFeeCleared; }

    public String getHallTicketNumber() { return hallTicketNumber; }
    public void setHallTicketNumber(String hallTicketNumber) { this.hallTicketNumber = hallTicketNumber; }
}
