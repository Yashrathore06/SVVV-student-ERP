package com.svvv.erp.service;

import com.svvv.erp.dto.BonafideApplyRequest;
import com.svvv.erp.dto.BonafideResponseDto;
import com.svvv.erp.entity.BonafideRequest;
import com.svvv.erp.entity.Notification;
import com.svvv.erp.entity.Student;
import com.svvv.erp.repository.BonafideRequestRepository;
import com.svvv.erp.repository.NotificationRepository;
import com.svvv.erp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BonafideService {

    @Autowired
    private BonafideRequestRepository bonafideRequestRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");

    private static final java.util.Set<Long> APPROVED_IDS = java.util.Set.of(1L, 2L, 3L, 4L, 41L, 42L, 61L, 62L, 91L, 92L);
    private static final java.util.Set<Long> PENDING_IDS = java.util.Set.of(
            10L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L, 110L, 120L,
            5L, 15L, 25L, 35L, 45L, 55L, 65L, 75L
    );

    public BonafideResponseDto getStatusForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        List<BonafideRequest> requests = bonafideRequestRepository.findByStudentIdOrderByRequestedAtDesc(studentId);

        // 1. If an explicit application exists in the database, return its live state first
        if (!requests.isEmpty()) {
            BonafideRequest latest = requests.get(0);
            return mapToDto(latest);
        }

        // 2. Original 10 students show status 'APPROVED' with standard download link /certificates/bonafide_certificate.pdf
        if (APPROVED_IDS.contains(studentId)) {
            BonafideResponseDto dto = new BonafideResponseDto();
            dto.setHasActiveRequest(true);
            dto.setId(1000L + studentId);
            dto.setReferenceNo("SVVV-BONA-2026-" + (2000 + studentId));
            dto.setStudentId(student.getId());
            dto.setStudentName(student.getName());
            dto.setEnrollmentNo(student.getEnrollmentNo());
            dto.setBranch(student.getBranch());
            dto.setSemester(student.getSemester());
            dto.setFatherName("Shri Academic Guardian");
            dto.setPurpose("Bonafide Certificate");
            dto.setRemarks("Approved by Academic Registrar");
            dto.setStatus("APPROVED");
            dto.setStatusBadgeText("Approved & Digitally Signed");
            dto.setAcademicYear("2026-2027");
            dto.setVerificationToken("SVVV-REG-" + studentId + "-SECURE-VERIFIED");
            dto.setRequestedAt("Oct 15, 2026 at 10:30 AM");
            dto.setApprovedAt("Oct 16, 2026 at 02:15 PM");
            dto.setCertificateUrl("/certificates/bonafide_certificate.pdf");
            dto.setDownloadUrl("/certificates/bonafide_certificate.pdf");
            return dto;
        }

        // 3. Pending 20 students show status 'PENDING' with remark "Pending Verification"
        if (PENDING_IDS.contains(studentId)) {
            BonafideResponseDto dto = new BonafideResponseDto();
            dto.setHasActiveRequest(true);
            dto.setId(2000L + studentId);
            dto.setReferenceNo("SVVV-BONA-2026-" + (3000 + studentId));
            dto.setStudentId(student.getId());
            dto.setStudentName(student.getName());
            dto.setEnrollmentNo(student.getEnrollmentNo());
            dto.setBranch(student.getBranch());
            dto.setSemester(student.getSemester());
            dto.setFatherName("Shri Academic Guardian");
            dto.setPurpose("Bonafide Certificate");
            dto.setRemarks("Pending Verification");
            dto.setStatus("PENDING");
            dto.setStatusBadgeText("Pending Verification");
            dto.setAcademicYear("2026-2027");
            dto.setVerificationToken("SVVV-REG-" + studentId + "-PENDING-REVIEW");
            dto.setRequestedAt("Oct 20, 2026 at 11:00 AM");
            dto.setApprovedAt(null);
            return dto;
        }

        // 4. Rest show status 'NOT APPLIED' with an "Apply Online" form
        BonafideResponseDto dto = new BonafideResponseDto();
        dto.setHasActiveRequest(false);
        dto.setStatus("NOT_APPLIED");
        dto.setStatusBadgeText("Not Applied");
        dto.setStudentId(student.getId());
        dto.setStudentName(student.getName());
        dto.setEnrollmentNo(student.getEnrollmentNo());
        dto.setBranch(student.getBranch());
        dto.setSemester(student.getSemester());
        dto.setAcademicYear("2026-2027");
        return dto;
    }

    @Transactional
    public BonafideResponseDto applyForBonafide(BonafideApplyRequest req) {
        Student student = studentRepository.findById(req.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + req.getStudentId()));

        // Generate unique reference number: SVVV-BONA-2026-XXXX
        int randomCode = (int) (1000 + (Math.random() * 9000));
        String refNo = "SVVV-BONA-2026-" + randomCode;

        BonafideRequest entity = new BonafideRequest();
        entity.setStudent(student);
        entity.setFatherName(req.getFatherName() != null ? req.getFatherName().trim() : "Shri Academic Guardian");
        entity.setPurpose(req.getPurpose() != null ? req.getPurpose().trim() : "General Purpose");
        entity.setRemarks(req.getRemarks());
        entity.setReferenceNo(refNo);
        entity.setStatus("PENDING");
        entity.setRequestedAt(LocalDateTime.now());
        entity.setCertificatePath(null);

        bonafideRequestRepository.save(entity);

        // Add In-App Notification
        Notification notif = new Notification(
                student.getUser(),
                "Bonafide Application Submitted: " + refNo,
                "Your request for Bonafide Certificate (" + entity.getPurpose() + ") is under review by the Assistant Registrar.",
                "BONAFIDE"
        );
        notificationRepository.save(notif);

        return mapToDto(entity);
    }

    @Transactional
    public BonafideResponseDto approveBonafide(Long requestId) {
        BonafideRequest entity = bonafideRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Bonafide request not found with ID: " + requestId));

        entity.setStatus("APPROVED");
        entity.setApprovedAt(LocalDateTime.now());
        entity.setCertificatePath("/certificates/bonafide_certificate.pdf");
        bonafideRequestRepository.save(entity);

        // Add In-App Notification
        Notification notif = new Notification(
                entity.getStudent().getUser(),
                "Bonafide Certificate Approved & Signed!",
                "Your official Bonafide Certificate (" + entity.getReferenceNo() + ") has been digitally signed and is ready to download.",
                "BONAFIDE"
        );
        notificationRepository.save(notif);

        return mapToDto(entity);
    }

    @Transactional
    public BonafideResponseDto rejectBonafide(Long id) {
        BonafideRequest entity = bonafideRequestRepository.findById(id).orElse(null);
        if (entity == null) {
            BonafideResponseDto fallback = new BonafideResponseDto();
            fallback.setId(id);
            fallback.setStatus("REJECTED");
            fallback.setStatusBadgeText("Rejected");
            return fallback;
        }

        entity.setStatus("REJECTED");
        bonafideRequestRepository.save(entity);

        if (entity.getStudent() != null && entity.getStudent().getUser() != null) {
            Notification notif = new Notification(
                    entity.getStudent().getUser(),
                    "Bonafide Certificate Status Updated",
                    "Your Bonafide Certificate request (" + entity.getReferenceNo() + ") was reviewed and not approved.",
                    "BONAFIDE"
            );
            notificationRepository.save(notif);
        }

        return mapToDto(entity);
    }

    public List<BonafideResponseDto> getAllRequests() {
        return bonafideRequestRepository.findAll().stream()
                .sorted((a, b) -> {
                    if (a.getRequestedAt() == null && b.getRequestedAt() == null) return 0;
                    if (a.getRequestedAt() == null) return 1;
                    if (b.getRequestedAt() == null) return -1;
                    return b.getRequestedAt().compareTo(a.getRequestedAt());
                })
                .map(this::mapToDto)
                .toList();
    }

    private BonafideResponseDto mapToDto(BonafideRequest b) {
        Student s = b.getStudent();
        BonafideResponseDto dto = new BonafideResponseDto();
        dto.setHasActiveRequest(true);
        dto.setId(b.getId());
        dto.setReferenceNo(b.getReferenceNo() != null ? b.getReferenceNo() : ("SVVV-BONA-2026-" + b.getId()));
        dto.setStudentId(s.getId());
        dto.setStudentName(s.getName());
        dto.setEnrollmentNo(s.getEnrollmentNo());
        dto.setBranch(s.getBranch());
        dto.setSemester(s.getSemester());
        dto.setFatherName(b.getFatherName());
        dto.setPurpose("Bonafide Certificate");
        dto.setRemarks(b.getRemarks());
        dto.setStatus(b.getStatus());
        dto.setAcademicYear("2026-2027");
        dto.setVerificationToken("SVVV-REG-" + Math.abs(b.hashCode()) + "-SECURE-VERIFIED");

        if ("APPROVED".equalsIgnoreCase(b.getStatus())) {
            dto.setStatusBadgeText("Approved & Digitally Signed");
            dto.setCertificateUrl("/certificates/bonafide_certificate.pdf");
            dto.setDownloadUrl("/certificates/bonafide_certificate.pdf");
        } else if ("REJECTED".equalsIgnoreCase(b.getStatus())) {
            dto.setStatusBadgeText("Rejected");
        } else {
            dto.setStatusBadgeText("Applied & Waiting for Verification");
        }

        dto.setRequestedAt(b.getRequestedAt() != null ? b.getRequestedAt().format(DATE_FORMATTER) : "Recently");
        dto.setApprovedAt(b.getApprovedAt() != null ? b.getApprovedAt().format(DATE_FORMATTER) : null);

        return dto;
    }
}
