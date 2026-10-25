package com.svvv.erp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "academic_performances")
public class AcademicPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;

    private Double cgpa;

    @Column(name = "sgpa_last_sem")
    private Double sgpaLastSem;

    @Column(name = "active_backlogs")
    private Integer activeBacklogs;

    @Column(name = "total_credits_secured")
    private Integer totalCreditsSecured;

    @Column(name = "total_credits_required")
    private Integer totalCreditsRequired;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public AcademicPerformance() {
        this.updatedAt = LocalDateTime.now();
    }

    public AcademicPerformance(Student student, Double cgpa, Double sgpaLastSem, Integer activeBacklogs, Integer totalCreditsSecured, Integer totalCreditsRequired) {
        this.student = student;
        this.cgpa = cgpa;
        this.sgpaLastSem = sgpaLastSem;
        this.activeBacklogs = activeBacklogs;
        this.totalCreditsSecured = totalCreditsSecured;
        this.totalCreditsRequired = totalCreditsRequired;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }

    public Double getSgpaLastSem() { return sgpaLastSem; }
    public void setSgpaLastSem(Double sgpaLastSem) { this.sgpaLastSem = sgpaLastSem; }

    public Integer getActiveBacklogs() { return activeBacklogs; }
    public void setActiveBacklogs(Integer activeBacklogs) { this.activeBacklogs = activeBacklogs; }

    public Integer getTotalCreditsSecured() { return totalCreditsSecured; }
    public void setTotalCreditsSecured(Integer totalCreditsSecured) { this.totalCreditsSecured = totalCreditsSecured; }

    public Integer getTotalCreditsRequired() { return totalCreditsRequired; }
    public void setTotalCreditsRequired(Integer totalCreditsRequired) { this.totalCreditsRequired = totalCreditsRequired; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
