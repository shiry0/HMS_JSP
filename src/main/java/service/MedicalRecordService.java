package service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import dao.AppointmentDAO;
import dao.BillDAO;
import dao.DoctorDAO;
import dao.MedicalRecordDAO;
import dao.PrescriptionDAO;
import model.Appointment;
import model.Bill;
import model.Doctor;
import model.MedicalRecord;
import model.Prescription;
import util.ValidationUtil;

public class MedicalRecordService {
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final BillDAO billDAO = new BillDAO();

    public void addRecord(int patientId, int doctorUserId, int apptId, String diagnosis, String symptoms, String treatment,
            String[] medicineNames, String[] dosages, String[] frequencies, String[] durations, String[] instructions)
            throws Exception {
        if (!ValidationUtil.isNotEmpty(diagnosis)) {
            throw new Exception("Diagnosis is required.");
        }
        if (!ValidationUtil.isNotEmpty(treatment)) {
            throw new Exception("Treatment is required.");
        }

        Doctor doctor = doctorDAO.getDoctorByUserId(doctorUserId);
        if (doctor == null) {
            throw new Exception("Doctor profile not found.");
        }

        if (apptId > 0) {
            Appointment appointment = appointmentDAO.getAppointmentById(apptId);
            if (appointment == null || appointment.getDoctorId() != doctor.getDoctorId()
                    || appointment.getPatientId() != patientId) {
                throw new Exception("Appointment does not match the selected patient.");
            }
        }

        MedicalRecord record = new MedicalRecord();
        record.setPatientId(patientId);
        record.setDoctorId(doctor.getDoctorId());
        record.setApptId(apptId);
        record.setDiagnosis(diagnosis.trim());
        record.setSymptoms(symptoms == null ? "" : symptoms.trim());
        record.setTreatment(treatment.trim());
        record.setRecordDate(new Date(System.currentTimeMillis()));

        int recordId = medicalRecordDAO.insertRecord(record);
        List<Prescription> prescriptions = buildPrescriptions(recordId, medicineNames, dosages, frequencies, durations,
                instructions);
        prescriptionDAO.insertPrescriptions(prescriptions);

        if (apptId > 0) {
            appointmentDAO.updateStatus(apptId, "Completed");
            if (billDAO.getBillByAppointmentId(apptId) == null) {
                Bill bill = new Bill();
                bill.setPatientId(patientId);
                bill.setApptId(apptId);
                bill.setConsultationFee(doctor.getConsultationFee());
                bill.setMedicineFee(0.0);
                bill.setTestFee(0.0);
                bill.setLateFee(0.0);
                bill.setTotalAmount(doctor.getConsultationFee());
                bill.setPaidAmount(0.0);
                bill.setStatus("Unpaid");
                billDAO.insertBill(bill);
            }
        }
    }

    private List<Prescription> buildPrescriptions(int recordId, String[] medicineNames, String[] dosages,
            String[] frequencies, String[] durations, String[] instructions) {
        List<Prescription> prescriptions = new ArrayList<>();
        if (medicineNames == null) {
            return prescriptions;
        }

        for (int i = 0; i < medicineNames.length; i++) {
            if (!ValidationUtil.isNotEmpty(medicineNames[i])) {
                continue;
            }
            Prescription prescription = new Prescription();
            prescription.setRecordId(recordId);
            prescription.setMedicineName(medicineNames[i].trim());
            prescription.setDosage(dosages != null && i < dosages.length ? dosages[i] : "");
            prescription.setFrequency(frequencies != null && i < frequencies.length ? frequencies[i] : "");
            prescription.setDuration(durations != null && i < durations.length ? durations[i] : "");
            prescription.setInstructions(instructions != null && i < instructions.length ? instructions[i] : "");
            prescriptions.add(prescription);
        }
        return prescriptions;
    }
}
