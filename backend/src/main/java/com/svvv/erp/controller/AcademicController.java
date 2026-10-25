package com.svvv.erp.controller;

import com.svvv.erp.dto.AcademicCalendarDto;
import com.svvv.erp.entity.AcademicSubject;
import com.svvv.erp.service.AcademicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academics")
public class AcademicController {

    @Autowired
    private AcademicService academicService;

    @GetMapping("/subjects")
    public ResponseEntity<List<AcademicSubject>> getSubjects(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) Integer semester) {
        return ResponseEntity.ok(academicService.getSubjectsForBranchAndSemester(branch, semester));
    }

    @GetMapping("/calendar")
    public ResponseEntity<AcademicCalendarDto> getCalendar() {
        return ResponseEntity.ok(academicService.getAcademicCalendar());
    }
}
