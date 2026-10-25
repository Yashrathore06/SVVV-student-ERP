package com.svvv.erp.dto;

import java.util.List;

public class AcademicCalendarDto {
    private Integer year;
    private Integer month;
    private String monthName;
    private String monthYear;
    private Integer currentDay;
    private Integer daysInMonth;
    private Integer firstDayOfWeek; // 1 = Monday, 7 = Sunday
    private List<AcademicCalendarEventDto> events;
    private List<String> activeNotices;

    public AcademicCalendarDto() {}

    public AcademicCalendarDto(Integer year, Integer month, String monthName, String monthYear, Integer currentDay, Integer daysInMonth, Integer firstDayOfWeek, List<AcademicCalendarEventDto> events, List<String> activeNotices) {
        this.year = year;
        this.month = month;
        this.monthName = monthName;
        this.monthYear = monthYear;
        this.currentDay = currentDay;
        this.daysInMonth = daysInMonth;
        this.firstDayOfWeek = firstDayOfWeek;
        this.events = events;
        this.activeNotices = activeNotices;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public Integer getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Integer currentDay) {
        this.currentDay = currentDay;
    }

    public Integer getDaysInMonth() {
        return daysInMonth;
    }

    public void setDaysInMonth(Integer daysInMonth) {
        this.daysInMonth = daysInMonth;
    }

    public Integer getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(Integer firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public List<AcademicCalendarEventDto> getEvents() {
        return events;
    }

    public void setEvents(List<AcademicCalendarEventDto> events) {
        this.events = events;
    }

    public List<String> getActiveNotices() {
        return activeNotices;
    }

    public void setActiveNotices(List<String> activeNotices) {
        this.activeNotices = activeNotices;
    }
}
