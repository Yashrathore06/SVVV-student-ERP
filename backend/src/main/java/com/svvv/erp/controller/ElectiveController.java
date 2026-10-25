package com.svvv.erp.controller;

import com.svvv.erp.dto.ElectiveEnrollRequest;
import com.svvv.erp.dto.ElectiveOverviewResponse;
import com.svvv.erp.service.ElectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/electives")
public class ElectiveController {

    @Autowired
    private ElectiveService electiveService;

    @GetMapping
    public ResponseEntity<?> getElectives(@RequestParam(required = false) Long studentId) {
        try {
            if (studentId != null) {
                return ResponseEntity.ok(electiveService.getOverviewForStudent(studentId));
            } else {
                return ResponseEntity.ok(electiveService.getAllCoursesWithSeats());
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Failed to load electives: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAdminElectives() {
        try {
            return ResponseEntity.ok(electiveService.getAdminElectivesDetail());
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Failed to load admin electives: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/overview")
    public ResponseEntity<?> getElectivesOverview(@RequestParam Long studentId) {
        try {
            ElectiveOverviewResponse overview = electiveService.getOverviewForStudent(studentId);
            return ResponseEntity.ok(overview);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Failed to load electives: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getElectivesStats() {
        try {
            return ResponseEntity.ok(electiveService.getElectivesAdminStats());
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Failed to load elective stats: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> enrollStudent(@RequestBody ElectiveEnrollRequest request) {
        try {
            if (request.getStudentId() == null || request.getCourseId() == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "studentId and courseId are required.");
                return ResponseEntity.badRequest().body(error);
            }

            ElectiveOverviewResponse response = electiveService.enrollStudent(request.getStudentId(), request.getCourseId());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Enrollment failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @DeleteMapping("/enrollment/student/{studentId}")
    public ResponseEntity<?> unenrollStudent(@PathVariable Long studentId) {
        boolean removed = electiveService.deleteEnrollmentByStudentId(studentId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", removed);
        resp.put("message", removed ? "Elective enrollment removed." : "No enrollment found.");
        return ResponseEntity.ok(resp);
    }
}
