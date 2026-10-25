package com.svvv.erp.dto;

import java.util.List;

public class ElectiveOverviewResponse {
    private boolean hasSelectedElective;
    private ElectiveCourseDto enrolledCourse;
    private String enrolledAt;
    private List<ElectiveCourseDto> availableCourses;
    private int totalOptions;
    private int aggregatedCapacity;
    private int overallVacancy;
    private String windowCloses;

    public ElectiveOverviewResponse() {}

    public boolean isHasSelectedElective() {
        return hasSelectedElective;
    }

    public void setHasSelectedElective(boolean hasSelectedElective) {
        this.hasSelectedElective = hasSelectedElective;
    }

    public ElectiveCourseDto getEnrolledCourse() {
        return enrolledCourse;
    }

    public void setEnrolledCourse(ElectiveCourseDto enrolledCourse) {
        this.enrolledCourse = enrolledCourse;
    }

    public String getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(String enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public List<ElectiveCourseDto> getAvailableCourses() {
        return availableCourses;
    }

    public void setAvailableCourses(List<ElectiveCourseDto> availableCourses) {
        this.availableCourses = availableCourses;
    }

    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
    }

    public int getAggregatedCapacity() {
        return aggregatedCapacity;
    }

    public void setAggregatedCapacity(int aggregatedCapacity) {
        this.aggregatedCapacity = aggregatedCapacity;
    }

    public int getOverallVacancy() {
        return overallVacancy;
    }

    public void setOverallVacancy(int overallVacancy) {
        this.overallVacancy = overallVacancy;
    }

    public String getWindowCloses() {
        return windowCloses;
    }

    public void setWindowCloses(String windowCloses) {
        this.windowCloses = windowCloses;
    }
}
