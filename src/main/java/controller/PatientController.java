package controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.AppointmentDAO;
import dao.BillDAO;
import dao.DepartmentDAO;
import dao.DoctorDAO;
import dao.MedicalRecordDAO;
import dao.PatientDAO;
import dao.PrescriptionDAO;
import model.Appointment;
import model.Doctor;
import model.MedicalRecord;
import model.Patient;
import service.AppointmentService;
import service.BillingService;
import service.PatientService;

@WebServlet("/patient/*")
public class PatientController extends BaseController {
    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final BillDAO billDAO = new BillDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final AppointmentService appointmentService = new AppointmentService();
    private final BillingService billingService = new BillingService();
    private final PatientService patientService = new PatientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "/dashboard" : req.getPathInfo();
        try {
            switch (path) {
                case "/dashboard":
                    showDashboard(req, resp);
                    break;
                case "/book-appointment":
                    showBookAppointment(req, resp);
                    break;
                case "/my-appointments":
                    showAppointments(req, resp);
                    break;
                case "/medical-records":
                    showMedicalRecords(req, resp);
                    break;
                case "/billing":
                    showBilling(req, resp);
                    break;
                case "/profile":
                    showProfile(req, resp);
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
            Patient patient = patientDAO.getPatientByUserId(getLoggedUser(req).getUserId());
            switch (path) {
                case "/book-appointment":
                    appointmentService.bookAppointment(patient.getPatientId(), intParam(req, "doctorId"),
                            req.getParameter("apptDate"), req.getParameter("apptTime"), req.getParameter("reason"));
                    setFlash(req, "success", "Appointment booked successfully.");
                    redirect(req, resp, "/patient/my-appointments");
                    break;
                case "/cancel":
                    appointmentService.cancelAppointment(intParam(req, "apptId"), getLoggedUser(req).getUserId(), "patient");
                    setFlash(req, "success", "Appointment cancelled. A 50% consultation fee has been added to billing.");
                    redirect(req, resp, "/patient/my-appointments");
                    break;
                case "/pay":
                    billingService.makePayment(intParam(req, "billId"), patient.getPatientId(), doubleParam(req, "amount"));
                    setFlash(req, "success", "Payment recorded successfully.");
                    redirect(req, resp, "/patient/billing");
                    break;
                case "/update-profile":
                    patientService.updateProfile(getLoggedUser(req).getUserId(), req.getParameter("fullName"),
                            req.getParameter("email"), req.getParameter("phone"),
                            req.getParameter("dob") == null || req.getParameter("dob").isBlank() ? null
                                    : Date.valueOf(req.getParameter("dob")),
                            req.getParameter("gender"), req.getParameter("bloodGroup"), req.getParameter("address"),
                            req.getParameter("emergencyContact"), req.getParameter("newPassword"));
                    setFlash(req, "success", "Profile updated successfully.");
                    redirect(req, resp, "/patient/profile");
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            try {
                if ("/book-appointment".equals(path)) {
                    showBookAppointment(req, resp);
                } else if ("/profile".equals(path) || "/update-profile".equals(path)) {
                    showProfile(req, resp);
                } else if ("/billing".equals(path) || "/pay".equals(path)) {
                    showBilling(req, resp);
                } else {
                    showAppointments(req, resp);
                }
            } catch (Exception inner) {
                throw new ServletException(inner);
            }
        }
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        appointmentService.processMissedAppointments();
        Patient patient = patientDAO.getPatientByUserId(getLoggedUser(req).getUserId());
        req.setAttribute("pageTitle", "Patient Dashboard");
        req.setAttribute("patient", patient);
        req.setAttribute("upcomingAppointments", appointmentDAO.getUpcomingAppointmentsByPatient(patient.getPatientId()));
        req.setAttribute("recentBills", billDAO.getRecentBillsByPatient(patient.getPatientId(), 5));
        req.setAttribute("recentDoctors", doctorDAO.getRecentDoctors(3));
        String keyword = req.getParameter("q");
        if (keyword != null && !keyword.isBlank()) {
            req.setAttribute("searchResults", doctorDAO.searchDoctors(keyword));
            req.setAttribute("searchKeyword", keyword);
        }
        forward(req, resp, "/WEB-INF/views/patient/dashboard.jsp");
    }

    private void showBookAppointment(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "Book Appointment");
        req.setAttribute("departments", departmentDAO.getAllDepartments());
        int deptId = intParam(req, "deptId");
        List<Doctor> doctors = deptId > 0 ? doctorDAO.getDoctorsByDepartment(deptId) : doctorDAO.getAllDoctors();
        req.setAttribute("doctors", doctors);
        req.setAttribute("selectedDeptId", deptId);
        forward(req, resp, "/WEB-INF/views/patient/book-appointment.jsp");
    }

    private void showAppointments(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Patient patient = patientDAO.getPatientByUserId(getLoggedUser(req).getUserId());
        String keyword = req.getParameter("q");
        String status = req.getParameter("status");
        req.setAttribute("pageTitle", "My Appointments");
        req.setAttribute("appointments",
                filterAppointments(appointmentDAO.getAppointmentsByPatient(patient.getPatientId()), keyword, status));
        req.setAttribute("searchKeyword", keyword);
        req.setAttribute("selectedStatus", status);
        forward(req, resp, "/WEB-INF/views/patient/my-appointments.jsp");
    }

    private void showMedicalRecords(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Patient patient = patientDAO.getPatientByUserId(getLoggedUser(req).getUserId());
        req.setAttribute("pageTitle", "Medical Records");
        List<MedicalRecord> records = medicalRecordDAO.getRecordsByPatient(patient.getPatientId());
        for (MedicalRecord record : records) {
            record.setPrescriptions(prescriptionDAO.getByRecordId(record.getRecordId()));
        }
        req.setAttribute("records", records);
        forward(req, resp, "/WEB-INF/views/patient/medical-records.jsp");
    }

    private void showBilling(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        appointmentService.processMissedAppointments();
        Patient patient = patientDAO.getPatientByUserId(getLoggedUser(req).getUserId());
        req.setAttribute("pageTitle", "Billing");
        req.setAttribute("bills", billDAO.getBillsByPatient(patient.getPatientId()));
        forward(req, resp, "/WEB-INF/views/patient/billing.jsp");
    }

    private void showProfile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "My Profile");
        req.setAttribute("patient", patientDAO.getPatientByUserId(getLoggedUser(req).getUserId()));
        forward(req, resp, "/WEB-INF/views/patient/profile.jsp");
    }

    private List<Appointment> filterAppointments(List<Appointment> appointments, String keyword, String status) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim().toLowerCase();
        String normalizedStatus = status == null ? "" : status.trim();
        return appointments.stream()
                .filter(appointment -> normalizedStatus.isBlank()
                        || normalizedStatus.equalsIgnoreCase(appointment.getStatus()))
                .filter(appointment -> normalizedKeyword.isBlank()
                        || containsIgnoreCase(appointment.getDoctorName(), normalizedKeyword)
                        || containsIgnoreCase(appointment.getDeptName(), normalizedKeyword)
                        || containsIgnoreCase(appointment.getReason(), normalizedKeyword)
                        || containsIgnoreCase(appointment.getStatus(), normalizedKeyword))
                .toList();
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }
}

