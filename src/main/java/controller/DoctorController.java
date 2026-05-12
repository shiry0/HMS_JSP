package controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.AppointmentDAO;
import dao.DoctorDAO;
import dao.MedicalRecordDAO;
import dao.PatientDAO;
import dao.PrescriptionDAO;
import model.Doctor;
import model.MedicalRecord;
import service.AppointmentService;
import service.MedicalRecordService;
import service.PatientService;

@WebServlet("/doctor/*")
public class DoctorController extends BaseController {
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentService appointmentService = new AppointmentService();
    private final MedicalRecordService medicalRecordService = new MedicalRecordService();
    private final PatientService patientService = new PatientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "/dashboard" : req.getPathInfo();
        try {
            switch (path) {
                case "/dashboard":
                    showDashboard(req, resp);
                    break;
                case "/appointments":
                    showAppointments(req, resp);
                    break;
                case "/patient-record":
                    showPatientRecord(req, resp);
                    break;
                case "/records":
                    showRecords(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        try {
            switch (path) {
                case "/update-appointment":
                    String action = req.getParameter("action");
                    if ("confirm".equalsIgnoreCase(action)) {
                        appointmentService.confirmAppointment(intParam(req, "apptId"), getLoggedUser(req).getUserId());
                    } else {
                        appointmentService.completeAppointment(intParam(req, "apptId"), getLoggedUser(req).getUserId());
                    }
                    setFlash(req, "success", "Appointment updated successfully.");
                    redirect(req, resp, "/doctor/appointments");
                    break;
                case "/cancel-appointment":
                    appointmentService.cancelAppointment(intParam(req, "apptId"), getLoggedUser(req).getUserId(), "doctor",
                            req.getParameter("cancelReason"));
                    setFlash(req, "success", "Appointment cancelled without charging the patient.");
                    redirect(req, resp, "/doctor/appointments");
                    break;
                case "/add-record":
                    medicalRecordService.addRecord(intParam(req, "patientId"), getLoggedUser(req).getUserId(),
                            intParam(req, "apptId"), req.getParameter("diagnosis"), req.getParameter("symptoms"),
                            req.getParameter("treatment"), req.getParameterValues("medicineName"),
                            req.getParameterValues("dosage"), req.getParameterValues("frequency"),
                            req.getParameterValues("duration"), req.getParameterValues("instructions"));
                    setFlash(req, "success", "Medical record saved successfully.");
                    redirect(req, resp,
                            "/doctor/patient-record?patientId=" + intParam(req, "patientId") + "&apptId=" + intParam(req, "apptId"));
                    break;
                case "/delete-record":
                    int patientId = intParam(req, "patientId");
                    medicalRecordService.deleteRecord(intParam(req, "recordId"), getLoggedUser(req).getUserId());
                    setFlash(req, "success", "Medical record deleted successfully.");
                    if (patientId > 0) {
                        redirect(req, resp, "/doctor/patient-record?patientId=" + patientId);
                    } else {
                        redirect(req, resp, "/doctor/records");
                    }
                    break;
                case "/update-patient-details":
                    int updatePatientId = intParam(req, "patientId");
                    int updateApptId = intParam(req, "apptId");
                    patientService.updatePatientByDoctor(updatePatientId, req.getParameter("fullName"),
                            req.getParameter("dob") == null || req.getParameter("dob").isBlank() ? null
                                    : Date.valueOf(req.getParameter("dob")),
                            req.getParameter("gender"), req.getParameter("bloodGroup"), req.getParameter("address"),
                            req.getParameter("emergencyContact"));
                    setFlash(req, "success", "Patient details updated successfully.");
                    redirect(req, resp, "/doctor/patient-record?patientId=" + updatePatientId + "&apptId=" + updateApptId);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            try {
                if (path.contains("record") || path.contains("patient-details")) {
                    showPatientRecord(req, resp);
                } else {
                    showAppointments(req, resp);
                }
            } catch (Exception inner) {
                throw new ServletException(inner);
            }
        }
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Doctor doctor = doctorDAO.getDoctorByUserId(getLoggedUser(req).getUserId());
        req.setAttribute("pageTitle", "Doctor Dashboard");
        req.setAttribute("doctor", doctor);
        req.setAttribute("appointments", appointmentDAO.getTodayAppointmentsForDoctor(doctor.getDoctorId()));
        forward(req, resp, "/WEB-INF/views/doctor/dashboard.jsp");
    }

    private void showAppointments(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Doctor doctor = doctorDAO.getDoctorByUserId(getLoggedUser(req).getUserId());
        req.setAttribute("pageTitle", "My Appointments");
        String date = req.getParameter("date");
        String keyword = req.getParameter("q");
        String status = req.getParameter("status");
        List<model.Appointment> appointments;
        if (date != null && !date.isBlank()) {
            appointments = appointmentDAO.getAppointmentsByDoctorOnDate(doctor.getDoctorId(), Date.valueOf(date));
        } else {
            appointments = appointmentDAO.getAppointmentsByDoctor(doctor.getDoctorId());
        }
        req.setAttribute("appointments", filterAppointments(appointments, keyword, status));
        req.setAttribute("searchKeyword", keyword);
        req.setAttribute("selectedStatus", status);
        forward(req, resp, "/WEB-INF/views/doctor/appointments.jsp");
    }

    private void showRecords(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Doctor doctor = doctorDAO.getDoctorByUserId(getLoggedUser(req).getUserId());
        String keyword = req.getParameter("q");
        List<MedicalRecord> records = medicalRecordDAO.getRecordsByDoctor(doctor.getDoctorId(), keyword);
        for (MedicalRecord record : records) {
            record.setPrescriptions(prescriptionDAO.getByRecordId(record.getRecordId()));
        }
        req.setAttribute("pageTitle", "Patient Records");
        req.setAttribute("records", records);
        req.setAttribute("searchKeyword", keyword);
        forward(req, resp, "/WEB-INF/views/doctor/records.jsp");
    }

    private void showPatientRecord(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int patientId = intParam(req, "patientId");
        req.setAttribute("pageTitle", "Patient Record");
        req.setAttribute("patient", patientDAO.getPatientById(patientId));
        List<MedicalRecord> records = medicalRecordDAO.getRecordsByPatient(patientId);
        for (MedicalRecord record : records) {
            record.setPrescriptions(prescriptionDAO.getByRecordId(record.getRecordId()));
        }
        req.setAttribute("records", records);
        req.setAttribute("apptId", intParam(req, "apptId"));
        forward(req, resp, "/WEB-INF/views/doctor/patient-record.jsp");
    }

    private List<model.Appointment> filterAppointments(List<model.Appointment> appointments, String keyword, String status) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim().toLowerCase();
        String normalizedStatus = status == null ? "" : status.trim();
        return appointments.stream()
                .filter(appointment -> normalizedStatus.isBlank()
                        || normalizedStatus.equalsIgnoreCase(appointment.getStatus()))
                .filter(appointment -> normalizedKeyword.isBlank()
                        || containsIgnoreCase(appointment.getPatientName(), normalizedKeyword)
                        || containsIgnoreCase(appointment.getReason(), normalizedKeyword)
                        || containsIgnoreCase(appointment.getStatus(), normalizedKeyword))
                .toList();
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }
}

