package com.svvv.erp.controller;

import com.svvv.erp.dto.FeeDetailsResponseDto;
import com.svvv.erp.dto.FeePaymentRequest;
import com.svvv.erp.dto.FeeReceiptDto;
import com.svvv.erp.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees")
public class FeeController {

    @Autowired
    private FeeService feeService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<FeeDetailsResponseDto> getFeeDetailsForStudent(@PathVariable Long studentId) {
        try {
            FeeDetailsResponseDto response = feeService.getFeeDetailsForStudent(studentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/receipts/{studentId}")
    public ResponseEntity<List<FeeReceiptDto>> getReceiptsForStudent(@PathVariable Long studentId) {
        try {
            List<FeeReceiptDto> receipts = feeService.getReceiptsForStudent(studentId);
            return ResponseEntity.ok(receipts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<?> processFeePayment(@RequestBody FeePaymentRequest request) {
        try {
            FeeDetailsResponseDto updated = feeService.processFeePayment(request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process payment: " + e.getMessage());
        }
    }
}
