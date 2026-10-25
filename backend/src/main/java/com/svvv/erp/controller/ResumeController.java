package com.svvv.erp.controller;

import com.svvv.erp.dto.ResumeDto;
import com.svvv.erp.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/students/{studentId}/resumes/upload")
    public ResponseEntity<?> uploadResume(
            @PathVariable("studentId") Long studentId,
            @RequestParam("file") MultipartFile file) {
        try {
            ResumeDto uploaded = resumeService.uploadResume(studentId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(uploaded);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to store file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/students/{studentId}/resumes")
    public ResponseEntity<List<ResumeDto>> getResumes(@PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(resumeService.getStudentResumes(studentId));
    }

    @GetMapping("/resumes/{resumeId}/download")
    public ResponseEntity<Resource> downloadResume(@PathVariable("resumeId") Long resumeId) {
        try {
            Resource resource = resumeService.loadResumeFileAsResource(resumeId);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/resumes/{resumeId}")
    public ResponseEntity<Map<String, String>> deleteResume(@PathVariable("resumeId") Long resumeId) {
        boolean deleted = resumeService.deleteResume(resumeId);
        Map<String, String> response = new HashMap<>();
        if (deleted) {
            response.put("message", "Resume deleted successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Resume not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
