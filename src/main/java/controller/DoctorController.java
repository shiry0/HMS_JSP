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

@WebServlet("/doctor/*")
public class DoctorController extends BaseController {
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentService appointmentService = new AppointmentService();
    private final MedicalRecordService medicalRecordService = new MedicalRecordService();

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
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            try {
                if (path.contains("record")) {
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
        if (date != null && !date.isBlank()) {
            req.setAttribute("appointments", appointmentDAO.getAppointmentsByDoctorOnDate(doctor.getDoctorId(), Date.valueOf(date)));
        } else {
            req.setAttribute("appointments", appointmentDAO.getAppointmentsByDoctor(doctor.getDoctorId()));
        }
        forward(req, resp, "/WEB-INF/views/doctor/appointments.jsp");
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
}

