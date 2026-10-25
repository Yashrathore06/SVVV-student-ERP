package com.svvv.erp.service;

import com.svvv.erp.dto.AcademicCalendarDto;
import com.svvv.erp.dto.AcademicCalendarEventDto;
import com.svvv.erp.entity.AcademicSubject;
import com.svvv.erp.repository.AcademicSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class AcademicService {

    @Autowired
    private AcademicSubjectRepository academicSubjectRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @jakarta.annotation.PostConstruct
    public void init() {
        try {
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
        } catch (Exception e) {
            System.err.println("Academic schema migration: " + e.getMessage());
        }
    }

    @Transactional
    public List<AcademicSubject> getSubjectsForBranchAndSemester(String branchInput, Integer semesterInput) {
        String branch = normalizeBranch(branchInput);
        int semester = (semesterInput != null && semesterInput > 0) ? semesterInput : getDefaultSemester(branch);

        // Ensure database has curriculum subjects seeded
        ensureSubjectsSeeded();

        List<AcademicSubject> subjects = academicSubjectRepository.findByBranchAndSemester(branch, semester);
        if (subjects.isEmpty()) {
            subjects = academicSubjectRepository.findBySemester(semester);
        }
        if (subjects.isEmpty()) {
            subjects = academicSubjectRepository.findByBranchAndSemester("B.Tech CSE", 7);
        }
        return subjects;
    }

    public AcademicCalendarDto getAcademicCalendar() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        String monthName = today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String monthYear = monthName + " " + year;
        int currentDay = today.getDayOfMonth();
        int daysInMonth = today.lengthOfMonth();
        LocalDate firstDay = today.withDayOfMonth(1);
        int firstDayOfWeek = firstDay.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

        List<AcademicCalendarEventDto> events = new ArrayList<>();
        events.add(new AcademicCalendarEventDto(2, monthName.substring(0, 3) + " 02", "National Holiday (Gandhi Jayanti)", "HOLIDAY", "orange", "holiday"));
        events.add(new AcademicCalendarEventDto(15, monthName.substring(0, 3) + " 15", "Mid-Term Examination & Assessments", "ACADEMIC", "blue", "academic"));
        events.add(new AcademicCalendarEventDto(20, monthName.substring(0, 3) + " 20", "Diwali Festivities & Autumn Recess", "HOLIDAY", "orange", "holiday"));
        events.add(new AcademicCalendarEventDto(28, monthName.substring(0, 3) + " 28", "Autonomous Syllabus Review & BOS", "EVENT", "green", "event"));
        events.add(new AcademicCalendarEventDto(31, monthName.substring(0, 3) + " 31", "End-Sem Project (Phase 1) Submission", "ACADEMIC", "blue", "academic"));

        List<String> notices = new ArrayList<>();
        notices.add("Autonomous Mid-Term Examination schedule published for all active semesters.");
        notices.add("Central LMS outline and lecture notes synchronized with Academic Registry.");
        notices.add("Attendance threshold notice (minimum 75%) active for semester hall tickets.");
        notices.add("Generic Elective Course Lock active for registered students.");

        return new AcademicCalendarDto(year, month, monthName, monthYear, currentDay, daysInMonth, firstDayOfWeek, events, notices);
    }

    @Transactional
    public void ensureSubjectsSeeded() {
        if (academicSubjectRepository.findByBranchAndSemester("B.Tech CSE", 7).isEmpty()) {
            List<AcademicSubject> btechList = new ArrayList<>();
            btechList.add(new AcademicSubject("BBAI501", "Human Values and Professional Ethics", "B.Tech CSE", 7, null, "/syllabus/bbai501.pdf", "THEORY", "Dr. A. Sharma", 3));
            btechList.add(new AcademicSubject("BTCS705N", "Industrial Training", "B.Tech CSE", 7, "Report Submission Due", "/syllabus/btcs705n.pdf", "PRACTICAL", "Prof. R. Patidar", 4));
            btechList.add(new AcademicSubject("BTCS706N", "Major Project (Phase I)", "B.Tech CSE", 7, "Synopsis Review", "/syllabus/btcs706n.pdf", "PRACTICAL", "Dr. S. K. Mittal", 6));
            btechList.add(new AcademicSubject("BTCS710", "Deep Learning Foundations", "B.Tech CSE", 7, null, "/syllabus/btcs710.pdf", "THEORY", "Prof. M. Verma", 4));
            btechList.add(new AcademicSubject("BTCS710(P)", "Deep Learning Foundations Lab", "B.Tech CSE", 7, null, "/syllabus/btcs710p.pdf", "PRACTICAL", "Prof. M. Verma", 2));
            btechList.add(new AcademicSubject("BTCS711", "Introduction to Quantum Computing", "B.Tech CSE", 7, null, "/syllabus/btcs711.pdf", "THEORY", "Dr. P. Sen", 4));
            btechList.add(new AcademicSubject("BTCS711(P)", "Quantum Computing Lab", "B.Tech CSE", 7, null, "/syllabus/btcs711p.pdf", "PRACTICAL", "Dr. P. Sen", 2));
            btechList.add(new AcademicSubject("BTCS712", "Cloud Competency Lab", "B.Tech CSE", 7, "AWS Credits Distributed", "/syllabus/btcs712.pdf", "PRACTICAL", "Prof. K. Chouhan", 3));
            btechList.add(new AcademicSubject("BTDSE722", "Prompt Engineering and Fine-Tuning", "B.Tech CSE", 7, null, "/syllabus/btdse722.pdf", "THEORY", "Dr. V. Rathore", 3));
            btechList.add(new AcademicSubject("BTDSE722(P)", "Prompt Engineering Lab", "B.Tech CSE", 7, null, "/syllabus/btdse722p.pdf", "PRACTICAL", "Dr. V. Rathore", 2));
            academicSubjectRepository.saveAll(btechList);
        }

        if (academicSubjectRepository.findByBranchAndSemester("B.Sc CS", 5).isEmpty()) {
            List<AcademicSubject> bscList = new ArrayList<>();
            bscList.add(new AcademicSubject("BSCS501", "Advanced Java Programming", "B.Sc CS", 5, null, "/syllabus/bscs501.pdf", "THEORY", "Prof. N. Joshi", 4));
            bscList.add(new AcademicSubject("BSCS502", "Computer Networks & Security", "B.Sc CS", 5, null, "/syllabus/bscs502.pdf", "THEORY", "Dr. H. Solanki", 4));
            bscList.add(new AcademicSubject("BSCS503", "Web Application Development", "B.Sc CS", 5, "Project Allocated", "/syllabus/bscs503.pdf", "THEORY", "Prof. T. Mishra", 4));
            bscList.add(new AcademicSubject("BSCS504", "Software Engineering Principles", "B.Sc CS", 5, null, "/syllabus/bscs504.pdf", "THEORY", "Prof. S. Gupta", 3));
            bscList.add(new AcademicSubject("BSCS505(P)", "Advanced Java Lab", "B.Sc CS", 5, null, "/syllabus/bscs505p.pdf", "PRACTICAL", "Prof. N. Joshi", 2));
            bscList.add(new AcademicSubject("BSCS506(P)", "Web Application Lab", "B.Sc CS", 5, null, "/syllabus/bscs506p.pdf", "PRACTICAL", "Prof. T. Mishra", 2));
            academicSubjectRepository.saveAll(bscList);
        }

        if (academicSubjectRepository.findByBranchAndSemester("MBA", 3).isEmpty()) {
            List<AcademicSubject> mbaList = new ArrayList<>();
            mbaList.add(new AcademicSubject("MBA301", "Strategic Management", "MBA", 3, "Case Study Due", "/syllabus/mba301.pdf", "THEORY", "Dr. K. Saxena", 4));
            mbaList.add(new AcademicSubject("MBA302", "Business Analytics & Big Data", "MBA", 3, null, "/syllabus/mba302.pdf", "THEORY", "Prof. R. Nair", 4));
            mbaList.add(new AcademicSubject("MBA303", "Consumer Behavior & Marketing Research", "MBA", 3, null, "/syllabus/mba303.pdf", "THEORY", "Dr. P. Tiwari", 4));
            mbaList.add(new AcademicSubject("MBA304", "Financial Derivatives & Risk Management", "MBA", 3, null, "/syllabus/mba304.pdf", "THEORY", "Prof. D. K. Dave", 4));
            mbaList.add(new AcademicSubject("MBA305", "Human Resource Development", "MBA", 3, null, "/syllabus/mba305.pdf", "THEORY", "Dr. A. Agrawal", 4));
            mbaList.add(new AcademicSubject("MBA306", "Supply Chain & Logistics Management", "MBA", 3, null, "/syllabus/mba306.pdf", "THEORY", "Prof. M. Bhati", 3));
            academicSubjectRepository.saveAll(mbaList);
        }

        if (academicSubjectRepository.findByBranchAndSemester("BCA", 5).isEmpty()) {
            List<AcademicSubject> bcaList = new ArrayList<>();
            bcaList.add(new AcademicSubject("BCA501", "Python Programming & Data Science", "BCA", 5, null, "/syllabus/bca501.pdf", "THEORY", "Prof. Y. Shrivastava", 4));
            bcaList.add(new AcademicSubject("BCA502", "Information Security & Cyber Laws", "BCA", 5, null, "/syllabus/bca502.pdf", "THEORY", "Dr. R. K. Jain", 4));
            bcaList.add(new AcademicSubject("BCA503", "Mobile Application Development", "BCA", 5, "Flutter App Submission", "/syllabus/bca503.pdf", "THEORY", "Prof. S. Rajput", 4));
            bcaList.add(new AcademicSubject("BCA504", "Cloud Computing Essentials", "BCA", 5, null, "/syllabus/bca504.pdf", "THEORY", "Prof. A. Soni", 3));
            bcaList.add(new AcademicSubject("BCA505(P)", "Python & Data Science Lab", "BCA", 5, null, "/syllabus/bca505p.pdf", "PRACTICAL", "Prof. Y. Shrivastava", 2));
            bcaList.add(new AcademicSubject("BCA506(P)", "Mobile App Development Lab", "BCA", 5, null, "/syllabus/bca506p.pdf", "PRACTICAL", "Prof. S. Rajput", 2));
            academicSubjectRepository.saveAll(bcaList);
        }
    }

    private String normalizeBranch(String branch) {
        if (branch == null || branch.trim().isEmpty()) return "B.Tech CSE";
        String lower = branch.toLowerCase();
        if (lower.contains("b.sc") || lower.contains("bsc")) return "B.Sc CS";
        if (lower.contains("bca")) return "BCA";
        if (lower.contains("mba")) return "MBA";
        return "B.Tech CSE";
    }

    private int getDefaultSemester(String branch) {
        if ("MBA".equalsIgnoreCase(branch)) return 3;
        if ("B.Sc CS".equalsIgnoreCase(branch) || "BCA".equalsIgnoreCase(branch)) return 5;
        return 7;
    }
}
