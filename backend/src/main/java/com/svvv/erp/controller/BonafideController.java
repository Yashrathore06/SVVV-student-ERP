package com.svvv.erp.controller;

import com.svvv.erp.dto.BonafideApplyRequest;
import com.svvv.erp.dto.BonafideResponseDto;
import com.svvv.erp.service.BonafideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/bonafide", "/api/admin/bonafide"})
public class BonafideController {

    @Autowired
    private BonafideService bonafideService;

    @Autowired
    private com.svvv.erp.repository.BonafideRequestRepository bonafideRequestRepository;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<BonafideResponseDto> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(bonafideService.getStatusForStudent(studentId));
    }

    @GetMapping("/status")
    public ResponseEntity<BonafideResponseDto> getStatus(@RequestParam Long studentId) {
        return ResponseEntity.ok(bonafideService.getStatusForStudent(studentId));
    }

    @PostMapping("/apply")
    public ResponseEntity<BonafideResponseDto> apply(@RequestBody BonafideApplyRequest request) {
        return ResponseEntity.ok(bonafideService.applyForBonafide(request));
    }

    @GetMapping("/all")
    public ResponseEntity<java.util.List<BonafideResponseDto>> getAll() {
        return ResponseEntity.ok(bonafideService.getAllRequests());
    }

    @RequestMapping(value = "/{id}/approve", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<BonafideResponseDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(bonafideService.approveBonafide(id));
    }

    @RequestMapping(value = "/{id}/reject", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<BonafideResponseDto> reject(@PathVariable Long id) {
        return ResponseEntity.ok(bonafideService.rejectBonafide(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBonafide(@PathVariable Long id) {
        if (bonafideRequestRepository.existsById(id)) {
            bonafideRequestRepository.deleteById(id);
            return ResponseEntity.ok(java.util.Map.of("success", true, "message", "Bonafide request deleted."));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<?> deleteBonafideByStudent(@PathVariable Long studentId) {
        java.util.List<com.svvv.erp.entity.BonafideRequest> list = bonafideRequestRepository.findByStudentIdOrderByRequestedAtDesc(studentId);
        if (!list.isEmpty()) {
            bonafideRequestRepository.deleteAll(list);
            return ResponseEntity.ok(java.util.Map.of("success", true, "message", "Bonafide requests removed for student."));
        }
        return ResponseEntity.ok(java.util.Map.of("success", false, "message", "No requests found."));
    }
}
