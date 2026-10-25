package com.svvv.erp.service;

import com.svvv.erp.dto.HostelApplicationRequest;
import com.svvv.erp.dto.HostelApprovalRequest;
import com.svvv.erp.entity.HostelApplication;
import com.svvv.erp.entity.Student;
import com.svvv.erp.repository.HostelApplicationRepository;
import com.svvv.erp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class HostelService {

    @Autowired
    private HostelApplicationRepository hostelApplicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Map<String, Object> getStudentProfileForHostel(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        Map<String, Object> profile = new HashMap<>();
        profile.put("studentId", student.getId());
        profile.put("name", student.getName());
        profile.put("enrollmentNo", student.getEnrollmentNo());
        profile.put("branch", student.getBranch());
        profile.put("semester", student.getSemester());
        profile.put("phone", student.getPhone() != null ? student.getPhone() : "+91 9876543210");
        profile.put("email", student.getEmail());
        profile.put("gender", student.getGender() != null ? student.getGender() : "Male");
        profile.put("programTerm", "B.Tech " + (student.getBranch() != null ? student.getBranch() : "CSE") + " (Semester " + (student.getSemester() != null ? student.getSemester() : 7) + ")");
        profile.put("academicYear", "2026-2027");

        return profile;
    }

    public Optional<HostelApplication> getLatestApplicationForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
        return hostelApplicationRepository.findTopByStudentOrderByApplicationDateDesc(student);
    }

    @Transactional
    public HostelApplication submitApplication(HostelApplicationRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + request.getStudentId()));

        // Calculate verified total
        Double base = request.getBaseRoomFee() != null ? request.getBaseRoomFee() : 50000.0;
        Double climate = request.getClimateFeeAddon() != null ? request.getClimateFeeAddon() : 0.0;
        Double total = base + climate;

        HostelApplication application = new HostelApplication();
        application.setStudent(student);
        application.setHostelWing(request.getHostelWing());
        application.setRoomOccupancyType(request.getRoomOccupancyType());
        application.setBaseRoomFee(base);
        application.setClimateFacility(request.getClimateFacility());
        application.setClimateFeeAddon(climate);
        application.setTotalPayableFee(total);
        application.setGuardianContact(request.getGuardianContact() != null ? request.getGuardianContact() : student.getPhone());
        application.setApplicationDate(LocalDateTime.now());
        application.setStatus("APPLIED");

        return hostelApplicationRepository.save(application);
    }

    public List<HostelApplication> getAllApplications(String status) {
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            return hostelApplicationRepository.findByStatusOrderByApplicationDateDesc(status.toUpperCase());
        }
        return hostelApplicationRepository.findAllByOrderByApplicationDateDesc();
    }

    @Transactional
    public HostelApplication processApproval(Long applicationId, HostelApprovalRequest request) {
        HostelApplication application = hostelApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Hostel application not found with ID: " + applicationId));

        if ("REJECTED".equalsIgnoreCase(request.getStatus())) {
            application.setStatus("REJECTED");
            if (request.getWardenName() != null && !request.getWardenName().trim().isEmpty()) {
                application.setWardenName(request.getWardenName());
            }
        } else {
            application.setStatus("APPROVED");
            application.setAllottedRoomNo(
                    request.getAllottedRoomNo() != null && !request.getAllottedRoomNo().trim().isEmpty()
                            ? request.getAllottedRoomNo()
                            : "Block-A Room 302"
            );
            application.setWardenName(
                    request.getWardenName() != null && !request.getWardenName().trim().isEmpty()
                            ? request.getWardenName()
                            : "Mr. S. K. Sharma (Chief Warden)"
            );
        }

        return hostelApplicationRepository.save(application);
    }
}
