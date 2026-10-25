package com.svvv.erp.service;

import com.svvv.erp.dto.ResumeDto;
import com.svvv.erp.dto.StudentAddRequest;
import com.svvv.erp.dto.StudentProfileDto;
import com.svvv.erp.dto.StudentProfileUpdateRequest;
import com.svvv.erp.entity.Student;
import com.svvv.erp.entity.User;
import com.svvv.erp.repository.StudentRepository;
import com.svvv.erp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<StudentProfileDto> getAllStudents() {
        return studentRepository.findAll().stream().map(s -> mapToProfileDto(s, false)).toList();
    }

    public Optional<StudentProfileDto> getProfile(Long studentId) {
        return studentRepository.findById(studentId).map(s -> mapToProfileDto(s, true));
    }

    public Optional<StudentProfileDto> getProfileByUserId(Long userId) {
        return studentRepository.findByUserId(userId).map(s -> mapToProfileDto(s, true));
    }

    public long getStudentCount() {
        return studentRepository.count();
    }

    public Optional<StudentProfileDto> getProfileByEnrollmentNo(String enrollmentNo) {
        return studentRepository.findByEnrollmentNo(enrollmentNo).map(s -> mapToProfileDto(s, true));
    }

    @Transactional
    public StudentProfileDto addStudent(StudentAddRequest request) {
        if (request.getEnrollmentNo() == null || request.getEnrollmentNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Enrollment number is required");
        }
        String cleanEnr = request.getEnrollmentNo().trim().toUpperCase();

        if (studentRepository.existsByEnrollmentNo(cleanEnr)) {
            throw new IllegalArgumentException("Student with enrollment number " + cleanEnr + " already exists");
        }

        // 1. Password formula: 'student' + last 5 digits/chars of enrollment number
        String rawPassword;
        if (cleanEnr.length() >= 5) {
            rawPassword = "student" + cleanEnr.substring(cleanEnr.length() - 5);
        } else {
            rawPassword = "student" + cleanEnr;
        }

        // 2. Create and persist User credentials with ROLE_STUDENT
        User user = userRepository.findByUsername(cleanEnr).orElse(null);
        if (user == null) {
            user = new User(cleanEnr, passwordEncoder.encode(rawPassword), "ROLE_STUDENT");
            user = userRepository.save(user);
        } else {
            user.setRole("ROLE_STUDENT");
            user.setPassword(passwordEncoder.encode(rawPassword));
            user = userRepository.save(user);
        }

        // 3. Create and persist Student profile
        Student student = new Student();
        student.setUser(user);
        student.setEnrollmentNo(cleanEnr);
        student.setName(request.getName() != null ? request.getName().trim() : "Student");
        student.setEmail(request.getEmail() != null ? request.getEmail().trim() : cleanEnr.toLowerCase() + "@svvv.edu.in");
        student.setPhone(request.getPhone() != null ? request.getPhone().trim() : "+91 98000 00000");
        student.setBranch(request.getBranch() != null ? request.getBranch().trim() : "B.Tech Computer Science & Engineering");
        student.setSemester(request.getSemester() != null ? request.getSemester() : 7);
        student.setCgpa(request.getCgpa() != null ? request.getCgpa() : 8.0);
        student.setBacklogs(request.getBacklogs() != null ? request.getBacklogs() : 0);
        student.setFamilyIncome(request.getFamilyIncome() != null ? request.getFamilyIncome() : 500000.0);
        student.setAddress(request.getAddress() != null ? request.getAddress().trim() : "Shri Vaishnav Campus, Indore (M.P.)");
        student.setGender(request.getGender() != null ? request.getGender().trim() : "Male");

        Student savedStudent = studentRepository.save(student);
        return mapToProfileDto(savedStudent);
    }

    @Transactional
    public boolean deleteStudentById(Long id) {
        if (id == null) {
            return false;
        }
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty()) {
            return false;
        }
        return cascadeDeleteStudent(studentOpt.get());
    }

    @Transactional
    public boolean deleteStudentByEnrollmentNo(String enrollmentNo) {
        if (enrollmentNo == null || enrollmentNo.trim().isEmpty()) {
            return false;
        }
        String cleanEnr = enrollmentNo.trim().toUpperCase();
        Optional<Student> studentOpt = studentRepository.findByEnrollmentNo(cleanEnr);
        if (studentOpt.isEmpty()) {
            studentOpt = studentRepository.findByUser_Username(cleanEnr);
        }
        if (studentOpt.isEmpty()) {
            return false;
        }
        return cascadeDeleteStudent(studentOpt.get());
    }

    private boolean cascadeDeleteStudent(Student student) {
        Long studentId = student.getId();
        User user = student.getUser();
        Long userId = user != null ? user.getId() : null;
        String enrollmentNo = student.getEnrollmentNo();

        // Safely delete child records first in dependency sequence:
        // placement_applications, student_fee_records, fee_receipts, bonafide_requests, resumes, notifications, then students record, and finally users record.
        try { jdbcTemplate.update("DELETE FROM placement_applications WHERE student_id = ?", studentId); } catch (Exception ignored) {}
        try { jdbcTemplate.update("DELETE FROM student_fee_records WHERE student_id = ?", studentId); } catch (Exception ignored) {}
        try { jdbcTemplate.update("DELETE FROM fee_receipts WHERE student_id = ?", studentId); } catch (Exception ignored) {}
        try { jdbcTemplate.update("DELETE FROM bonafide_requests WHERE student_id = ?", studentId); } catch (Exception ignored) {}
        try { jdbcTemplate.update("DELETE FROM resumes WHERE student_id = ?", studentId); } catch (Exception ignored) {}
        try { jdbcTemplate.update("DELETE FROM hostel_applications WHERE student_id = ?", studentId); } catch (Exception ignored) {}
        try { jdbcTemplate.update("DELETE FROM semester_results WHERE student_id = ?", studentId); } catch (Exception ignored) {}
        try { jdbcTemplate.update("DELETE FROM hall_tickets WHERE student_id = ?", studentId); } catch (Exception ignored) {}
        try { jdbcTemplate.update("DELETE FROM elective_enrollments WHERE student_id = ?", studentId); } catch (Exception ignored) {}
        try { jdbcTemplate.update("DELETE FROM academic_performances WHERE student_id = ?", studentId); } catch (Exception ignored) {}

        if (userId != null) {
            try { jdbcTemplate.update("DELETE FROM notifications WHERE user_id = ?", userId); } catch (Exception ignored) {}
        }

        studentRepository.delete(student);

        if (userId != null) {
            try { userRepository.deleteById(userId); } catch (Exception ignored) {}
        } else if (enrollmentNo != null) {
            try {
                userRepository.findByUsername(enrollmentNo).ifPresent(userRepository::delete);
            } catch (Exception ignored) {}
        }

        return true;
    }

    public Optional<StudentProfileDto> updateProfile(Long studentId, StudentProfileUpdateRequest request) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            return Optional.empty();
        }

        Student student = studentOpt.get();

        if (request.getPhone() != null) {
            student.setPhone(request.getPhone().trim());
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            student.setEmail(request.getEmail().trim());
        }
        if (request.getFamilyIncome() != null) {
            student.setFamilyIncome(request.getFamilyIncome());
        }
        if (request.getAddress() != null) {
            student.setAddress(request.getAddress().trim());
        }

        Student updated = studentRepository.save(student);
        return Optional.of(mapToProfileDto(updated));
    }

    private StudentProfileDto mapToProfileDto(Student student) {
        return mapToProfileDto(student, true);
    }

    private StudentProfileDto mapToProfileDto(Student student, boolean includeResumes) {
        StudentProfileDto dto = new StudentProfileDto();
        dto.setId(student.getId());
        if (student.getUser() != null) {
            dto.setUserId(student.getUser().getId());
        }
        dto.setEnrollmentNo(student.getEnrollmentNo());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setBranch(student.getBranch());
        dto.setSemester(student.getSemester());
        dto.setCgpa(student.getCgpa());
        dto.setBacklogs(student.getBacklogs());
        dto.setFamilyIncome(student.getFamilyIncome());
        dto.setAddress(student.getAddress());
        dto.setGender(student.getGender() != null ? student.getGender() : "Male");

        // Fetch associated resumes only when explicitly requested
        if (includeResumes) {
            try {
                List<ResumeDto> resumes = resumeService.getStudentResumes(student.getId());
                dto.setResumes(resumes);
                dto.setActiveResume(resumeService.getLatestResume(student.getId()).orElse(null));
            } catch (Exception ignored) {}
        }

        return dto;
    }
}
