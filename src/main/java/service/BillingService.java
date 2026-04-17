package service;

import dao.AppointmentDAO;
import dao.BillDAO;
import dao.DoctorDAO;
import model.Appointment;
import model.Bill;
import model.Doctor;
import util.ValidationUtil;

public class BillingService {
    private final BillDAO billDAO = new BillDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

    public void makePayment(int billId, int patientId, double amount) throws Exception {
        if (!ValidationUtil.isPositiveAmount(amount)) {
            throw new Exception("Payment amount must be a positive number.");
        }

        Bill bill = billDAO.getBillById(billId);
        if (bill == null) {
            throw new Exception("Bill not found.");
        }
        if (bill.getPatientId() != patientId) {
            throw new Exception("You are not allowed to pay this bill.");
        }

        double remaining = bill.getTotalAmount() - bill.getPaidAmount();
        if (remaining <= 0) {
            throw new Exception("This bill has already been fully paid.");
        }
        if (amount > remaining) {
            throw new Exception("Amount cannot exceed the remaining balance.");
        }

        double newPaid = bill.getPaidAmount() + amount;
        String status = newPaid >= bill.getTotalAmount() ? "Paid" : "Partial";
        billDAO.updatePayment(billId, newPaid, status);
    }

    public Bill generateBill(int apptId, double medicineFee, double testFee) throws Exception {
        Appointment appointment = appointmentDAO.getAppointmentById(apptId);
        if (appointment == null) {
            throw new Exception("Appointment not found.");
        }

        Bill existing = billDAO.getBillByAppointmentId(apptId);
        if (existing != null) {
            return existing;
        }

        Doctor doctor = doctorDAO.getDoctorById(appointment.getDoctorId());
        if (doctor == null) {
            throw new Exception("Doctor not found.");
        }

        Bill bill = new Bill();
        bill.setPatientId(appointment.getPatientId());
        bill.setApptId(apptId);
        bill.setConsultationFee(doctor.getConsultationFee());
        bill.setMedicineFee(Math.max(medicineFee, 0));
        bill.setTestFee(Math.max(testFee, 0));
        bill.setLateFee(0.0);
        bill.setTotalAmount(bill.getConsultationFee() + bill.getMedicineFee() + bill.getTestFee());
        bill.setPaidAmount(0.0);
        bill.setStatus("Unpaid");
        billDAO.insertBill(bill);
        return bill;
    }
}
