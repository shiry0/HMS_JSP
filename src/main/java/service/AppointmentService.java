package service;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;

import dao.AppointmentDAO;
import dao.BillDAO;
import dao.DoctorDAO;
import dao.PatientDAO;
import model.Appointment;
import model.Bill;
import model.Doctor;
import model.Patient;
import util.DateUtil;
import util.ValidationUtil;

public class AppointmentService {
    private static final double MISSED_APPOINTMENT_LATE_FEE = 200.00;
    private static final double PATIENT_CANCELLATION_RATE = 0.50;

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final BillDAO billDAO = new BillDAO();

    public void bookAppointment(int patientId, int doctorId, String dateStr, String timeStr, String reason) throws Exception {
        if (!ValidationUtil.isNotEmpty(reason)) {
            throw new Exception("Appointment reason is required.");
        }

        Date date = DateUtil.toSqlDate(dateStr);
        if (!DateUtil.isTodayOrFuture(date)) {
            throw new Exception("Appointment date must be today or in the future.");
        }

        Doctor doctor = doctorDAO.getDoctorById(doctorId);
        if (doctor == null || !doctor.isActive()) {
            throw new Exception("Doctor is not available for booking.");
        }
        if (!isDoctorAvailableOnDay(doctor.getAvailableDays(), date.toLocalDate().getDayOfWeek())) {
            throw new Exception("Doctor is only available on: " + doctor.getAvailableDays() + ".");
        }

        LocalTime selectedTime = LocalTime.parse(timeStr);
        if (selectedTime.isBefore(LocalTime.of(8, 0)) || selectedTime.isAfter(LocalTime.of(17, 0))) {
            throw new Exception("Appointment time must be between 08:00 and 17:00.");
        }
        if (date.toLocalDate().isEqual(LocalDate.now()) && selectedTime.isBefore(LocalTime.now())) {
            throw new Exception("Appointment time must be in the future.");
        }

        Time time = Time.valueOf(selectedTime);
        if (!appointmentDAO.isDoctorAvailable(doctorId, date, time)) {
            throw new Exception("Doctor is not available at that time.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatientId(patientId);
        appointment.setDoctorId(doctorId);
        appointment.setApptDate(date);
        appointment.setApptTime(time);
        appointment.setStatus("Pending");
        appointment.setReason(reason.trim());
        appointment.setNotes("");
        appointmentDAO.insertAppointment(appointment);
    }

    private boolean isDoctorAvailableOnDay(String availableDays, DayOfWeek requestedDay) {
        if (availableDays == null || availableDays.isBlank()) {
            return true;
        }

        String normalized = availableDays.trim().toLowerCase();
        if (normalized.contains("daily") || normalized.contains("everyday") || normalized.contains("all days")) {
            return true;
        }

        EnumSet<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
        String[] parts = normalized.replace(" to ", "-").split("[,/]+");
        for (String part : parts) {
            addAvailableDays(days, part.trim());
        }
        return days.isEmpty() || days.contains(requestedDay);
    }

    private void addAvailableDays(EnumSet<DayOfWeek> days, String value) {
        if (value.isBlank()) {
            return;
        }
        if (value.contains("weekday")) {
            days.addAll(EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY));
            return;
        }
        if (value.contains("weekend")) {
            days.add(DayOfWeek.SATURDAY);
            days.add(DayOfWeek.SUNDAY);
            return;
        }
        if (value.contains("-")) {
            String[] range = value.split("-", 2);
            DayOfWeek start = parseDay(range[0]);
            DayOfWeek end = parseDay(range[1]);
            if (start != null && end != null) {
                DayOfWeek current = start;
                while (true) {
                    days.add(current);
                    if (current == end) {
                        break;
                    }
                    current = current.plus(1);
                }
            }
            return;
        }
        DayOfWeek day = parseDay(value);
        if (day != null) {
            days.add(day);
        }
    }

    private DayOfWeek parseDay(String value) {
        String token = value.trim().toLowerCase();
        if (token.startsWith("mon")) {
            return DayOfWeek.MONDAY;
        }
        if (token.startsWith("tue")) {
            return DayOfWeek.TUESDAY;
        }
        if (token.startsWith("wed")) {
            return DayOfWeek.WEDNESDAY;
        }
        if (token.startsWith("thu")) {
            return DayOfWeek.THURSDAY;
        }
        if (token.startsWith("fri")) {
            return DayOfWeek.FRIDAY;
        }
        if (token.startsWith("sat")) {
            return DayOfWeek.SATURDAY;
        }
        if (token.startsWith("sun")) {
            return DayOfWeek.SUNDAY;
        }
        return null;
    }

    public void cancelAppointment(int apptId, int requestingUserId, String role) throws Exception {
        cancelAppointment(apptId, requestingUserId, role, "");
    }

    public void cancelAppointment(int apptId, int requestingUserId, String role, String cancelReason) throws Exception {
        Appointment appointment = appointmentDAO.getAppointmentById(apptId);
        if (appointment == null) {
            throw new Exception("Appointment not found.");
        }
        if ("Completed".equalsIgnoreCase(appointment.getStatus())
                || "Cancelled".equalsIgnoreCase(appointment.getStatus())) {
            throw new Exception("This appointment can no longer be cancelled.");
        }

        if ("patient".equalsIgnoreCase(role)) {
            Patient patient = patientDAO.getPatientByUserId(requestingUserId);
            if (patient == null || !appointmentDAO.belongsToPatient(apptId, patient.getPatientId())) {
                throw new Exception("You are not allowed to cancel this appointment.");
            }
        } else if ("doctor".equalsIgnoreCase(role)) {
            Doctor doctor = doctorDAO.getDoctorByUserId(requestingUserId);
            if (doctor == null || !appointmentDAO.belongsToDoctor(apptId, doctor.getDoctorId())) {
                throw new Exception("You are not allowed to cancel this appointment.");
            }
            if (!ValidationUtil.isNotEmpty(cancelReason)) {
                throw new Exception("Cancellation reason is required.");
            }
        }

        if ("doctor".equalsIgnoreCase(role)) {
            appointmentDAO.cancelAppointment(apptId, cancelReason.trim());
        } else {
            appointmentDAO.cancelAppointment(apptId);
        }

        if ("patient".equalsIgnoreCase(role) && billDAO.getBillByAppointmentId(apptId) == null) {
            Doctor doctor = doctorDAO.getDoctorById(appointment.getDoctorId());
            double cancellationFee = doctor == null ? 0.0 : doctor.getConsultationFee() * PATIENT_CANCELLATION_RATE;

            Bill bill = new Bill();
            bill.setPatientId(appointment.getPatientId());
            bill.setApptId(apptId);
            bill.setConsultationFee(cancellationFee);
            bill.setMedicineFee(0.0);
            bill.setTestFee(0.0);
            bill.setLateFee(0.0);
            bill.setTotalAmount(cancellationFee);
            bill.setPaidAmount(0.0);
            bill.setStatus(cancellationFee > 0 ? "Unpaid" : "Paid");
            billDAO.insertBill(bill);
        }
    }

    public void confirmAppointment(int apptId, int doctorUserId) throws Exception {
        Doctor doctor = doctorDAO.getDoctorByUserId(doctorUserId);
        if (doctor == null || !appointmentDAO.belongsToDoctor(apptId, doctor.getDoctorId())) {
            throw new Exception("You are not allowed to update this appointment.");
        }

        Appointment appointment = appointmentDAO.getAppointmentById(apptId);
        if (appointment == null) {
            throw new Exception("Appointment not found.");
        }
        if ("Cancelled".equalsIgnoreCase(appointment.getStatus())) {
            throw new Exception("Cancelled appointments cannot be confirmed.");
        }
        if ("Completed".equalsIgnoreCase(appointment.getStatus())) {
            throw new Exception("Completed appointments cannot be confirmed again.");
        }
        if ("Confirmed".equalsIgnoreCase(appointment.getStatus())) {
            throw new Exception("This appointment is already confirmed.");
        }

        appointmentDAO.updateStatus(apptId, "Confirmed");
    }

    public void completeAppointment(int apptId, int doctorUserId) throws Exception {
        Doctor doctor = doctorDAO.getDoctorByUserId(doctorUserId);
        if (doctor == null || !appointmentDAO.belongsToDoctor(apptId, doctor.getDoctorId())) {
            throw new Exception("You are not allowed to complete this appointment.");
        }

        Appointment appointment = appointmentDAO.getAppointmentById(apptId);
        if (appointment == null) {
            throw new Exception("Appointment not found.");
        }
        if ("Cancelled".equalsIgnoreCase(appointment.getStatus())) {
            throw new Exception("Cancelled appointments cannot be completed.");
        }
        if ("Completed".equalsIgnoreCase(appointment.getStatus())) {
            throw new Exception("This appointment is already completed.");
        }

        appointmentDAO.updateStatus(apptId, "Completed");

        if (billDAO.getBillByAppointmentId(apptId) == null) {
            Bill bill = new Bill();
            bill.setPatientId(appointment.getPatientId());
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

    public void processMissedAppointments() throws Exception {
        List<Appointment> missedAppointments = appointmentDAO.getMissedAppointments();
        for (Appointment appointment : missedAppointments) {
            appointmentDAO.updateStatus(appointment.getApptId(), "Cancelled");
            if (billDAO.getBillByAppointmentId(appointment.getApptId()) == null) {
                Bill bill = new Bill();
                bill.setPatientId(appointment.getPatientId());
                bill.setApptId(appointment.getApptId());
                bill.setConsultationFee(0.0);
                bill.setMedicineFee(0.0);
                bill.setTestFee(0.0);
                bill.setLateFee(MISSED_APPOINTMENT_LATE_FEE);
                bill.setTotalAmount(MISSED_APPOINTMENT_LATE_FEE);
                bill.setPaidAmount(0.0);
                bill.setStatus("Unpaid");
                billDAO.insertBill(bill);
            }
        }
    }
}
