package com.svvv.erp.service;

import com.svvv.erp.dto.LoginRequest;
import com.svvv.erp.dto.LoginResponse;
import com.svvv.erp.entity.Student;
import com.svvv.erp.entity.User;
import com.svvv.erp.repository.StudentRepository;
import com.svvv.erp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse authenticate(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return LoginResponse.failure("Student ID / Username is required.");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return LoginResponse.failure("Password is required.");
        }

        String identifier = request.getUsername().trim();
        String inputPassword = request.getPassword().trim();
        String selectedRole = request.getRole() != null ? request.getRole().trim().toUpperCase() : "STUDENT";
        boolean isAdminRoleSelected = selectedRole.contains("ADMIN");

        // 1. Check if the identifier is Admin ("admin" or user in DB with role containing "ADMIN")
        boolean isAdminIdentifier = "admin".equalsIgnoreCase(identifier);
        Optional<User> adminUserOpt = userRepository.findByUsername(identifier);
        if (!isAdminIdentifier && adminUserOpt.isPresent()) {
            String uRole = adminUserOpt.get().getRole();
            if (uRole != null && uRole.toUpperCase().contains("ADMIN")) {
                isAdminIdentifier = true;
            }
        }

        if (isAdminIdentifier) {
            User adminUser = adminUserOpt.orElseGet(() -> userRepository.findByUsername("admin").orElse(null));
            boolean passwordMatches = false;
            if ("admin123".equals(inputPassword)) {
                passwordMatches = true;
            } else if (adminUser != null) {
                try {
                    if (passwordEncoder.matches(inputPassword, adminUser.getPassword())) {
                        passwordMatches = true;
                    }
                } catch (Exception ignored) {}
                if (!passwordMatches && adminUser.getPassword() != null && adminUser.getPassword().equals(inputPassword)) {
                    passwordMatches = true;
                }
            }

            if (!passwordMatches) {
                return LoginResponse.failure("Invalid Enrollment Number / Username or Password.");
            }

            // Role check: If admin enters credentials on Student tab
            if (!isAdminRoleSelected) {
                return LoginResponse.failure("Invalid credentials or role mismatch. Please select Admin Login.");
            }

            if (adminUser == null) {
                adminUser = new User("admin", passwordEncoder.encode("admin123"), "ROLE_ADMIN");
                adminUser = userRepository.save(adminUser);
            }

            LoginResponse adminResp = LoginResponse.success(
                    adminUser.getId(),
                    "admin",
                    "ROLE_ADMIN",
                    "Admin authentication successful! Welcome to SVVV ERP Console."
            );
            adminResp.setName("Administrator");
            adminResp.setEmail("admin@svvv.edu.in");
            return adminResp;
        }

        // 2. Student Authentication
        // Try to find student by enrollment number, username, or email
        Optional<Student> studentOpt = studentRepository.findByEnrollmentNo(identifier);
        if (studentOpt.isEmpty()) {
            studentOpt = studentRepository.findByUser_Username(identifier);
        }
        if (studentOpt.isEmpty()) {
            studentOpt = studentRepository.findAll().stream()
                    .filter(s -> s.getEmail() != null && s.getEmail().equalsIgnoreCase(identifier))
                    .findFirst();
        }

        Student student = studentOpt.orElse(null);

        // Calculate expected default password: 'student' + last 5 digits of Enrollment No
        String expectedStudentPassword = null;
        if (student != null && student.getEnrollmentNo() != null) {
            String enroll = student.getEnrollmentNo().trim();
            String last5 = enroll.length() >= 5 ? enroll.substring(enroll.length() - 5) : enroll;
            expectedStudentPassword = "student" + last5;
        }

        // Find or auto-provision associated User
        User user = null;
        if (student != null) {
            user = student.getUser();
            if (user == null) {
                user = userRepository.findByUsername(student.getEnrollmentNo()).orElseGet(() -> {
                    User newUser = new User(student.getEnrollmentNo(), passwordEncoder.encode(inputPassword), "ROLE_STUDENT");
                    return userRepository.save(newUser);
                });
                student.setUser(user);
                studentRepository.save(student);
            }
        } else {
            user = userRepository.findByUsername(identifier).orElse(null);
        }

        if (user == null && student == null) {
            return LoginResponse.failure("Invalid Enrollment Number / Username or Password.");
        }

        // Password matching protocol
        boolean passwordMatches = false;

        // Rule 1: 'student' + last 5 digits of Enrollment No
        if (expectedStudentPassword != null && expectedStudentPassword.equalsIgnoreCase(inputPassword)) {
            passwordMatches = true;
        }

        // Rule 2: Default dev/student password fallback
        if (!passwordMatches && "student123".equals(inputPassword)) {
            passwordMatches = true;
        }

        // Rule 3: Database BCrypt or plain-text match
        if (!passwordMatches && user != null) {
            try {
                passwordMatches = passwordEncoder.matches(inputPassword, user.getPassword());
            } catch (Exception ignored) {}

            if (!passwordMatches && user.getPassword() != null && user.getPassword().equals(inputPassword)) {
                passwordMatches = true;
            }
        }

        if (!passwordMatches) {
            return LoginResponse.failure("Invalid Enrollment Number / Username or Password.");
        }

        // Role check: If student enters credentials on Admin tab
        if (isAdminRoleSelected) {
            return LoginResponse.failure("Access Denied: Student credentials cannot access Admin Console.");
        }

        // Update password to BCrypt in DB if needed
        if (user != null && !passwordEncoder.matches(inputPassword, user.getPassword())) {
            try {
                user.setPassword(passwordEncoder.encode(inputPassword));
                userRepository.save(user);
            } catch (Exception ignored) {}
        }

        String role = "ROLE_STUDENT";

        // Build Clean Success Response
        LoginResponse response = LoginResponse.success(
                user != null ? user.getId() : (student != null ? student.getId() : 1L),
                student != null ? student.getEnrollmentNo() : (user != null ? user.getUsername() : identifier),
                role,
                "Login successful! Welcome to SVVV ERP Portal."
        );

        if (student != null) {
            response.setStudentId(student.getId());
            response.setName(student.getName());
            response.setEmail(student.getEmail());
            response.setEnrollmentNo(student.getEnrollmentNo());
            response.setBranch(student.getBranch());
            response.setSemester(student.getSemester());
            response.setCgpa(student.getCgpa());
            response.setBacklogs(student.getBacklogs());
        }

        return response;
    }
}
