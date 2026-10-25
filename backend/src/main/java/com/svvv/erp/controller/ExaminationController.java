package com.svvv.erp.controller;

import com.svvv.erp.dto.ExaminationResponseDto;
import com.svvv.erp.service.ExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/examination")
public class ExaminationController {

    @Autowired
    private ExaminationService examinationService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ExaminationResponseDto> getExaminationDetailsForStudent(@PathVariable Long studentId) {
        try {
            ExaminationResponseDto response = examinationService.getExaminationDetailsForStudent(studentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
