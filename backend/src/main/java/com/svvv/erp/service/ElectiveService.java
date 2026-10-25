package com.svvv.erp.service;

import com.svvv.erp.dto.ElectiveCourseDto;
import com.svvv.erp.dto.ElectiveOverviewResponse;
import com.svvv.erp.entity.ElectiveCourse;
import com.svvv.erp.entity.ElectiveEnrollment;
import com.svvv.erp.entity.Notification;
import com.svvv.erp.entity.Student;
import com.svvv.erp.repository.ElectiveCourseRepository;
import com.svvv.erp.repository.ElectiveEnrollmentRepository;
import com.svvv.erp.repository.NotificationRepository;
import com.svvv.erp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ElectiveService {

    @Autowired
    private ElectiveCourseRepository electiveCourseRepository;

    @Autowired
    private ElectiveEnrollmentRepository electiveEnrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");

    private static final List<String> ACTIVE_COURSE_CODES = Arrays.asList("CS-701", "CS-704", "CS-705", "CS-708");

    public List<ElectiveCourseDto> getAllCoursesWithSeats() {
        ensureFourCoursesSeeded();
        List<ElectiveCourse> all = electiveCourseRepository.findAll();
        List<ElectiveCourseDto> result = new ArrayList<>();

        for (String code : ACTIVE_COURSE_CODES) {
            ElectiveCourse c = all.stream()
                    .filter(x -> x.getCourseCode().equalsIgnoreCase(code))
                    .findFirst()
                    .orElse(null);
            if (c != null) {
                int enrolledCount = (int) electiveEnrollmentRepository.countByCourseId(c.getId());
                result.add(mapToDto(c, enrolledCount));
            }
        }
        return result;
    }

    public ElectiveOverviewResponse getOverviewForStudent(Long studentId) {
        ensureFourCoursesSeeded();

        Student student = null;
        if (studentId != null) {
            student = studentRepository.findById(studentId).orElse(null);
        }

        ElectiveOverviewResponse response = new ElectiveOverviewResponse();
        response.setWindowCloses("Active");

        // 1. Check if student is already enrolled in any elective course
        List<ElectiveEnrollment> existingEnrollments = (student != null)
                ? electiveEnrollmentRepository.findByStudentId(studentId)
                : Collections.emptyList();

        if (!existingEnrollments.isEmpty()) {
            // Student has already selected a course -> Single-Elective Lock Active
            ElectiveEnrollment enrollment = existingEnrollments.get(0);
            ElectiveCourse course = enrollment.getCourse();

            response.setHasSelectedElective(true);
            response.setEnrolledAt(enrollment.getEnrolledAt() != null
                    ? enrollment.getEnrolledAt().format(DATE_FORMATTER)
                    : "Confirmed");

            int enrolledCount = (int) electiveEnrollmentRepository.countByCourseId(course.getId());
            ElectiveCourseDto enrolledDto = mapToDto(course, enrolledCount);
            response.setEnrolledCourse(enrolledDto);

            // Hide the rest of the list: only the single enrolled course is rendered
            response.setAvailableCourses(Collections.singletonList(enrolledDto));
            response.setTotalOptions(1);
            response.setAggregatedCapacity(30);
            response.setOverallVacancy(enrolledDto.getRemainingSeats());
            return response;
        }

        // 2. Student has NOT selected yet -> Provide full 4 available courses
        response.setHasSelectedElective(false);
        response.setEnrolledCourse(null);

        List<ElectiveCourseDto> dtoList = getAllCoursesWithSeats();
        int totalEnrolled = dtoList.stream().mapToInt(ElectiveCourseDto::getEnrolledSeats).sum();
        int totalCapacity = dtoList.stream().mapToInt(ElectiveCourseDto::getTotalSeats).sum();

        response.setAvailableCourses(dtoList);
        response.setTotalOptions(dtoList.size());
        response.setAggregatedCapacity(totalCapacity);
        response.setOverallVacancy(Math.max(0, totalCapacity - totalEnrolled));

        return response;
    }

    @Transactional
    public ElectiveOverviewResponse enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        ElectiveCourse course = electiveCourseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        // Enforce SINGLE-ELECTIVE Lock
        List<ElectiveEnrollment> existing = electiveEnrollmentRepository.findByStudentId(studentId);
        if (!existing.isEmpty()) {
            throw new IllegalStateException("You have already selected an elective course. Your choice is permanently locked.");
        }

        // Real-time dynamic seat check (Capacity: 30)
        long currentEnrolled = electiveEnrollmentRepository.countByCourseId(courseId);
        int totalCapacity = course.getTotalSeats() != null ? course.getTotalSeats() : 30;
        if (currentEnrolled >= totalCapacity) {
            throw new IllegalStateException("Seats Full: This course has reached its maximum capacity of " + totalCapacity + " seats.");
        }

        // Save Enrollment
        ElectiveEnrollment enrollment = new ElectiveEnrollment(student, course);
        electiveEnrollmentRepository.save(enrollment);

        // Get updated enrolled course dto
        int updatedCount = (int) (currentEnrolled + 1);
        ElectiveCourseDto dto = mapToDto(course, updatedCount);

        // Create in-app notification
        try {
            Notification notif = new Notification(
                    student.getUser(),
                    "Elective Enrolled: " + course.getCourseName(),
                    "Your registration for " + course.getCourseCode() + " (" + course.getCourseName() + ") is confirmed at " + dto.getVenue() + ".",
                    "ELECTIVE"
            );
            notificationRepository.save(notif);
        } catch (Exception ignored) {}

        // Return updated overview response with single enrolled course
        ElectiveOverviewResponse response = new ElectiveOverviewResponse();
        response.setHasSelectedElective(true);
        response.setEnrolledCourse(dto);
        response.setEnrolledAt(enrollment.getEnrolledAt() != null ? enrollment.getEnrolledAt().format(DATE_FORMATTER) : "Confirmed");
        response.setAvailableCourses(Collections.singletonList(dto));
        response.setTotalOptions(1);
        response.setAggregatedCapacity(totalCapacity);
        response.setOverallVacancy(dto.getRemainingSeats());

        return response;
    }

    public Map<String, Object> getElectivesAdminStats() {
        ensureFourCoursesSeeded();
        List<ElectiveCourseDto> courses = getAllCoursesWithSeats();
        long totalEnrolled = electiveEnrollmentRepository.count();
        long totalCapacity = 120; // 4 courses * 30 seats each
        long totalVacant = Math.max(0, totalCapacity - totalEnrolled);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCourses", 4);
        stats.put("totalCapacity", totalCapacity);
        stats.put("totalEnrolled", totalEnrolled);
        stats.put("totalVacant", totalVacant);
        stats.put("enrollmentRatio", totalEnrolled + "/" + totalCapacity);
        stats.put("summaryText", "Total Enrolled: " + totalEnrolled + "/" + totalCapacity);
        stats.put("courses", courses);
        return stats;
    }

    public List<Map<String, Object>> getAdminElectivesDetail() {
        ensureFourCoursesSeeded();
        List<ElectiveCourse> all = electiveCourseRepository.findAll();
        List<Map<String, Object>> list = new ArrayList<>();

        for (String code : ACTIVE_COURSE_CODES) {
            ElectiveCourse c = all.stream()
                    .filter(x -> x.getCourseCode().equalsIgnoreCase(code))
                    .findFirst()
                    .orElse(null);
            if (c != null) {
                // filled_seats = COUNT(elective_enrollments WHERE course_id = course.id)
                long filledSeats = electiveEnrollmentRepository.countByCourseId(c.getId());
                int totalSeats = 30; // standard capacity 30
                // remaining_seats = 30 - filled_seats
                long remainingSeats = Math.max(0, totalSeats - filledSeats);
                // capacity_percentage = (filled_seats / 30) * 100
                double capacityPercentage = Math.round(((double) filledSeats / (double) totalSeats) * 1000.0) / 10.0;

                String instructor = "Faculty Lead";
                String venue = "Campus Hall";
                if (code.contains("701")) {
                    instructor = "Dr. R. Verma";
                    venue = "Main Block — Seminar Hall #104 (Ground Floor)";
                } else if (code.contains("704")) {
                    instructor = "Prof. N. Patel";
                    venue = "SRK Block — Room #310 (3rd Floor)";
                } else if (code.contains("705")) {
                    instructor = "Dr. S. Mukherjee";
                    venue = "Architecture Block — Cyber Forensics Lab #305";
                } else if (code.contains("708")) {
                    instructor = "Prof. K. Mehta";
                    venue = "Mechanical Block — Computer Center Lab #202";
                }

                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", c.getId());
                map.put("courseId", c.getId());
                map.put("courseCode", c.getCourseCode());
                map.put("courseName", c.getCourseName());
                map.put("department", "Computer Science & Engineering");
                map.put("instructor", instructor);
                map.put("facultyName", instructor);
                map.put("venue", venue);
                map.put("totalSeats", totalSeats);
                map.put("capacity", totalSeats);
                map.put("filled_seats", filledSeats);
                map.put("filledSeats", filledSeats);
                map.put("enrolled", filledSeats);
                map.put("enrolledCount", filledSeats);
                map.put("enrolledSeats", filledSeats);
                map.put("remaining_seats", remainingSeats);
                map.put("remainingSeats", remainingSeats);
                map.put("vacant", remainingSeats);
                map.put("vacant_seats", remainingSeats);
                map.put("capacity_percentage", capacityPercentage);
                map.put("capacityPercentage", capacityPercentage);
                map.put("percentage", (int) Math.round(capacityPercentage));

                list.add(map);
            }
        }
        return list;
    }

    private ElectiveCourseDto mapToDto(ElectiveCourse c, int enrolledCount) {
        ElectiveCourseDto dto = new ElectiveCourseDto();
        dto.setId(c.getId());
        dto.setCourseCode(c.getCourseCode());
        dto.setCourseName(c.getCourseName());
        int total = (c.getTotalSeats() != null && c.getTotalSeats() == 30) ? 30 : 30;
        dto.setTotalSeats(total);
        dto.setEnrolledSeats(enrolledCount);
        int remaining = Math.max(0, total - enrolledCount);
        dto.setRemainingSeats(remaining);
        dto.setFull(remaining <= 0);
        dto.setStatus(remaining <= 0 ? "CLOSED" : "OPEN");

        // Rich SVVV Campus Specific Metadata & Venues
        String code = c.getCourseCode().toUpperCase();
        if (code.contains("704")) {
            dto.setCredits(3);
            dto.setVenue("SRK Block — Room #310 (3rd Floor)");
            dto.setFacultyName("Prof. N. Patel");
            dto.setSchedule("Mon, Wed, Fri • 11:30 AM - 12:30 PM");
        } else if (code.contains("701")) {
            dto.setCredits(4);
            dto.setVenue("Main Block — Seminar Hall #104 (Ground Floor)");
            dto.setFacultyName("Dr. R. Verma");
            dto.setSchedule("Tue, Thu, Sat • 10:00 AM - 11:00 AM");
        } else if (code.contains("705")) {
            dto.setCredits(3);
            dto.setVenue("Architecture Block — Cyber Forensics Lab #305");
            dto.setFacultyName("Dr. S. Mukherjee");
            dto.setSchedule("Mon, Wed, Fri • 02:00 PM - 03:00 PM");
        } else if (code.contains("708")) {
            dto.setCredits(3);
            dto.setVenue("Mechanical Block — Computer Center Lab #202");
            dto.setFacultyName("Prof. K. Mehta");
            dto.setSchedule("Tue, Thu • 02:00 PM - 03:30 PM");
        } else {
            dto.setCredits(3);
            dto.setVenue("Main Block — Room #204");
            dto.setFacultyName("Prof. M. Kulkarni");
            dto.setSchedule("Mon, Wed • 10:00 AM - 11:30 AM");
        }

        return dto;
    }

    public void ensureFourCoursesSeeded() {
        createOrUpdateCourse("CS-701", "Deep Learning & Neural Networks", 30);
        createOrUpdateCourse("CS-704", "Cloud Architecture & Services", 30);
        createOrUpdateCourse("CS-705", "Cyber Security & Digital Forensics", 30);
        createOrUpdateCourse("CS-708", "Full Stack Web Engineering", 30);
    }

    private void createOrUpdateCourse(String code, String name, int seats) {
        Optional<ElectiveCourse> opt = electiveCourseRepository.findByCourseCode(code);
        if (opt.isPresent()) {
            ElectiveCourse c = opt.get();
            boolean changed = false;
            if (c.getTotalSeats() == null || c.getTotalSeats() != seats) {
                c.setTotalSeats(seats);
                changed = true;
            }
            if (c.getCourseName() == null || !c.getCourseName().equals(name)) {
                c.setCourseName(name);
                changed = true;
            }
            if (changed) {
                electiveCourseRepository.save(c);
            }
        } else {
            ElectiveCourse c = new ElectiveCourse(code, name, seats, "OPEN");
            electiveCourseRepository.save(c);
        }
    }

    @Transactional
    public boolean deleteEnrollmentByStudentId(Long studentId) {
        List<ElectiveEnrollment> enrollments = electiveEnrollmentRepository.findByStudentId(studentId);
        if (!enrollments.isEmpty()) {
            electiveEnrollmentRepository.deleteAll(enrollments);
            return true;
        }
        return false;
    }
}
