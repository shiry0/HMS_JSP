package model;

import java.sql.Timestamp;

public class Bill {
    private int billId;
    private int patientId;
    private int apptId;
    private double consultationFee;
    private double medicineFee;
    private double testFee;
    private double lateFee;
    private double totalAmount;
    private double paidAmount;
    private String status;
    private Timestamp billDate;
    private String patientName;

    public Bill() {
    }

    public Bill(int billId, int patientId, int apptId, double consultationFee, double medicineFee, double testFee,
            double lateFee, double totalAmount, double paidAmount, String status, Timestamp billDate,
            String patientName) {
        this.billId = billId;
        this.patientId = patientId;
        this.apptId = apptId;
        this.consultationFee = consultationFee;
        this.medicineFee = medicineFee;
        this.testFee = testFee;
        this.lateFee = lateFee;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.status = status;
        this.billDate = billDate;
        this.patientName = patientName;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public double getMedicineFee() {
        return medicineFee;
    }

    public void setMedicineFee(double medicineFee) {
        this.medicineFee = medicineFee;
    }

    public double getTestFee() {
        return testFee;
    }

    public void setTestFee(double testFee) {
        this.testFee = testFee;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getBillDate() {
        return billDate;
    }

    public void setBillDate(Timestamp billDate) {
        this.billDate = billDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
