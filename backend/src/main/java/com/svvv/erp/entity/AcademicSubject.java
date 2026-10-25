package com.svvv.erp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "academic_subjects")
public class AcademicSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject_code", nullable = false, length = 50)
    private String subjectCode;

    @Column(name = "subject_name", nullable = false, length = 150)
    private String subjectName;

    @Column(nullable = false, length = 100)
    private String branch;

    @Column(nullable = false)
    private Integer semester;

    @Column(length = 255)
    private String notice;

    @Column(name = "course_outline_url", length = 255)
    private String courseOutlineUrl;

    @Column(length = 20)
    private String type = "THEORY"; // THEORY or PRACTICAL

    @Column(name = "faculty_name", length = 100)
    private String facultyName;

    private Integer credits = 4;

    public AcademicSubject() {}

    public AcademicSubject(String subjectCode, String subjectName, String branch, Integer semester, String notice, String courseOutlineUrl, String type, String facultyName, Integer credits) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.branch = branch;
        this.semester = semester;
        this.notice = notice;
        this.courseOutlineUrl = courseOutlineUrl;
        this.type = type;
        this.facultyName = facultyName;
        this.credits = credits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getCourseOutlineUrl() {
        return courseOutlineUrl;
    }

    public void setCourseOutlineUrl(String courseOutlineUrl) {
        this.courseOutlineUrl = courseOutlineUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }
}
