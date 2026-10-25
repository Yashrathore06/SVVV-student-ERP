package com.svvv.erp.controller;

import com.svvv.erp.dto.ApplicantStudentDto;
import com.svvv.erp.dto.CreateDriveRequest;
import com.svvv.erp.entity.*;
import com.svvv.erp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/admin/placements")
public class AdminPlacementController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PlacementApplicationRepository placementApplicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");

    /**
     * 1. Get all placement drives with live applicant counts
     */
    @GetMapping("/drives")
    public ResponseEntity<?> getAllDrives() {
        try {
            List<Company> companies = companyRepository.findAll();
            companies.sort((a, b) -> b.getId().compareTo(a.getId()));

            List<Map<String, Object>> response = new ArrayList<>();
            for (Company c : companies) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", c.getId());
                map.put("companyName", c.getCompanyName());
                map.put("jobRole", c.getJobRole());
                map.put("packageCtc", c.getPackageCtc());
                map.put("minCgpa", c.getMinCgpa());
                map.put("allowedBranch", c.getAllowedBranch());
                map.put("maxBacklogs", c.getMaxBacklogs());
                map.put("deadline", c.getDeadline());
                map.put("status", c.getStatus());

                List<PlacementApplication> applications = placementApplicationRepository.findByCompanyId(c.getId());
                map.put("applicantsCount", applications.size());

                response.add(map);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Failed to fetch drives: " + ex.getMessage());
            return ResponseEntity.internalServerError().body(err);
        }
    }

    /**
     * 2. POST a new campus recruitment drive
     */
    @PostMapping("/drives")
    public ResponseEntity<?> createPlacementDrive(@RequestBody CreateDriveRequest request) {
        try {
            if (request.getCompanyName() == null || request.getCompanyName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Company name is required."));
            }
            if (request.getJobRole() == null || request.getJobRole().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Job role is required."));
            }
            if (request.getPackageCtc() == null || request.getPackageCtc() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Valid CTC package is required."));
            }

            Company company = new Company();
            company.setCompanyName(request.getCompanyName().trim());
            company.setJobRole(request.getJobRole().trim());
            company.setPackageCtc(request.getPackageCtc());
            company.setMinCgpa(request.getMinCgpa() != null ? request.getMinCgpa() : 6.0);
            company.setAllowedBranch(request.getAllowedBranch() != null && !request.getAllowedBranch().trim().isEmpty()
                    ? request.getAllowedBranch().trim()
                    : "ALL");
            company.setMaxBacklogs(request.getMaxBacklogs() != null ? request.getMaxBacklogs() : 0);
            company.setDeadline(request.getDeadline() != null ? request.getDeadline() : LocalDate.now().plusDays(21));
            company.setStatus("ACTIVE");

            Company saved = companyRepository.save(company);

            // Broadcast notification to all students
            try {
                List<Student> students = studentRepository.findAll();
                for (Student s : students) {
                    if (s.getUser() != null) {
                        Notification notif = new Notification(
                                s.getUser(),
                                "Campus Drive: " + saved.getCompanyName(),
                                "Recruitment drive announced for " + saved.getJobRole() + " (" + saved.getPackageCtc() + " LPA). Min CGPA: " + saved.getMinCgpa() + ". Apply via Placements portal.",
                                "PLACEMENT"
                        );
                        notificationRepository.save(notif);
                    }
                }
            } catch (Exception notifEx) {
                System.err.println("Could not broadcast drive notification: " + notifEx.getMessage());
            }

            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Placement drive for " + saved.getCompanyName() + " published successfully!");
            resp.put("drive", saved);
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Error creating placement drive: " + ex.getMessage());
            return ResponseEntity.internalServerError().body(err);
        }
    }

    /**
     * 3. GET applicants for a specific placement drive
     */
    @GetMapping("/drives/{companyId}/applicants")
    public ResponseEntity<?> getDriveApplicants(@PathVariable Long companyId) {
        try {
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Company drive not found with ID: " + companyId));

            List<PlacementApplication> applications = placementApplicationRepository.findByCompanyId(companyId);
            List<ApplicantStudentDto> applicantsList = new ArrayList<>();

            for (PlacementApplication app : applications) {
                Student s = app.getStudent();
                Resume r = app.getResume();

                ApplicantStudentDto dto = new ApplicantStudentDto();
                dto.setApplicationId(app.getId());
                dto.setStudentId(s.getId());
                dto.setRollNo(s.getEnrollmentNo());
                dto.setName(s.getName());
                dto.setEmail(s.getEmail());
                dto.setPhone(s.getPhone() != null ? s.getPhone() : "+91 9876543210");
                dto.setBranch(s.getBranch());
                dto.setSemester(s.getSemester());
                dto.setCgpa(s.getCgpa());
                dto.setBacklogs(s.getBacklogs());
                dto.setStatus(app.getStatus());
                dto.setAppliedAt(app.getAppliedAt() != null ? app.getAppliedAt().format(DATE_TIME_FORMATTER) : "Recently");

                if (r != null) {
                    dto.setResumeFileName(r.getFileName());
                    dto.setResumeUrl(r.getFilePath() != null ? r.getFilePath() : "#");
                } else {
                    dto.setResumeFileName("Default_Resume.pdf");
                    dto.setResumeUrl("#");
                }

                applicantsList.add(dto);
            }

            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("companyId", company.getId());
            resp.put("companyName", company.getCompanyName());
            resp.put("jobRole", company.getJobRole());
            resp.put("packageCtc", company.getPackageCtc());
            resp.put("minCgpa", company.getMinCgpa());
            resp.put("totalApplicants", applicantsList.size());
            resp.put("applicants", applicantsList);

            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Error fetching applicants: " + ex.getMessage());
            return ResponseEntity.internalServerError().body(err);
        }
    }

    /**
     * 4. 1-Click Export Applicants CSV
     */
    @GetMapping("/drives/{companyId}/export-csv")
    public ResponseEntity<byte[]> exportApplicantsCsv(@PathVariable Long companyId) {
        try {
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new IllegalArgumentException("Company drive not found with ID: " + companyId));

            List<PlacementApplication> applications = placementApplicationRepository.findByCompanyId(companyId);

            StringBuilder csv = new StringBuilder();
            csv.append("Application ID,Enrollment No,Student Name,Academic Branch,Semester,CGPA,Backlogs,Phone,Email,Status,Applied At,Resume File\n");

            for (PlacementApplication app : applications) {
                Student s = app.getStudent();
                Resume r = app.getResume();

                csv.append(app.getId()).append(",")
                        .append(escapeCsv(s.getEnrollmentNo())).append(",")
                        .append(escapeCsv(s.getName())).append(",")
                        .append(escapeCsv(s.getBranch())).append(",")
                        .append(s.getSemester() != null ? s.getSemester() : "").append(",")
                        .append(s.getCgpa() != null ? s.getCgpa() : "").append(",")
                        .append(s.getBacklogs() != null ? s.getBacklogs() : 0).append(",")
                        .append(escapeCsv(s.getPhone() != null ? s.getPhone() : "")).append(",")
                        .append(escapeCsv(s.getEmail() != null ? s.getEmail() : "")).append(",")
                        .append(escapeCsv(app.getStatus())).append(",")
                        .append(escapeCsv(app.getAppliedAt() != null ? app.getAppliedAt().toString() : "")).append(",")
                        .append(escapeCsv(r != null ? r.getFileName() : "None")).append("\n");
            }

            byte[] csvBytes = csv.toString().getBytes(StandardCharsets.UTF_8);
            String safeCompanyName = company.getCompanyName().replaceAll("[^a-zA-Z0-9_-]", "_");
            String filename = "SVVV_Applicants_" + safeCompanyName + ".csv";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                    .body(csvBytes);
        } catch (Exception ex) {
            byte[] errBytes = ("Error exporting CSV: " + ex.getMessage()).getBytes(StandardCharsets.UTF_8);
            return ResponseEntity.internalServerError().body(errBytes);
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
