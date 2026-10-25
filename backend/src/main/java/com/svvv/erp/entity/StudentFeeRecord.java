package com.svvv.erp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_fee_records")
public class StudentFeeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private Integer semester;

    @Column(name = "total_payable_amount", nullable = false)
    private Double totalPayableAmount;

    @Column(name = "paid_amount", nullable = false)
    private Double paidAmount;

    @Column(name = "due_amount", nullable = false)
    private Double dueAmount;

    @Column(nullable = false, length = 50)
    private String status; // PAID, PENDING, PARTIALLY_PAID

    @Column(name = "academic_year", nullable = false, length = 50)
    private String academicYear;

    public StudentFeeRecord() {}

    public StudentFeeRecord(Student student, Integer semester, Double totalPayableAmount, Double paidAmount, String status, String academicYear) {
        this.student = student;
        this.semester = semester;
        this.totalPayableAmount = totalPayableAmount != null ? totalPayableAmount : 0.0;
        this.paidAmount = paidAmount != null ? paidAmount : 0.0;
        this.dueAmount = Math.max(0.0, this.totalPayableAmount - this.paidAmount);
        this.status = status;
        this.academicYear = academicYear;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public Double getTotalPayableAmount() { return totalPayableAmount; }
    public void setTotalPayableAmount(Double totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
        recalculateDue();
    }

    public Double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
        recalculateDue();
    }

    public Double getDueAmount() { return dueAmount; }
    public void setDueAmount(Double dueAmount) { this.dueAmount = dueAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public void recalculateDue() {
        double total = this.totalPayableAmount != null ? this.totalPayableAmount : 0.0;
        double paid = this.paidAmount != null ? this.paidAmount : 0.0;
        this.dueAmount = Math.max(0.0, total - paid);
        if (this.dueAmount == 0.0) {
            this.status = "PAID";
        } else if (paid > 0) {
            this.status = "PARTIALLY_PAID";
        } else {
            this.status = "PENDING";
        }
    }
}
