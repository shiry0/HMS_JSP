package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private int recordId;
    private int patientId;
    private int doctorId;
    private int apptId;
    private String diagnosis;
    private String symptoms;
    private String treatment;
    private Date recordDate;
    private String doctorName;
    private String patientName;
    private List<Prescription> prescriptions = new ArrayList<>();

    public MedicalRecord() {
    }

    public MedicalRecord(int recordId, int patientId, int doctorId, int apptId, String diagnosis, String symptoms,
            String treatment, Date recordDate, String doctorName, String patientName, List<Prescription> prescriptions) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.apptId = apptId;
        this.diagnosis = diagnosis;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.recordDate = recordDate;
        this.doctorName = doctorName;
        this.patientName = patientName;
        if (prescriptions != null) {
            this.prescriptions = prescriptions;
        }
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
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

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
