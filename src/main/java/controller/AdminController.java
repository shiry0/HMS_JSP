package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.AppointmentDAO;
import dao.BillDAO;
import dao.DepartmentDAO;
import dao.DoctorDAO;
import dao.PatientDAO;
import model.Department;
import service.DoctorService;
import service.PatientService;

@WebServlet("/admin/*")
public class AdminController extends BaseController {
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final BillDAO billDAO = new BillDAO();
    private final DoctorService doctorService = new DoctorService();
    private final PatientService patientService = new PatientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "/dashboard" : req.getPathInfo();
        try {
            switch (path) {
                case "/dashboard":
                    showDashboard(req, resp);
                    break;
                case "/manage-doctors":
                    showDoctorManagement(req, resp);
                    break;
                case "/manage-patients":
                    showPatientManagement(req, resp);
                    break;
                case "/departments":
                    showDepartments(req, resp);
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
                case "/add-doctor":
                    doctorService.addDoctor(req.getParameter("fullName"), req.getParameter("email"),
                            req.getParameter("phone"), req.getParameter("password"), intParam(req, "deptId"),
                            req.getParameter("specialization"), req.getParameter("qualification"),
                            intParam(req, "experienceYrs"), doubleParam(req, "consultationFee"),
                            req.getParameter("availableDays"));
                    setFlash(req, "success", "Doctor added successfully.");
                    redirect(req, resp, "/admin/manage-doctors");
                    break;
                case "/update-doctor":
                    doctorService.updateDoctor(intParam(req, "doctorId"), req.getParameter("fullName"),
                            req.getParameter("email"), req.getParameter("phone"), intParam(req, "deptId"),
                            req.getParameter("specialization"), req.getParameter("qualification"),
                            intParam(req, "experienceYrs"), doubleParam(req, "consultationFee"),
                            req.getParameter("availableDays"), booleanParam(req, "active"));
                    setFlash(req, "success", "Doctor updated successfully.");
                    redirect(req, resp, "/admin/manage-doctors");
                    break;
                case "/delete-doctor":
                    doctorService.deleteDoctor(intParam(req, "doctorId"));
                    setFlash(req, "success", "Doctor deleted successfully.");
                    redirect(req, resp, "/admin/manage-doctors");
                    break;
                case "/add-patient":
                    patientService.addPatient(req.getParameter("fullName"), req.getParameter("email"),
                            req.getParameter("phone"), req.getParameter("password"),
                            req.getParameter("dob") == null || req.getParameter("dob").isBlank() ? null
                                    : java.sql.Date.valueOf(req.getParameter("dob")),
                            req.getParameter("gender"), req.getParameter("bloodGroup"), req.getParameter("address"),
                            req.getParameter("emergencyContact"));
                    setFlash(req, "success", "Patient added successfully.");
                    redirect(req, resp, "/admin/manage-patients");
                    break;
                case "/update-patient":
                    patientService.updatePatientByAdmin(intParam(req, "patientId"), req.getParameter("fullName"),
                            req.getParameter("email"), req.getParameter("phone"),
                            req.getParameter("dob") == null || req.getParameter("dob").isBlank() ? null
                                    : java.sql.Date.valueOf(req.getParameter("dob")),
                            req.getParameter("gender"), req.getParameter("bloodGroup"), req.getParameter("address"),
                            req.getParameter("emergencyContact"), booleanParam(req, "active"));
                    setFlash(req, "success", "Patient updated successfully.");
                    redirect(req, resp, "/admin/manage-patients");
                    break;
                case "/delete-patient":
                    patientService.deletePatient(intParam(req, "patientId"));
                    setFlash(req, "success", "Patient deleted successfully.");
                    redirect(req, resp, "/admin/manage-patients");
                    break;
                case "/add-department":
                    Department department = new Department();
                    department.setDeptName(req.getParameter("deptName"));
                    department.setDescription(req.getParameter("description"));
                    departmentDAO.insertDepartment(department);
                    setFlash(req, "success", "Department added successfully.");
                    redirect(req, resp, "/admin/departments");
                    break;
                case "/update-department":
                    Department editingDepartment = new Department();
                    editingDepartment.setDeptId(intParam(req, "deptId"));
                    editingDepartment.setDeptName(req.getParameter("deptName"));
                    editingDepartment.setDescription(req.getParameter("description"));
                    departmentDAO.updateDepartment(editingDepartment);
                    setFlash(req, "success", "Department updated successfully.");
                    redirect(req, resp, "/admin/departments");
                    break;
                case "/delete-department":
                    departmentDAO.deleteDepartment(intParam(req, "deptId"));
                    setFlash(req, "success", "Department deleted successfully.");
                    redirect(req, resp, "/admin/departments");
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            try {
                if (path.contains("doctor")) {
                    showDoctorManagement(req, resp);
                } else if (path.contains("patient")) {
                    showPatientManagement(req, resp);
                } else {
                    showDepartments(req, resp);
                }
            } catch (Exception inner) {
                throw new ServletException(inner);
            }
        }
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "Admin Dashboard");
        req.setAttribute("doctorCount", doctorDAO.countDoctors());
        req.setAttribute("patientCount", patientDAO.countPatients());
        req.setAttribute("todayAppointmentsCount", appointmentDAO.countTodayAppointments());
        req.setAttribute("totalRevenue", billDAO.getTotalRevenue());
        req.setAttribute("todayAppointments", appointmentDAO.getTodayAppointments());
        forward(req, resp, "/WEB-INF/views/admin/dashboard.jsp");
    }

    private void showDoctorManagement(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "Manage Doctors");
        req.setAttribute("doctors", doctorDAO.getAllDoctors());
        req.setAttribute("departments", departmentDAO.getAllDepartments());
        int editDoctorId = intParam(req, "editDoctorId");
        if (editDoctorId == 0) {
            editDoctorId = intParam(req, "doctorId");
        }
        if (editDoctorId > 0) {
            req.setAttribute("editingDoctor", doctorDAO.getDoctorById(editDoctorId));
        }
        forward(req, resp, "/WEB-INF/views/admin/manage-doctors.jsp");
    }

    private void showPatientManagement(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "Manage Patients");
        String keyword = req.getParameter("q");
        req.setAttribute("patients",
                keyword != null && !keyword.isBlank() ? patientDAO.searchPatients(keyword) : patientDAO.getAllPatients());
        int editPatientId = intParam(req, "editPatientId");
        if (editPatientId == 0) {
            editPatientId = intParam(req, "patientId");
        }
        if (editPatientId > 0) {
            req.setAttribute("editingPatient", patientDAO.getPatientById(editPatientId));
        }
        forward(req, resp, "/WEB-INF/views/admin/manage-patients.jsp");
    }

    private void showDepartments(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("pageTitle", "Manage Departments");
        req.setAttribute("departments", departmentDAO.getAllDepartments());
        int editDeptId = intParam(req, "editDeptId");
        if (editDeptId == 0) {
            editDeptId = intParam(req, "deptId");
        }
        if (editDeptId > 0) {
            req.setAttribute("editingDepartment", departmentDAO.getDepartmentById(editDeptId));
        }
        forward(req, resp, "/WEB-INF/views/admin/manage-departments.jsp");
    }
}

