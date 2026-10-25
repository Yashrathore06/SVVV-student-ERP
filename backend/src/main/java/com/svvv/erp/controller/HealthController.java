package com.svvv.erp.controller;

import com.svvv.erp.repository.CompanyRepository;
import com.svvv.erp.repository.ElectiveCourseRepository;
import com.svvv.erp.repository.StudentRepository;
import com.svvv.erp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ElectiveCourseRepository electiveCourseRepository;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("project", "ERP-based Integrated Student Management System");
        response.put("institution", "Shri Vaishnav Vidyapeeth Vishwavidyalaya (SVVV), Indore");

        try (Connection connection = dataSource.getConnection()) {
            response.put("databaseStatus", "CONNECTED");
            response.put("databaseProduct", connection.getMetaData().getDatabaseProductName());
            response.put("databaseVersion", connection.getMetaData().getDatabaseProductVersion());

            // Real counts from Supabase database
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", userRepository.count());
            stats.put("totalStudents", studentRepository.count());
            stats.put("totalCompanies", companyRepository.count());
            stats.put("totalElectives", electiveCourseRepository.count());
            response.put("databaseStats", stats);
        } catch (Exception e) {
            response.put("databaseStatus", "ERROR: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
