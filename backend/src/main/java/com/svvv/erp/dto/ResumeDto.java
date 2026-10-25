package com.svvv.erp.dto;

import java.time.LocalDateTime;

public class ResumeDto {
    private Long id;
    private Long studentId;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadedAt;

    public ResumeDto() {}

    public ResumeDto(Long id, Long studentId, String fileName, String filePath, LocalDateTime uploadedAt) {
        this.id = id;
        this.studentId = studentId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
