package com.svvv.erp.dto;

public class AcademicCalendarEventDto {
    private Integer day;
    private String date;
    private String title;
    private String type;
    private String color;
    private String badgeClass;

    public AcademicCalendarEventDto() {}

    public AcademicCalendarEventDto(Integer day, String date, String title, String type, String color, String badgeClass) {
        this.day = day;
        this.date = date;
        this.title = title;
        this.type = type;
        this.color = color;
        this.badgeClass = badgeClass;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public void setBadgeClass(String badgeClass) {
        this.badgeClass = badgeClass;
    }
}
