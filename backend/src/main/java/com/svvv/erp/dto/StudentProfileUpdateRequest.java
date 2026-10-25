package com.svvv.erp.dto;

public class StudentProfileUpdateRequest {

    private String phone;
    private String email;
    private Double familyIncome;
    private String address;

    public StudentProfileUpdateRequest() {}

    public StudentProfileUpdateRequest(String phone, String email, Double familyIncome, String address) {
        this.phone = phone;
        this.email = email;
        this.familyIncome = familyIncome;
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getFamilyIncome() {
        return familyIncome;
    }

    public void setFamilyIncome(Double familyIncome) {
        this.familyIncome = familyIncome;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
