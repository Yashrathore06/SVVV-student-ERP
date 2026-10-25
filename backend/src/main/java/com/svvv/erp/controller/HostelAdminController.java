package com.svvv.erp.controller;

import com.svvv.erp.dto.HostelApprovalRequest;
import com.svvv.erp.entity.HostelApplication;
import com.svvv.erp.service.HostelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hostel/admin")
public class HostelAdminController {

    @Autowired
    private HostelService hostelService;

    @GetMapping("/applications")
    public ResponseEntity<?> getAllApplications(@RequestParam(required = false) String status) {
        try {
            List<HostelApplication> applications = hostelService.getAllApplications(status);
            return ResponseEntity.ok(applications);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    @PostMapping("/approve/{applicationId}")
    public ResponseEntity<?> approveApplication(@PathVariable Long applicationId,
                                                @RequestBody(required = false) HostelApprovalRequest request) {
        try {
            if (request == null) {
                request = new HostelApprovalRequest();
                request.setStatus("APPROVED");
            }
            HostelApplication updated = hostelService.processApproval(applicationId, request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Application approved successfully. Room " + updated.getAllottedRoomNo() + " allotted.");
            response.put("application", updated);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    @PostMapping("/reject/{applicationId}")
    public ResponseEntity<?> rejectApplication(@PathVariable Long applicationId,
                                               @RequestBody(required = false) HostelApprovalRequest request) {
        try {
            if (request == null) {
                request = new HostelApprovalRequest();
            }
            request.setStatus("REJECTED");
            HostelApplication updated = hostelService.processApproval(applicationId, request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Application rejected.");
            response.put("application", updated);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }
}
