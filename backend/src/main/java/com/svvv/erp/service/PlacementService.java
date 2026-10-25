package com.svvv.erp.service;

import com.svvv.erp.dto.AppliedDriveDto;
import com.svvv.erp.dto.PlacementDriveDto;
import com.svvv.erp.entity.*;
import com.svvv.erp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PlacementService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private PlacementApplicationRepository placementApplicationRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    public List<PlacementDriveDto> getDrivesForStudent(Long studentId) {
        return getDrivesForStudent(studentId, null);
    }

    public List<PlacementDriveDto> getDrivesForStudent(Long studentId, String branchOverride) {
        Student student = null;
        if (studentId != null) {
            student = studentRepository.findById(studentId).orElse(null);
        }

        String studentBranch = (branchOverride != null && !branchOverride.trim().isEmpty()) ? branchOverride.trim() : null;
        if (studentBranch == null && student != null && student.getBranch() != null && !student.getBranch().trim().isEmpty()) {
            studentBranch = student.getBranch().trim();
        }
        if (studentBranch == null || studentBranch.trim().isEmpty()) {
            studentBranch = "B.Tech Computer Science & Engineering";
        }

        // Fetch active companies strictly matching the student's branch via repository SQL query
        String[] keywords = getBranchSqlKeywords(studentBranch);
        List<Company> companies = companyRepository.findActiveCompaniesMatchingBranch(
                studentBranch, keywords[0], keywords[1]);

        List<PlacementApplication> studentApplications = (student != null)
                ? placementApplicationRepository.findByStudentId(student.getId())
                : Collections.emptyList();

        Map<Long, PlacementApplication> appMap = new HashMap<>();
        for (PlacementApplication app : studentApplications) {
            appMap.put(app.getCompany().getId(), app);
        }

        List<PlacementDriveDto> result = new ArrayList<>();

        for (Company comp : companies) {
            // Strict branch eligibility verification
            boolean branchPassed = isBranchEligible(studentBranch, comp.getAllowedBranch());
            if (!branchPassed) {
                // Strictly exclude companies that do not match the student's branch
                continue;
            }

            PlacementDriveDto dto = new PlacementDriveDto();
            dto.setId(comp.getId());
            dto.setCompanyName(comp.getCompanyName());
            dto.setJobRole(comp.getJobRole());
            dto.setPackageCtc(comp.getPackageCtc());
            dto.setMinCgpa(comp.getMinCgpa());
            dto.setAllowedBranch(comp.getAllowedBranch());
            dto.setMaxBacklogs(comp.getMaxBacklogs());
            dto.setDeadline(comp.getDeadline());
            dto.setStatus(comp.getStatus());

            // Assign logo code and defaults based on company name
            String cName = comp.getCompanyName().toUpperCase();
            if (cName.contains("TCS")) {
                dto.setLogoCode("TCS");
                dto.setServiceBond("1 Year");
                dto.setDriveRef("#TCS-2026-IND");
            } else if (cName.contains("WIPRO")) {
                dto.setLogoCode("WIP");
                dto.setServiceBond("No Bond");
                dto.setDriveRef("#WIP-2026-IND");
            } else if (cName.contains("INFOSYS")) {
                dto.setLogoCode("INF");
                dto.setServiceBond("2 Years");
                dto.setDriveRef("#INF-SP-2026");
            } else if (cName.contains("COGNIZANT")) {
                dto.setLogoCode("CTS");
                dto.setServiceBond("1 Year");
                dto.setDriveRef("#CTS-GEN-891");
            } else {
                dto.setLogoCode(comp.getCompanyName().substring(0, Math.min(3, comp.getCompanyName().length())).toUpperCase());
                dto.setServiceBond("1 Year");
                dto.setDriveRef("#" + dto.getLogoCode() + "-2026");
            }

            // Student profile snapshots
            Double studentCgpa = (student != null && student.getCgpa() != null) ? student.getCgpa() : 8.0;
            Integer studentBacklogs = (student != null && student.getBacklogs() != null) ? student.getBacklogs() : 0;
            String displayBranch = getBranchCode(studentBranch);

            dto.setStudentCgpa(studentCgpa);
            dto.setStudentBranch(displayBranch);
            dto.setStudentBacklogs(studentBacklogs);

            // Automated Criteria Checks (Rule-based evaluation)
            boolean cgpaPassed = studentCgpa >= comp.getMinCgpa();
            boolean backlogsPassed = studentBacklogs <= comp.getMaxBacklogs();

            dto.setCgpaPassed(cgpaPassed);
            dto.setBranchPassed(true); // Branch is strictly verified and matched
            dto.setBacklogsPassed(backlogsPassed);

            boolean isEligible = cgpaPassed && backlogsPassed;
            dto.setEligible(isEligible);

            if (!isEligible) {
                if (!cgpaPassed) {
                    dto.setEligibilityReason("Criteria Not Met (" + String.format("%.1f", comp.getMinCgpa()) + " CGPA Required)");
                } else {
                    dto.setEligibilityReason("Backlog Limit Exceeded (Max: " + comp.getMaxBacklogs() + ")");
                }
            } else {
                dto.setEligibilityReason("Eligible");
            }

            // Check if already applied
            if (appMap.containsKey(comp.getId())) {
                PlacementApplication app = appMap.get(comp.getId());
                dto.setAlreadyApplied(true);
                dto.setApplicationStatus(app.getStatus());
                if (app.getAppliedAt() != null) {
                    dto.setAppliedDate(app.getAppliedAt().format(DATE_FORMATTER));
                }
            } else {
                dto.setAlreadyApplied(false);
            }

            result.add(dto);
        }

        return result;
    }

    @Transactional
    public AppliedDriveDto applyToDrive(Long studentId, Long companyId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with ID: " + companyId));

        // Check if student has a resume uploaded (SINGLE-ENTRY DATA REUSE PRINCIPLE)
        Optional<Resume> resumeOpt = resumeRepository.findTopByStudentIdOrderByUploadedAtDesc(studentId);
        Resume resume;
        if (resumeOpt.isEmpty()) {
            resume = new Resume();
            resume.setStudent(student);
            resume.setFileName(student.getName().replaceAll("\\s+", "_") + "_Verified_SVVV_Profile.pdf");
            resume.setFilePath("/uploads/resumes/" + student.getEnrollmentNo() + "_resume.pdf");
            resume.setUploadedAt(java.time.LocalDateTime.now());
            resume = resumeRepository.save(resume);
        } else {
            resume = resumeOpt.get();
        }

        // Automated eligibility rule verification
        if (student.getCgpa() < company.getMinCgpa()) {
            throw new IllegalStateException("Ineligible: Your CGPA (" + student.getCgpa() + ") does not meet the minimum requirement (" + company.getMinCgpa() + ").");
        }
        if (!isBranchEligible(student.getBranch(), company.getAllowedBranch())) {
            throw new IllegalStateException("Ineligible: Your branch (" + student.getBranch() + ") is not eligible for this drive.");
        }
        if (student.getBacklogs() > company.getMaxBacklogs()) {
            throw new IllegalStateException("Ineligible: You have " + student.getBacklogs() + " active backlog(s). Maximum allowed is " + company.getMaxBacklogs() + ".");
        }

        // Duplicate application check
        if (placementApplicationRepository.existsByStudentIdAndCompanyId(studentId, companyId)) {
            throw new IllegalStateException("You have already submitted an application for " + company.getCompanyName() + ".");
        }

        // Save application
        PlacementApplication application = new PlacementApplication();
        application.setStudent(student);
        application.setCompany(company);
        application.setResume(resume);
        application.setStatus("APPLIED");
        PlacementApplication saved = placementApplicationRepository.save(application);

        // Generate in-app Notification for student
        Notification notification = new Notification(
                student.getUser(),
                "Application Submitted: " + company.getCompanyName(),
                "Your 1-Click application for " + company.getJobRole() + " (" + company.getPackageCtc() + " LPA) was submitted successfully with resume '" + resume.getFileName() + "'.",
                "PLACEMENT"
        );
        notificationRepository.save(notification);

        return mapToAppliedDriveDto(saved);
    }

    public List<AppliedDriveDto> getAppliedDrivesForStudent(Long studentId) {
        List<PlacementApplication> apps = placementApplicationRepository.findByStudentId(studentId);
        List<AppliedDriveDto> list = new ArrayList<>();

        for (PlacementApplication app : apps) {
            list.add(mapToAppliedDriveDto(app));
        }
        return list;
    }

    private AppliedDriveDto mapToAppliedDriveDto(PlacementApplication app) {
        AppliedDriveDto dto = new AppliedDriveDto();
        dto.setApplicationId(app.getId());
        dto.setCompanyId(app.getCompany().getId());
        dto.setCompanyName(app.getCompany().getCompanyName());
        dto.setRoleApplied(app.getCompany().getJobRole());

        if (app.getAppliedAt() != null) {
            dto.setAppliedDate(app.getAppliedAt().format(DATE_FORMATTER));
        } else {
            dto.setAppliedDate("Recent");
        }

        String cName = app.getCompany().getCompanyName().toUpperCase();
        if (cName.contains("TCS")) {
            dto.setLogoCode("TCS");
            dto.setDriveRef("Drive Ref: #TCS-2026-IND");
        } else if (cName.contains("WIPRO")) {
            dto.setLogoCode("WIP");
            dto.setDriveRef("Drive Ref: #WIP-2026-IND");
        } else if (cName.contains("INFOSYS")) {
            dto.setLogoCode("INF");
            dto.setDriveRef("Drive Ref: #INF-SP-2026");
        } else if (cName.contains("COGNIZANT")) {
            dto.setLogoCode("CTS");
            dto.setDriveRef("Drive Ref: #CTS-GEN-891");
        } else {
            dto.setLogoCode(app.getCompany().getCompanyName().substring(0, Math.min(3, app.getCompany().getCompanyName().length())).toUpperCase());
            dto.setDriveRef("Drive Ref: #" + dto.getLogoCode() + "-2026");
        }

        String rawStatus = app.getStatus() != null ? app.getStatus() : "APPLIED";
        dto.setCurrentStatus(rawStatus);

        // Customize display badges and interview notes based on status
        if ("SHORTLISTED".equalsIgnoreCase(rawStatus)) {
            dto.setStatusBadgeText("Shortlisted for Round 2");
            dto.setStatusNote("Technical Interview: Sep 05, 10:30 AM");
            dto.setCallLetterAvailable(true);
            dto.setCallLetterRef("CALL-LTR-" + app.getId() + "-TCS");
        } else if ("ASSESSMENT_CLEARED".equalsIgnoreCase(rawStatus)) {
            dto.setStatusBadgeText("Online Assessment Cleared");
            dto.setStatusNote("Awaiting slot allocation");
            dto.setCallLetterAvailable(false);
        } else if ("REJECTED".equalsIgnoreCase(rawStatus)) {
            dto.setStatusBadgeText("Not Shortlisted");
            dto.setStatusNote("Recruitment cycle closed");
            dto.setCallLetterAvailable(false);
        } else {
            dto.setStatusBadgeText("Application Under Review");
            dto.setStatusNote("Resume shared with recruiter panel");
            dto.setCallLetterAvailable(false);
        }

        return dto;
    }

    public String getBranchCode(String branchFullName) {
        if (branchFullName == null) return "B.Tech CSE";
        String lower = branchFullName.toLowerCase().trim();
        if (lower.contains("b.sc") || lower.contains("bsc")) return "B.Sc CS";
        if (lower.contains("bca")) return "BCA";
        if (lower.contains("mba")) return "MBA";
        if (lower.contains("b.tech") || lower.contains("btech") || lower.contains("cse")) return "B.Tech CSE";
        if (lower.contains("computer science")) return "CSE";
        if (lower.contains("information technology") || lower.contains("it")) return "IT";
        if (lower.contains("electronics") || lower.contains("ece")) return "ECE";
        if (lower.contains("mechanical") || lower.contains("me")) return "ME";
        if (lower.contains("civil") || lower.contains("ce")) return "CE";
        return branchFullName.trim();
    }

    private String[] getBranchSqlKeywords(String branch) {
        if (branch == null || branch.trim().isEmpty()) {
            return new String[]{"ALL", "ALL"};
        }
        String upper = branch.toUpperCase().trim();
        if (upper.contains("B.TECH") || upper.contains("BTECH")) {
            return new String[]{"B.Tech", "CSE"};
        } else if (upper.contains("B.SC") || upper.contains("BSC")) {
            return new String[]{"B.Sc", "BSc"};
        } else if (upper.contains("BCA")) {
            return new String[]{"BCA", "BCA"};
        } else if (upper.contains("MBA")) {
            return new String[]{"MBA", "MBA"};
        } else if (upper.contains("CIVIL") || upper.contains("CE")) {
            return new String[]{"Civil", "CE"};
        } else if (upper.contains("MECHANICAL") || upper.contains("ME")) {
            return new String[]{"Mechanical", "ME"};
        } else if (upper.contains("ELECTRICAL") || upper.contains("EE")) {
            return new String[]{"Electrical", "EE"};
        }
        return new String[]{branch, branch};
    }

    public boolean isBranchEligible(String studentBranch, String allowedBranches) {
        if (allowedBranches == null || allowedBranches.trim().isEmpty() || allowedBranches.equalsIgnoreCase("ALL")) {
            return true;
        }
        if (studentBranch == null || studentBranch.trim().isEmpty()) {
            return false;
        }

        String studentUpper = studentBranch.toUpperCase().trim();
        String allowedUpper = allowedBranches.toUpperCase().trim();

        // Exact match or open for ALL
        if (allowedUpper.equals(studentUpper) || allowedUpper.contains("ALL")) {
            return true;
        }

        // Student degree classification
        boolean isBTech = studentUpper.contains("B.TECH") || studentUpper.contains("BTECH");
        boolean isBSc = studentUpper.contains("B.SC") || studentUpper.contains("BSC");
        boolean isBca = studentUpper.contains("BCA");
        boolean isMba = studentUpper.contains("MBA");

        // Split allowed branches by delimiters (comma, semicolon, slash, pipe)
        String[] allowedList = allowedUpper.split("[,;/|]+");
        for (String item : allowedList) {
            String token = item.trim();
            if (token.isEmpty()) continue;
            if (token.equals("ALL")) return true;

            if (isBTech) {
                // Must not match B.Sc, BCA, or MBA drives
                if (token.contains("B.SC") || token.contains("BSC") || token.contains("MBA") || token.contains("BCA")) {
                    continue;
                }
                if (token.contains("B.TECH") || token.contains("BTECH") || token.contains("CSE")
                        || token.contains("COMPUTER SCIENCE & ENGINEERING") || token.contains("CS & ENG")) {
                    return true;
                }
                if (token.equals("CS") || token.equals("IT") || token.equals("CSE")) {
                    return true;
                }
            } else if (isBSc) {
                // Must not match B.Tech, BCA, or MBA drives
                if (token.contains("B.TECH") || token.contains("BTECH") || token.contains("MBA") || token.contains("BCA")) {
                    continue;
                }
                if (token.contains("B.SC") || token.contains("BSC") || token.contains("B.SC COMPUTER SCIENCE") || token.contains("B.SC CS")) {
                    return true;
                }
                if (token.equals("CS") && !token.contains("TECH")) {
                    return true;
                }
            } else if (isBca) {
                if (token.contains("B.TECH") || token.contains("B.SC") || token.contains("MBA")) {
                    continue;
                }
                if (token.contains("BCA") || token.contains("BACHELOR OF COMPUTER APPLICATIONS")) {
                    return true;
                }
            } else if (isMba) {
                if (token.contains("B.TECH") || token.contains("B.SC") || token.contains("BCA")) {
                    continue;
                }
                if (token.contains("MBA") || token.contains("MASTER OF BUSINESS ADMINISTRATION")) {
                    return true;
                }
            }
        }

        return false;
    }
}
