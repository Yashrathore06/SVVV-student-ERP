package com.svvv.erp.dto;

public class SemesterResultDto {
    private Long id;
    private String semesterName;
    private Integer semesterNumber;
    private String semesterRoman;
    private String sessionPeriod;
    private Integer enrolledCredits;
    private Integer backlogsInSem;
    private Double sgpa;
    private String status;
    private String gradeCardPdfPath;

    public SemesterResultDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSemesterName() { return semesterName; }
    public void setSemesterName(String semesterName) { this.semesterName = semesterName; }

    public Integer getSemesterNumber() { return semesterNumber; }
    public void setSemesterNumber(Integer semesterNumber) { this.semesterNumber = semesterNumber; }

    public String getSemesterRoman() { return semesterRoman; }
    public void setSemesterRoman(String semesterRoman) { this.semesterRoman = semesterRoman; }

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
