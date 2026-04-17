package model;

import java.sql.Timestamp;

public class Doctor {
    private int doctorId;
    private int userId;
    private int deptId;
    private boolean active;
    private String specialization;
    private String qualification;
    private int experienceYrs;
    private double consultationFee;
    private String availableDays;
    private String fullName;
    private String email;
    private String phone;
    private String deptName;
    private Timestamp createdAt;

    public Doctor() {
    }

    public Doctor(int doctorId, int userId, int deptId, boolean active, String specialization, String qualification,
            int experienceYrs, double consultationFee, String availableDays, String fullName, String email,
            String phone, String deptName, Timestamp createdAt) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.deptId = deptId;
        this.active = active;
        this.specialization = specialization;
        this.qualification = qualification;
        this.experienceYrs = experienceYrs;
        this.consultationFee = consultationFee;
        this.availableDays = availableDays;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.deptName = deptName;
        this.createdAt = createdAt;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public int getExperienceYrs() {
        return experienceYrs;
    }

    public void setExperienceYrs(int experienceYrs) {
        this.experienceYrs = experienceYrs;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
