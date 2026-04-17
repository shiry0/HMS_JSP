package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Appointment {
    private int apptId;
    private int patientId;
    private int doctorId;
    private Date apptDate;
    private Time apptTime;
    private String status;
    private String reason;
    private String notes;
    private Timestamp createdAt;
    private String patientName;
    private String doctorName;
    private String deptName;

    public Appointment() {
    }

    public Appointment(int apptId, int patientId, int doctorId, Date apptDate, Time apptTime, String status,
            String reason, String notes, Timestamp createdAt, String patientName, String doctorName, String deptName) {
        this.apptId = apptId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.apptDate = apptDate;
        this.apptTime = apptTime;
        this.status = status;
        this.reason = reason;
        this.notes = notes;
        this.createdAt = createdAt;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.deptName = deptName;
    }

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public Date getApptDate() {
        return apptDate;
    }

    public void setApptDate(Date apptDate) {
        this.apptDate = apptDate;
    }

    public Time getApptTime() {
        return apptTime;
    }

    public void setApptTime(Time apptTime) {
        this.apptTime = apptTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
