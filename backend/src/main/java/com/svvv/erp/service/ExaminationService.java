package com.svvv.erp.service;

import com.svvv.erp.dto.ExaminationResponseDto;
import com.svvv.erp.dto.HallTicketDto;
import com.svvv.erp.dto.SemesterResultDto;
import com.svvv.erp.entity.*;
import com.svvv.erp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExaminationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AcademicPerformanceRepository academicPerformanceRepository;

    @Autowired
    private HallTicketRepository hallTicketRepository;

    @Autowired
    private SemesterResultRepository semesterResultRepository;

    @Autowired
    private StudentFeeRecordRepository studentFeeRecordRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    @Transactional
    public ExaminationResponseDto getExaminationDetailsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        int currentSem = student.getSemester() != null ? student.getSemester() : 7;
        String branch = student.getBranch() != null ? student.getBranch() : "B.Tech Computer Science & Engineering";

        // 1. Get or Create Academic Performance
        AcademicPerformance performance = academicPerformanceRepository.findByStudentId(studentId)
                .orElseGet(() -> {
                    double cgpaVal = student.getCgpa() != null ? student.getCgpa() : 8.42;
                    int backlogs = student.getBacklogs() != null ? student.getBacklogs() : 0;
                    AcademicPerformance ap = new AcademicPerformance(
                            student,
                            cgpaVal,
                            8.50,
                            backlogs,
                            24,
                            24
                    );
                    return academicPerformanceRepository.save(ap);
                });

        // 2. Determine Fee Clearance dynamically from student fee ledger
        boolean isFeeCleared = studentFeeRecordRepository.findByStudentIdAndSemester(studentId, currentSem)
                .map(r -> r.getDueAmount() == 0.0 || "PAID".equalsIgnoreCase(r.getStatus()))
                .orElse(true);

        // 3. Get or Create Hall Ticket
        HallTicket hallTicket = hallTicketRepository.findByStudentIdAndSemester(studentId, currentSem)
                .orElseGet(() -> {
                    String htNumber = "HT-2026-AUT-" + (student.getEnrollmentNo() != null ? student.getEnrollmentNo() : "23100BTCSE15208");
                    HallTicket ht = new HallTicket(
                            student,
                            currentSem,
                            "Autumn 2026",
                            LocalDate.of(2026, 11, 18),
                            "Block B - SVVV Main Campus",
                            "09:30 AM (Morning Shift)",
                            82.0,
                            isFeeCleared,
                            htNumber
                    );
                    return hallTicketRepository.save(ht);
                });

        // Keep dynamic fee status synchronized on Hall Ticket
        hallTicket.setIsFeeCleared(isFeeCleared);
        hallTicketRepository.save(hallTicket);

        // 4. Ensure Seed Semester Results exist
        ensureSeedSemesterResults(student);

        // 5. Fetch all Semester Results
        List<SemesterResult> results = semesterResultRepository.findByStudentIdOrderBySemesterNumberDesc(studentId);

        return mapToExaminationResponse(student, performance, hallTicket, results);
    }

    private void ensureSeedSemesterResults(Student student) {
        List<SemesterResult> existing = semesterResultRepository.findByStudentIdOrderBySemesterNumberDesc(student.getId());
        if (existing.isEmpty()) {
            List<SemesterResult> seed = new ArrayList<>();
            seed.add(new SemesterResult(student, "Semester VI (Spring 2026)", 6, "Jan – May 2026", 24, 0, 8.50, "PASS", "/api/examination/gradecard/SEM6.pdf"));
            seed.add(new SemesterResult(student, "Semester V (Autumn 2025)", 5, "Jul – Dec 2025", 22, 0, 8.32, "PASS", "/api/examination/gradecard/SEM5.pdf"));
            seed.add(new SemesterResult(student, "Semester IV (Spring 2025)", 4, "Jan – May 2025", 24, 0, 8.44, "PASS", "/api/examination/gradecard/SEM4.pdf"));
            seed.add(new SemesterResult(student, "Semester III (Autumn 2024)", 3, "Jul – Dec 2024", 23, 0, 8.20, "PASS", "/api/examination/gradecard/SEM3.pdf"));
            seed.add(new SemesterResult(student, "Semester II (Spring 2024)", 2, "Jan – May 2024", 22, 0, 8.38, "PASS", "/api/examination/gradecard/SEM2.pdf"));
            seed.add(new SemesterResult(student, "Semester I (Autumn 2023)", 1, "Jul – Dec 2023", 22, 0, 8.52, "PASS", "/api/examination/gradecard/SEM1.pdf"));
            semesterResultRepository.saveAll(seed);
        }
    }

    private ExaminationResponseDto mapToExaminationResponse(Student s, AcademicPerformance p, HallTicket ht, List<SemesterResult> results) {
        ExaminationResponseDto dto = new ExaminationResponseDto();
        dto.setStudentId(s.getId());
        dto.setStudentName(s.getName());
        dto.setEnrollmentNo(s.getEnrollmentNo());
        dto.setBranch(s.getBranch());
        dto.setCurrentSemester(ht.getSemester());
        dto.setCurrentSemesterRoman(toRoman(ht.getSemester()));
        dto.setSessionTitle("B.Tech " + (s.getBranch() != null ? s.getBranch() : "Computer Science & Engineering") + " (Semester " + toRoman(ht.getSemester()) + ") • University Examination Session 2026–2027");

        // KPI 1: CGPA
        dto.setCgpa(p.getCgpa());
        dto.setCgpaDifferenceText("+0.18 vs Sem V • First Class with Distinction Track");
        dto.setCgpaProgressPercent((p.getCgpa() / 10.0) * 100);

        // KPI 2: SGPA Last Sem
        dto.setSgpaLastSem(p.getSgpaLastSem());
        dto.setTotalCreditsSecured(p.getTotalCreditsSecured());
        dto.setTotalCreditsRequired(p.getTotalCreditsRequired());
        dto.setSgpaProgressPercent((p.getSgpaLastSem() / 10.0) * 100);

        // KPI 3: Backlogs
        int backlogs = p.getActiveBacklogs() != null ? p.getActiveBacklogs() : 0;
        dto.setActiveBacklogs(backlogs);
        if (backlogs == 0) {
            dto.setBacklogStatusTag("Clean Record • All Cleared");
            dto.setBacklogTagClass("green");
            dto.setDegreeEligibilityText("Eligible for Degree with Honors");
        } else {
            dto.setBacklogStatusTag(backlogs + " Active Backlogs Pending");
            dto.setBacklogTagClass("red");
            dto.setDegreeEligibilityText("Clear outstanding ATKT before 8th Semester");
        }

        // Hall Ticket DTO
        HallTicketDto htDto = new HallTicketDto();
        htDto.setId(ht.getId());
        htDto.setSemester(ht.getSemester());
        htDto.setSemesterRoman(toRoman(ht.getSemester()));
        htDto.setSessionName(ht.getSessionName());
        htDto.setCommencementDate(ht.getCommencementDate());
        htDto.setCommencementDateFormatted(ht.getCommencementDate() != null ? ht.getCommencementDate().format(DATE_FORMATTER) : "Nov 18, 2026");
        htDto.setExamCenter(ht.getExamCenter() != null ? ht.getExamCenter().replace(", Indore", "").trim() : "Block B - SVVV Main Campus");
        htDto.setReportingTime(ht.getReportingTime());
        htDto.setAttendancePercentage(ht.getAttendancePercentage());
        htDto.setIsFeeCleared(ht.getIsFeeCleared());
        htDto.setHallTicketNumber(ht.getHallTicketNumber());

        boolean isAttendanceOk = ht.getAttendancePercentage() != null && ht.getAttendancePercentage() >= 75.0;
        htDto.setIsAttendanceEligible(isAttendanceOk);

        boolean isHallTicketActive = Boolean.TRUE.equals(ht.getIsFeeCleared()) && isAttendanceOk;
        htDto.setIsHallTicketEnabled(isHallTicketActive);

        if (isHallTicketActive) {
            htDto.setStatusBadgeText("Fee Dues Cleared");
            htDto.setStatusBadgeClass("green");
        } else if (!Boolean.TRUE.equals(ht.getIsFeeCleared())) {
            htDto.setStatusBadgeText("Blocked due to Pending Fee Dues");
            htDto.setStatusBadgeClass("red");
        } else {
            htDto.setStatusBadgeText("Attendance Low (<75%)");
            htDto.setStatusBadgeClass("red");
        }

        dto.setHallTicket(htDto);

        // Semester Results DTO list
        dto.setSemesterResults(results.stream().map(this::mapToResultDto).toList());

        return dto;
    }

    private SemesterResultDto mapToResultDto(SemesterResult r) {
        SemesterResultDto dto = new SemesterResultDto();
        dto.setId(r.getId());
        dto.setSemesterName(r.getSemesterName());
        dto.setSemesterNumber(r.getSemesterNumber());
        dto.setSemesterRoman(toRoman(r.getSemesterNumber()));
        dto.setSessionPeriod(r.getSessionPeriod());
        dto.setEnrolledCredits(r.getEnrolledCredits());
        dto.setBacklogsInSem(r.getBacklogsInSem());
        dto.setSgpa(r.getSgpa());
        dto.setStatus(r.getStatus());
        dto.setGradeCardPdfPath(r.getGradeCardPdfPath());
        return dto;
    }

    private String toRoman(int num) {
        String[] romans = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII"};
        if (num >= 1 && num < romans.length) return romans[num];
        return String.valueOf(num);
    }
}
