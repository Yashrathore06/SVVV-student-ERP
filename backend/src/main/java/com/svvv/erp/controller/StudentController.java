package com.svvv.erp.controller;

import com.svvv.erp.dto.StudentAddRequest;
import com.svvv.erp.dto.StudentProfileDto;
import com.svvv.erp.dto.StudentProfileUpdateRequest;
import com.svvv.erp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getStudentCount() {
        long count = studentService.getStudentCount();
        Map<String, Object> resp = new HashMap<>();
        resp.put("count", count);
        resp.put("totalStudents", count);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<StudentProfileDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping({"/add", ""})
    public ResponseEntity<?> addStudent(@RequestBody StudentAddRequest request) {
        try {
            StudentProfileDto created = studentService.addStudent(request);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Student record successfully added to directory!");
            resp.put("student", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (IllegalArgumentException ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(err);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Failed to add student: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    @DeleteMapping({"/delete/{enrollmentNo}", "/enrollment/{enrollmentNo}", "/{enrollmentNo}"})
    public ResponseEntity<?> deleteStudent(@PathVariable("enrollmentNo") String enrollmentNo) {
        boolean deleted = false;
        try {
            Long id = Long.parseLong(enrollmentNo);
            deleted = studentService.deleteStudentById(id);
        } catch (NumberFormatException ignored) {}

        if (!deleted) {
            deleted = studentService.deleteStudentByEnrollmentNo(enrollmentNo);
        }

        if (deleted) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Student record for " + enrollmentNo + " and linked child records successfully deleted!");
            return ResponseEntity.ok(resp);
        } else {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Student with enrollment number / ID " + enrollmentNo + " not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<StudentProfileDto> getStudentProfile(@PathVariable("id") Long id) {
        return studentService.getProfile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/profile")
    public ResponseEntity<StudentProfileDto> getProfileByUserId(@PathVariable("userId") Long userId) {
        return studentService.getProfileByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<StudentProfileDto> getStudentByUserId(@PathVariable("userId") Long userId) {
        return studentService.getProfileByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<StudentProfileDto> getCurrentStudentProfile(
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "userId", required = false) Long userId) {
        if (studentId != null) {
            return studentService.getProfile(studentId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        if (userId != null) {
            return studentService.getProfileByUserId(userId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<StudentProfileDto> updateStudentProfile(
            @PathVariable("id") Long id,
            @RequestBody StudentProfileUpdateRequest request) {
        return studentService.updateProfile(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
