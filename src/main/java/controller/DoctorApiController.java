package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.DoctorDAO;
import model.Doctor;

@WebServlet("/api/doctors")
public class DoctorApiController extends BaseController {
    private final DoctorDAO doctorDAO = new DoctorDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            int deptId = intParam(req, "deptId");
            List<Doctor> doctors = deptId > 0 ? doctorDAO.getDoctorsByDepartment(deptId) : doctorDAO.getAllDoctors();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < doctors.size(); i++) {
                Doctor doctor = doctors.get(i);
                if (i > 0) {
                    json.append(',');
                }
                json.append("{\"doctorId\":").append(doctor.getDoctorId())
                        .append(",\"fullName\":\"").append(escapeJson(doctor.getFullName()))
                        .append("\",\"specialization\":\"").append(escapeJson(doctor.getSpecialization()))
                        .append("\"}");
            }
            json.append(']');
            resp.getWriter().write(json.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Unable to load doctors\"}");
        }
    }

    private String escapeJson(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

