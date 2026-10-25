package com.svvv.erp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "job_role", nullable = false, length = 100)
    private String jobRole;

    @Column(name = "package_ctc", nullable = false)
    private Double packageCtc; // in LPA, e.g., 6.5, 12.0

    @Column(name = "min_cgpa", nullable = false)
    private Double minCgpa;

    @Column(name = "allowed_branch", nullable = false, length = 150)
    private String allowedBranch; // e.g., "CSE,IT" or "ALL"

    @Column(name = "max_backlogs", nullable = false)
    private Integer maxBacklogs = 0;

    private LocalDate deadline;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE"; // "ACTIVE" or "CLOSED"

    public Company() {}

    public Company(String companyName, String jobRole, Double packageCtc, Double minCgpa,
                   String allowedBranch, Integer maxBacklogs, LocalDate deadline, String status) {
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.packageCtc = packageCtc;
        this.minCgpa = minCgpa;
        this.allowedBranch = allowedBranch;
        this.maxBacklogs = maxBacklogs;
        this.deadline = deadline;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public Double getPackageCtc() {
        return packageCtc;
    }

    public void setPackageCtc(Double packageCtc) {
        this.packageCtc = packageCtc;
    }

    public Double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(Double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public String getAllowedBranch() {
        return allowedBranch;
    }

    public void setAllowedBranch(String allowedBranch) {
        this.allowedBranch = allowedBranch;
    }

    public Integer getMaxBacklogs() {
        return maxBacklogs;
    }

    public void setMaxBacklogs(Integer maxBacklogs) {
        this.maxBacklogs = maxBacklogs;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
