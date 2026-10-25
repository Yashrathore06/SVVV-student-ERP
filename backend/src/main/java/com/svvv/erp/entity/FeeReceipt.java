package com.svvv.erp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fee_receipts")
public class FeeReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, length = 50)
    private String semester; // e.g. "Semester VII", "Semester VI"

    @Column(name = "transaction_id", nullable = false, length = 100)
    private String transactionId;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "amount_paid", nullable = false)
    private Double amountPaid;

    @Column(name = "payment_mode", nullable = false, length = 50)
    private String paymentMode;

    @Column(nullable = false, length = 50)
    private String status; // PAID, FAILED

    @Column(name = "receipt_pdf_path", length = 255)
    private String receiptPdfPath;

    public FeeReceipt() {}

    public FeeReceipt(Student student, String semester, String transactionId, LocalDate paymentDate, Double amountPaid, String paymentMode, String status, String receiptPdfPath) {
        this.student = student;
        this.semester = semester;
        this.transactionId = transactionId;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.paymentMode = paymentMode;
        this.status = status;
        this.receiptPdfPath = receiptPdfPath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReceiptPdfPath() { return receiptPdfPath; }
    public void setReceiptPdfPath(String receiptPdfPath) { this.receiptPdfPath = receiptPdfPath; }
}
