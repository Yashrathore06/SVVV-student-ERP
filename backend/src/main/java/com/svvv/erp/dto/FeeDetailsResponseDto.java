package com.svvv.erp.dto;

import java.util.List;

public class FeeDetailsResponseDto {
    private Long studentId;
    private String studentName;
    private String enrollmentNo;
    private String branch;
    private Integer semester;
    private String semesterRoman;
    private String academicYear;
    private String registrationCycle;

    // Financial Overview
    private Double totalPayableAmount;
    private Double paidAmount;
    private Double dueAmount;
    private String status; // PAID, PENDING, PARTIALLY_PAID
    private String financeStatusText;

    // KPI 1: Current Semester
    private String currentSemesterTitle;
    private String currentSemesterSub;

    // KPI 2: Semester Fee Amount
    private Double semesterFeeAmount;
    private String feeScheduledTag;

    // KPI 3: Dues & Hall Ticket Status
    private String duesTitle;
    private String duesSubtitle;
    private String duesBadgeText;
    private String duesBadgeClass;
    private String hallTicketStatus;
    private String hallTicketStatusClass;

    // Fee Breakdown Particulars
    private Double tuitionFee;
    private Double examFee;
    private Double libraryResourceFee;

    // Payment Portal State
    private Boolean isFeePaid;
    private String reconciliationTitle;
    private String reconciliationMessage;
    private String paymentButtonText;
    private Boolean isPaymentEnabled;

    // Receipts History
    private List<FeeReceiptDto> receipts;

    public FeeDetailsResponseDto() {}

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEnrollmentNo() { return enrollmentNo; }
    public void setEnrollmentNo(String enrollmentNo) { this.enrollmentNo = enrollmentNo; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public String getSemesterRoman() { return semesterRoman; }
    public void setSemesterRoman(String semesterRoman) { this.semesterRoman = semesterRoman; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public String getRegistrationCycle() { return registrationCycle; }
    public void setRegistrationCycle(String registrationCycle) { this.registrationCycle = registrationCycle; }

    public Double getTotalPayableAmount() { return totalPayableAmount; }
    public void setTotalPayableAmount(Double totalPayableAmount) { this.totalPayableAmount = totalPayableAmount; }

    public Double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(Double paidAmount) { this.paidAmount = paidAmount; }

    public Double getDueAmount() { return dueAmount; }
    public void setDueAmount(Double dueAmount) { this.dueAmount = dueAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFinanceStatusText() { return financeStatusText; }
    public void setFinanceStatusText(String financeStatusText) { this.financeStatusText = financeStatusText; }

    public String getCurrentSemesterTitle() { return currentSemesterTitle; }
    public void setCurrentSemesterTitle(String currentSemesterTitle) { this.currentSemesterTitle = currentSemesterTitle; }

    public String getCurrentSemesterSub() { return currentSemesterSub; }
    public void setCurrentSemesterSub(String currentSemesterSub) { this.currentSemesterSub = currentSemesterSub; }

    public Double getSemesterFeeAmount() { return semesterFeeAmount; }
    public void setSemesterFeeAmount(Double semesterFeeAmount) { this.semesterFeeAmount = semesterFeeAmount; }

    public String getFeeScheduledTag() { return feeScheduledTag; }
    public void setFeeScheduledTag(String feeScheduledTag) { this.feeScheduledTag = feeScheduledTag; }

    public String getDuesTitle() { return duesTitle; }
    public void setDuesTitle(String duesTitle) { this.duesTitle = duesTitle; }

    public String getDuesSubtitle() { return duesSubtitle; }
    public void setDuesSubtitle(String duesSubtitle) { this.duesSubtitle = duesSubtitle; }

    public String getDuesBadgeText() { return duesBadgeText; }
    public void setDuesBadgeText(String duesBadgeText) { this.duesBadgeText = duesBadgeText; }

    public String getDuesBadgeClass() { return duesBadgeClass; }
    public void setDuesBadgeClass(String duesBadgeClass) { this.duesBadgeClass = duesBadgeClass; }

    public String getHallTicketStatus() { return hallTicketStatus; }
    public void setHallTicketStatus(String hallTicketStatus) { this.hallTicketStatus = hallTicketStatus; }

    public String getHallTicketStatusClass() { return hallTicketStatusClass; }
    public void setHallTicketStatusClass(String hallTicketStatusClass) { this.hallTicketStatusClass = hallTicketStatusClass; }

    public Double getTuitionFee() { return tuitionFee; }
    public void setTuitionFee(Double tuitionFee) { this.tuitionFee = tuitionFee; }

    public Double getExamFee() { return examFee; }
    public void setExamFee(Double examFee) { this.examFee = examFee; }

    public Double getLibraryResourceFee() { return libraryResourceFee; }
    public void setLibraryResourceFee(Double libraryResourceFee) { this.libraryResourceFee = libraryResourceFee; }

    public Boolean getIsFeePaid() { return isFeePaid; }
    public void setIsFeePaid(Boolean isFeePaid) { this.isFeePaid = isFeePaid; }

    public String getReconciliationTitle() { return reconciliationTitle; }
    public void setReconciliationTitle(String reconciliationTitle) { this.reconciliationTitle = reconciliationTitle; }

    public String getReconciliationMessage() { return reconciliationMessage; }
    public void setReconciliationMessage(String reconciliationMessage) { this.reconciliationMessage = reconciliationMessage; }

    public String getPaymentButtonText() { return paymentButtonText; }
    public void setPaymentButtonText(String paymentButtonText) { this.paymentButtonText = paymentButtonText; }

    public Boolean getIsPaymentEnabled() { return isPaymentEnabled; }
    public void setIsPaymentEnabled(Boolean isPaymentEnabled) { this.isPaymentEnabled = isPaymentEnabled; }

    public List<FeeReceiptDto> getReceipts() { return receipts; }
    public void setReceipts(List<FeeReceiptDto> receipts) { this.receipts = receipts; }
}
