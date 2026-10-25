package com.svvv.erp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "elective_courses")
public class ElectiveCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code", nullable = false, unique = true, length = 50)
    private String courseCode;

    @Column(name = "course_name", nullable = false, length = 150)
    private String courseName;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(nullable = false, length = 20)
    private String status = "OPEN"; // "OPEN" or "CLOSED"

    public ElectiveCourse() {}

    public ElectiveCourse(String courseCode, String courseName, Integer totalSeats, String status) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.totalSeats = totalSeats;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
