package com.svvv.erp.config;

import com.svvv.erp.entity.*;
import com.svvv.erp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ElectiveCourseRepository electiveCourseRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private PlacementApplicationRepository placementApplicationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> Checking & Initializing SVVV ERP System Data...");

        // Ensure database schema tables & columns are up-to-date
        try {
            jdbcTemplate.execute("ALTER TABLE bonafide_requests ADD COLUMN IF NOT EXISTS father_name VARCHAR(150);");
            jdbcTemplate.execute("ALTER TABLE bonafide_requests ADD COLUMN IF NOT EXISTS remarks VARCHAR(500);");
            jdbcTemplate.execute("ALTER TABLE bonafide_requests ADD COLUMN IF NOT EXISTS reference_no VARCHAR(100);");
            jdbcTemplate.execute("UPDATE bonafide_requests SET purpose = 'Bonafide Certificate' WHERE purpose IS NULL OR purpose != 'Bonafide Certificate';");
            jdbcTemplate.execute("UPDATE bonafide_requests SET certificate_path = '/certificates/bonafide_certificate.pdf' WHERE status = 'APPROVED';");

            // Academic Subjects Table
            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS academic_subjects (
                    id BIGSERIAL PRIMARY KEY,
                    subject_code VARCHAR(50) NOT NULL,
                    subject_name VARCHAR(150) NOT NULL,
                    branch VARCHAR(100) NOT NULL,
                    semester INT NOT NULL,
                    notice VARCHAR(255),
                    course_outline_url VARCHAR(255),
                    type VARCHAR(20) DEFAULT 'THEORY',
                    faculty_name VARCHAR(100),
                    credits INT DEFAULT 4
                );
            """);
            jdbcTemplate.execute("ALTER TABLE academic_subjects ADD COLUMN IF NOT EXISTS course_outline_url VARCHAR(255);");
            jdbcTemplate.execute("ALTER TABLE academic_subjects ADD COLUMN IF NOT EXISTS notice VARCHAR(255);");
            jdbcTemplate.execute("ALTER TABLE academic_subjects ADD COLUMN IF NOT EXISTS type VARCHAR(20) DEFAULT 'THEORY';");
            jdbcTemplate.execute("ALTER TABLE academic_subjects ADD COLUMN IF NOT EXISTS faculty_name VARCHAR(100);");
            jdbcTemplate.execute("ALTER TABLE academic_subjects ADD COLUMN IF NOT EXISTS credits INT DEFAULT 4;");

            // Fee tables
            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS fee_structures (
                    id BIGSERIAL PRIMARY KEY,
                    branch VARCHAR(255),
                    semester INT,
                    tuition_fee DOUBLE PRECISION,
                    exam_fee DOUBLE PRECISION,
                    library_resource_fee DOUBLE PRECISION,
                    total_amount DOUBLE PRECISION
                );
            """);

            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS student_fee_records (
                    id BIGSERIAL PRIMARY KEY,
                    student_id BIGINT REFERENCES students(id) ON DELETE CASCADE,
                    semester INT,
                    total_payable_amount DOUBLE PRECISION,
                    paid_amount DOUBLE PRECISION,
                    due_amount DOUBLE PRECISION,
                    status VARCHAR(50),
                    academic_year VARCHAR(50)
                );
            """);

            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS fee_receipts (
                    id BIGSERIAL PRIMARY KEY,
                    student_id BIGINT REFERENCES students(id) ON DELETE CASCADE,
                    semester VARCHAR(50),
                    transaction_id VARCHAR(100),
                    payment_date DATE,
                    amount_paid DOUBLE PRECISION,
                    payment_mode VARCHAR(100),
                    status VARCHAR(50),
                    receipt_pdf_path VARCHAR(255)
                );
            """);

            // Default Fee Structures if empty
            jdbcTemplate.execute("""
                INSERT INTO fee_structures (branch, semester, tuition_fee, exam_fee, library_resource_fee, total_amount)
                SELECT 'B.Tech Computer Science & Engineering', 7, 50000.00, 5000.00, 0.00, 55000.00
                WHERE NOT EXISTS (SELECT 1 FROM fee_structures WHERE semester = 7 AND branch LIKE '%Computer Science%');
            """);

            // Examination tables
            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS academic_performances (
                    id BIGSERIAL PRIMARY KEY,
                    student_id BIGINT UNIQUE REFERENCES students(id) ON DELETE CASCADE,
                    cgpa DOUBLE PRECISION,
                    sgpa_last_sem DOUBLE PRECISION,
                    active_backlogs INT,
                    total_credits_secured INT,
                    total_credits_required INT,
                    updated_at TIMESTAMP
                );
            """);

            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS hall_tickets (
                    id BIGSERIAL PRIMARY KEY,
                    student_id BIGINT REFERENCES students(id) ON DELETE CASCADE,
                    semester INT,
                    session_name VARCHAR(100),
                    commencement_date DATE,
                    exam_center VARCHAR(255),
                    reporting_time VARCHAR(100),
                    attendance_percentage DOUBLE PRECISION,
                    is_fee_cleared BOOLEAN,
                    hall_ticket_number VARCHAR(100)
                );
            """);

            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS semester_results (
                    id BIGSERIAL PRIMARY KEY,
                    student_id BIGINT REFERENCES students(id) ON DELETE CASCADE,
                    semester_name VARCHAR(100),
                    semester_number INT,
                    session_period VARCHAR(100),
                    enrolled_credits INT,
                    backlogs_in_sem INT,
                    sgpa DOUBLE PRECISION,
                    status VARCHAR(50),
                    grade_card_pdf_path VARCHAR(255)
                );
            """);

            // Hostel applications table & student gender
            jdbcTemplate.execute("ALTER TABLE students ADD COLUMN IF NOT EXISTS gender VARCHAR(20) DEFAULT 'Male';");
            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS hostel_applications (
                    application_id BIGSERIAL PRIMARY KEY,
                    student_id BIGINT REFERENCES students(id) ON DELETE CASCADE,
                    hostel_wing VARCHAR(100),
                    room_occupancy_type VARCHAR(100),
                    base_room_fee DOUBLE PRECISION,
                    climate_facility VARCHAR(100),
                    climate_fee_addon DOUBLE PRECISION,
                    total_payable_fee DOUBLE PRECISION,
                    guardian_contact VARCHAR(50),
                    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    allotted_room_no VARCHAR(100),
                    warden_name VARCHAR(150)
                );
            """);

            // Campus announcements table
            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS campus_announcements (
                    id BIGSERIAL PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    target_audience VARCHAR(150) NOT NULL,
                    content TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
            """);
        } catch (Exception ex) {
            System.out.println(">>> Schema column/table check note: " + ex.getMessage());
        }

        // Synchronize PostgreSQL sequences to avoid duplicate key issues on identity columns
        try {
            jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('users', 'id'), COALESCE((SELECT MAX(id) FROM users), 0) + 1, false);");
            jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('students', 'id'), COALESCE((SELECT MAX(id) FROM students), 0) + 1, false);");
            jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('companies', 'id'), COALESCE((SELECT MAX(id) FROM companies), 0) + 1, false);");
            jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('elective_courses', 'id'), COALESCE((SELECT MAX(id) FROM elective_courses), 0) + 1, false);");
            jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('notifications', 'id'), COALESCE((SELECT MAX(id) FROM notifications), 0) + 1, false);");
            jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('campus_announcements', 'id'), COALESCE((SELECT MAX(id) FROM campus_announcements), 0) + 1, false);");
        } catch (Exception ex) {
            System.out.println(">>> Sequence sync note: " + ex.getMessage());
        }

        // 1. Create Admin User
        try {
            if (!userRepository.existsByUsername("admin")) {
                User adminUser = new User("admin", passwordEncoder.encode("admin123"), "ADMIN");
                userRepository.save(adminUser);
            }
        } catch (Exception e) {
            System.out.println(">>> Admin seeding note: " + e.getMessage());
        }

        // 2. Create Student 1 & 2 only if table is completely unseeded
        if (studentRepository.count() == 0) {
            try {
                User studentUser1 = userRepository.findByUsername("0827CS211001").orElse(null);
                if (studentUser1 == null) {
                    studentUser1 = new User("0827CS211001", passwordEncoder.encode("student123"), "STUDENT");
                    studentUser1 = userRepository.save(studentUser1);
                }

                Student student1 = studentRepository.findByEnrollmentNo("0827CS211001").orElse(null);
                if (student1 == null) {
                    student1 = new Student(
                            studentUser1,
                            "0827CS211001",
                            "Yash Prajapati",
                            "yash.cs21@svvv.edu.in",
                            "9876543210",
                            "Computer Science & Engineering",
                            7,
                            8.45,
                            0,
                            450000.0,
                            "Indore, Madhya Pradesh"
                    );
                    studentRepository.save(student1);

                    Notification notif1 = new Notification(
                            studentUser1,
                            "Welcome to SVVV Student ERP Portal",
                            "Your centralized academic profile is active. Check your Placement eligibility and Generic Electives.",
                            "GENERAL"
                    );
                    notificationRepository.save(notif1);
                }
            } catch (Exception e) {
                System.out.println(">>> Seed Student 1 note: " + e.getMessage());
            }

            try {
                User studentUser2 = userRepository.findByUsername("0827IT211045").orElse(null);
                if (studentUser2 == null) {
                    studentUser2 = new User("0827IT211045", passwordEncoder.encode("student123"), "STUDENT");
                    studentUser2 = userRepository.save(studentUser2);
                }

                Student student2 = studentRepository.findByEnrollmentNo("0827IT211045").orElse(null);
                if (student2 == null) {
                    student2 = new Student(
                            studentUser2,
                            "0827IT211045",
                            "Aman Sharma",
                            "aman.it21@svvv.edu.in",
                            "9812345678",
                            "Information Technology",
                            7,
                            7.10,
                            1,
                            380000.0,
                            "Ujjain, Madhya Pradesh"
                    );
                    studentRepository.save(student2);

                    Notification notif2 = new Notification(
                            studentUser2,
                            "Welcome to SVVV Student ERP Portal",
                            "Your centralized academic profile is active. Check your Placement eligibility and Generic Electives.",
                            "GENERAL"
                    );
                    notificationRepository.save(notif2);
                }
            } catch (Exception e) {
                System.out.println(">>> Seed Student 2 note: " + e.getMessage());
            }
        }

        // 4. Create Student 3 (Yash Rathore)
        try {
            User studentUser3 = userRepository.findByUsername("23100BTCSE15208").orElse(null);
            if (studentUser3 == null) {
                studentUser3 = new User("23100BTCSE15208", passwordEncoder.encode("yash123"), "STUDENT");
                studentUser3 = userRepository.save(studentUser3);
            }

            Student student3 = studentRepository.findByEnrollmentNo("23100BTCSE15208").orElse(null);
            if (student3 == null) {
                student3 = new Student(
                        studentUser3,
                        "23100BTCSE15208",
                        "Yash Rathore",
                        "yash.rathore21@svvv.edu.in",
                        "9993916371",
                        "Computer Science & Engineering",
                        7,
                        7.98,
                        0,
                        500000.0,
                        "Indore, Madhya Pradesh"
                );
                studentRepository.save(student3);

                Notification notif3 = new Notification(
                        studentUser3,
                        "Welcome Yash Rathore to SVVV ERP Portal",
                        "Your centralized profile (CGPA: 7.98) is active. Placement eligibility and Generic Electives are ready.",
                        "GENERAL"
                );
                notificationRepository.save(notif3);
                System.out.println(">>> Student 'Yash Rathore' (23100BTCSE15208) successfully seeded!");
            } else if (student3.getUser() == null || !student3.getUser().getId().equals(studentUser3.getId())) {
                student3.setUser(studentUser3);
                studentRepository.save(student3);
            }
        } catch (Exception e) {
            System.out.println(">>> Seed Student 3 note: " + e.getMessage());
        }

        // 5. Seed Placement Companies
        try {
            if (companyRepository.count() == 0) {
                Company comp1 = new Company(
                        "TCS Digital",
                        "System Engineer",
                        7.5,
                        7.0,
                        "CSE,IT,ECE",
                        0,
                        LocalDate.now().plusDays(15),
                        "ACTIVE"
                );
                companyRepository.save(comp1);

                Company comp2 = new Company(
                        "Infosys Specialist Programmer",
                        "Specialist Programmer",
                        9.5,
                        8.0,
                        "CSE,IT",
                        0,
                        LocalDate.now().plusDays(20),
                        "ACTIVE"
                );
                companyRepository.save(comp2);

                Company comp3 = new Company(
                        "Wipro Elite",
                        "Project Engineer",
                        4.5,
                        6.0,
                        "ALL",
                        1,
                        LocalDate.now().plusDays(10),
                        "ACTIVE"
                );
                companyRepository.save(comp3);
            }
        } catch (Exception e) {
            System.out.println(">>> Seed Companies note: " + e.getMessage());
        }

        // 6. Seed Elective Courses
        try {
            if (electiveCourseRepository.count() == 0) {
                ElectiveCourse course1 = new ElectiveCourse(
                        "CS-704",
                        "Cloud Architecture & Services",
                        120,
                        "OPEN"
                );
                electiveCourseRepository.save(course1);

                ElectiveCourse course2 = new ElectiveCourse(
                        "CS-701",
                        "Deep Learning & Neural Networks",
                        120,
                        "OPEN"
                );
                electiveCourseRepository.save(course2);

                ElectiveCourse course3 = new ElectiveCourse(
                        "CS-705",
                        "Cyber Security & Digital Forensics",
                        60,
                        "OPEN"
                );
                electiveCourseRepository.save(course3);

                ElectiveCourse course4 = new ElectiveCourse(
                        "CS-708",
                        "Full Stack Web Engineering",
                        100,
                        "OPEN"
                );
                electiveCourseRepository.save(course4);

                ElectiveCourse course5 = new ElectiveCourse(
                        "CS-709",
                        "Financial Tech & Blockchain",
                        90,
                        "OPEN"
                );
                electiveCourseRepository.save(course5);

                ElectiveCourse course6 = new ElectiveCourse(
                        "CS-712",
                        "Robotics & Embedded Systems",
                        60,
                        "OPEN"
                );
                electiveCourseRepository.save(course6);
            }
        } catch (Exception e) {
            System.out.println(">>> Seed Electives note: " + e.getMessage());
        }

        // 7. Seed Sample Placement Applications & Resumes
        try {
            if (placementApplicationRepository.count() == 0) {
                Student s1 = studentRepository.findByEnrollmentNo("0827CS211001").orElse(null);
                Student s3 = studentRepository.findByEnrollmentNo("23100BTCSE15208").orElse(null);
                Company c1 = companyRepository.findById(1L).orElse(null);

                if (s1 != null && c1 != null) {
                    Resume r1 = new Resume(s1, "Yash_Prajapati_Resume.pdf", "/uploads/resumes/yash_resume.pdf");
                    resumeRepository.save(r1);
                    PlacementApplication app1 = new PlacementApplication(s1, c1, r1, "APPLIED");
                    placementApplicationRepository.save(app1);
                }
                if (s3 != null && c1 != null) {
                    Resume r3 = new Resume(s3, "Yash_Rathore_Resume.pdf", "/uploads/resumes/yash_rathore_resume.pdf");
                    resumeRepository.save(r3);
                    PlacementApplication app2 = new PlacementApplication(s3, c1, r3, "SHORTLISTED");
                    placementApplicationRepository.save(app2);
                }
            }
        } catch (Exception e) {
            System.out.println(">>> Seed Placement Apps note: " + e.getMessage());
        }

        // 8. Seed Default Campus Announcements
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM campus_announcements", Integer.class);
            if (count == null || count == 0) {
                jdbcTemplate.update("""
                    INSERT INTO campus_announcements (title, target_audience, content, created_at)
                    VALUES 
                    ('Schedule for TCS Digital Campus Recruitment Assessment', 'All Enrolled Students (Campus Wide)', 'Online coding assessment is confirmed for 24th May, 10:00 AM at the Main Computing Center (Lab 3 & 4). Students must carry their institutional ID badges and updated resume copy.', NOW() - INTERVAL '2 hours'),
                    ('Autumn 2026 Generic Elective Phase-II Course Lock Deadline', 'B.Tech Computer Science & Engineering', 'Students who have not locked their Generic Elective preferences must finalize their choices before Friday, 5:00 PM. Unselected candidates will be allocated automatically based on vacancy.', NOW() - INTERVAL '1 day'),
                    ('Hostel Monsoon Maintenance Clearance & Room Inspection', 'All Enrolled Students (Campus Wide)', 'Residential warden team will conduct the quarterly electrical and fire-safety inspection for all hostel wings this weekend. Please coordinate with floor prefects.', NOW() - INTERVAL '3 days')
                """);
                System.out.println(">>> Seeded default campus announcements!");
            }
        } catch (Exception e) {
            System.out.println(">>> Seed Campus Announcements note: " + e.getMessage());
        }

        System.out.println(">>> Database synchronization complete!");
    }
}
