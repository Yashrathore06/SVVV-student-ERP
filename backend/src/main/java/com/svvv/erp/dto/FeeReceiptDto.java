package com.svvv.erp.dto;

import java.time.LocalDate;

public class FeeReceiptDto {
    private Long id;
    private String semester;
    private String transactionId;
    private String paymentDate;
    private Double amountPaid;
    private String paymentMode;
    private String status;
    private String receiptPdfPath;

    public FeeReceiptDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReceiptPdfPath() { return receiptPdfPath; }
    public void setReceiptPdfPath(String receiptPdfPath) { this.receiptPdfPath = receiptPdfPath; }
}
