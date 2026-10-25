package com.svvv.erp.controller;

import com.svvv.erp.dto.HostelApplicationRequest;
import com.svvv.erp.entity.HostelApplication;
import com.svvv.erp.service.HostelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/hostel")
public class HostelStudentController {

    @Autowired
    private HostelService hostelService;

    @GetMapping("/profile/{studentId}")
    public ResponseEntity<?> getStudentProfile(@PathVariable Long studentId) {
        try {
            Map<String, Object> profile = hostelService.getStudentProfileForHostel(studentId);
            return ResponseEntity.ok(profile);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    @GetMapping("/application/{studentId}")
    public ResponseEntity<?> getApplicationStatus(@PathVariable Long studentId) {
        try {
            Optional<HostelApplication> appOpt = hostelService.getLatestApplicationForStudent(studentId);
            Map<String, Object> response = new HashMap<>();
            if (appOpt.isPresent()) {
                response.put("hasApplication", true);
                response.put("application", appOpt.get());
            } else {
                response.put("hasApplication", false);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    @PostMapping("/apply")
    public ResponseEntity<?> submitApplication(@RequestBody HostelApplicationRequest request) {
        try {
            HostelApplication application = hostelService.submitApplication(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Hostel application submitted successfully. Warden verification is pending.");
            response.put("application", application);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }
}
