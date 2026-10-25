package com.svvv.erp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fee_structures")
public class FeeStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String branch;

    @Column(nullable = false)
    private Integer semester;

    @Column(name = "tuition_fee", nullable = false)
    private Double tuitionFee;

    @Column(name = "exam_fee", nullable = false)
    private Double examFee;

    @Column(name = "library_resource_fee", nullable = false)
    private Double libraryResourceFee;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    public FeeStructure() {}

    public FeeStructure(String branch, Integer semester, Double tuitionFee, Double examFee, Double libraryResourceFee) {
        this.branch = branch;
        this.semester = semester;
        this.tuitionFee = tuitionFee;
        this.examFee = examFee;
        this.libraryResourceFee = libraryResourceFee;
        this.totalAmount = (tuitionFee != null ? tuitionFee : 0.0) +
                           (examFee != null ? examFee : 0.0) +
                           (libraryResourceFee != null ? libraryResourceFee : 0.0);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public Double getTuitionFee() { return tuitionFee; }
    public void setTuitionFee(Double tuitionFee) { this.tuitionFee = tuitionFee; }

    public Double getExamFee() { return examFee; }
    public void setExamFee(Double examFee) { this.examFee = examFee; }

    public Double getLibraryResourceFee() { return libraryResourceFee; }
    public void setLibraryResourceFee(Double libraryResourceFee) { this.libraryResourceFee = libraryResourceFee; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}
