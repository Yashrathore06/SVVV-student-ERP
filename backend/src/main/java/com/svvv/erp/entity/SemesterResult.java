package com.svvv.erp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "semester_results")
public class SemesterResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "semester_name")
    private String semesterName;

    @Column(name = "semester_number")
    private Integer semesterNumber;

    @Column(name = "session_period")
    private String sessionPeriod;

    @Column(name = "enrolled_credits")
    private Integer enrolledCredits;

    @Column(name = "backlogs_in_sem")
    private Integer backlogsInSem;

    @Column(name = "sgpa")
    private Double sgpa;

    private String status; // "PASS", "ATKT", "FAIL"

    @Column(name = "grade_card_pdf_path")
    private String gradeCardPdfPath;

    public SemesterResult() {}

    public SemesterResult(Student student, String semesterName, Integer semesterNumber, String sessionPeriod, Integer enrolledCredits, Integer backlogsInSem, Double sgpa, String status, String gradeCardPdfPath) {
        this.student = student;
        this.semesterName = semesterName;
        this.semesterNumber = semesterNumber;
        this.sessionPeriod = sessionPeriod;
        this.enrolledCredits = enrolledCredits;
        this.backlogsInSem = backlogsInSem;
        this.sgpa = sgpa;
        this.status = status;
        this.gradeCardPdfPath = gradeCardPdfPath;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getSemesterName() { return semesterName; }
    public void setSemesterName(String semesterName) { this.semesterName = semesterName; }

    public Integer getSemesterNumber() { return semesterNumber; }
    public void setSemesterNumber(Integer semesterNumber) { this.semesterNumber = semesterNumber; }

    public String getSessionPeriod() { return sessionPeriod; }
    public void setSessionPeriod(String sessionPeriod) { this.sessionPeriod = sessionPeriod; }

    public Integer getEnrolledCredits() { return enrolledCredits; }
    public void setEnrolledCredits(Integer enrolledCredits) { this.enrolledCredits = enrolledCredits; }

    public Integer getBacklogsInSem() { return backlogsInSem; }
    public void setBacklogsInSem(Integer backlogsInSem) { this.backlogsInSem = backlogsInSem; }

    public Double getSgpa() { return sgpa; }
    public void setSgpa(Double sgpa) { this.sgpa = sgpa; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGradeCardPdfPath() { return gradeCardPdfPath; }
    public void setGradeCardPdfPath(String gradeCardPdfPath) { this.gradeCardPdfPath = gradeCardPdfPath; }
}
