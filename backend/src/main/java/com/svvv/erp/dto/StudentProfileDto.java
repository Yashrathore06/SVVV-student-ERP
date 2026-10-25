package com.svvv.erp.dto;

import java.util.List;

public class StudentProfileDto {

    private Long id;
    private Long userId;
    private String enrollmentNo;
    private String name;
    private String email;
    private String phone;
    private String branch;
    private Integer semester;
    private Double cgpa;
    private Integer backlogs;
    private Double familyIncome;
    private String address;
    private String gender;
    private List<ResumeDto> resumes;
    private ResumeDto activeResume;

    public StudentProfileDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(String enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public Integer getBacklogs() {
        return backlogs;
    }

    public void setBacklogs(Integer backlogs) {
        this.backlogs = backlogs;
    }

    public Double getFamilyIncome() {
        return familyIncome;
    }

    public void setFamilyIncome(Double familyIncome) {
        this.familyIncome = familyIncome;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<ResumeDto> getResumes() {
        return resumes;
    }

    public void setResumes(List<ResumeDto> resumes) {
        this.resumes = resumes;
    }

    public ResumeDto getActiveResume() {
        return activeResume;
    }

    public void setActiveResume(ResumeDto activeResume) {
        this.activeResume = activeResume;
    }
}
