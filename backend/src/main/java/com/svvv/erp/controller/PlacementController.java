package com.svvv.erp.controller;

import com.svvv.erp.dto.AppliedDriveDto;
import com.svvv.erp.dto.PlacementApplyRequest;
import com.svvv.erp.dto.PlacementDriveDto;
import com.svvv.erp.service.PlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/placements")
public class PlacementController {

    @Autowired
    private PlacementService placementService;

    @GetMapping("/drives")
    public ResponseEntity<?> getAvailableDrives(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String branch) {
        try {
            List<PlacementDriveDto> drives = placementService.getDrivesForStudent(studentId, branch);
            return ResponseEntity.ok(drives);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Failed to fetch drives: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyToDrive(@RequestBody PlacementApplyRequest request) {
        try {
            if (request.getStudentId() == null || request.getCompanyId() == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "studentId and companyId are required.");
                return ResponseEntity.badRequest().body(error);
            }

            AppliedDriveDto applied = placementService.applyToDrive(request.getStudentId(), request.getCompanyId());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Application submitted successfully via 1-Click Apply using stored resume!");
            response.put("application", applied);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Application failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/my-applications")
    public ResponseEntity<?> getMyApplications(@RequestParam Long studentId) {
        try {
            List<AppliedDriveDto> list = placementService.getAppliedDrivesForStudent(studentId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @Autowired
    private com.svvv.erp.repository.PlacementApplicationRepository placementApplicationRepository;

    @DeleteMapping("/applications/student/{studentId}/company/{companyId}")
    public ResponseEntity<?> unapplyStudent(@PathVariable Long studentId, @PathVariable Long companyId) {
        java.util.Optional<com.svvv.erp.entity.PlacementApplication> appOpt = placementApplicationRepository.findByStudentIdAndCompanyId(studentId, companyId);
        if (appOpt.isPresent()) {
            placementApplicationRepository.delete(appOpt.get());
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Placement application removed.");
            return ResponseEntity.ok(resp);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", false);
        resp.put("message", "Application not found.");
        return ResponseEntity.ok(resp);
    }
}
