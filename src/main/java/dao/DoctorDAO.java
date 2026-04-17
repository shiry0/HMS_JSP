package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Doctor;

public class DoctorDAO {

    public void insertDoctor(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctors (user_id, dept_id, specialization, qualification, experience_yrs, consultation_fee, available_days) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, doctor.getUserId());
            ps.setInt(2, doctor.getDeptId());
            ps.setString(3, doctor.getSpecialization());
            ps.setString(4, doctor.getQualification());
            ps.setInt(5, doctor.getExperienceYrs());
            ps.setDouble(6, doctor.getConsultationFee());
            ps.setString(7, doctor.getAvailableDays());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    doctor.setDoctorId(rs.getInt(1));
                }
            }
        }
    }

    public Doctor getDoctorByUserId(int userId) throws SQLException {
        String sql = baseSelect() + " WHERE d.user_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapDoctor(rs) : null;
            }
        }
    }

    public Doctor getDoctorById(int doctorId) throws SQLException {
        String sql = baseSelect() + " WHERE d.doctor_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapDoctor(rs) : null;
            }
        }
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = baseSelect() + " ORDER BY u.full_name";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }
        return doctors;
    }

    public List<Doctor> getDoctorsByDepartment(int deptId) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = baseSelect() + " WHERE d.dept_id = ? ORDER BY u.full_name";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapDoctor(rs));
                }
            }
        }
        return doctors;
    }

    public List<Doctor> searchDoctors(String keyword) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = baseSelect()
                + " WHERE u.full_name LIKE ? OR d.specialization LIKE ? OR dept.dept_name LIKE ? ORDER BY u.full_name";
        String term = "%" + keyword + "%";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapDoctor(rs));
                }
            }
        }
        return doctors;
    }

    public void updateDoctor(Doctor doctor) throws SQLException {
        String sql = "UPDATE doctors SET dept_id = ?, specialization = ?, qualification = ?, experience_yrs = ?, "
                + "consultation_fee = ?, available_days = ? WHERE doctor_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctor.getDeptId());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getQualification());
            ps.setInt(4, doctor.getExperienceYrs());
            ps.setDouble(5, doctor.getConsultationFee());
            ps.setString(6, doctor.getAvailableDays());
            ps.setInt(7, doctor.getDoctorId());
            ps.executeUpdate();
        }
    }

    public void deleteDoctor(int doctorId) throws SQLException {
        String sql = "DELETE u FROM users u JOIN doctors d ON u.user_id = d.user_id WHERE d.doctor_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.executeUpdate();
        }
    }

    public int countDoctors() throws SQLException {
        String sql = "SELECT COUNT(*) FROM doctors";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public List<Doctor> getRecentDoctors(int limit) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = baseSelect() + " ORDER BY u.created_at DESC LIMIT ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapDoctor(rs));
                }
            }
        }
        return doctors;
    }

    private String baseSelect() {
        return "SELECT d.*, u.full_name, u.email, u.phone, u.is_active, u.created_at, dept.dept_name "
                + "FROM doctors d "
                + "JOIN users u ON d.user_id = u.user_id "
                + "JOIN departments dept ON d.dept_id = dept.dept_id";
    }

    private Doctor mapDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setUserId(rs.getInt("user_id"));
        doctor.setDeptId(rs.getInt("dept_id"));
        doctor.setActive(rs.getBoolean("is_active"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setQualification(rs.getString("qualification"));
        doctor.setExperienceYrs(rs.getInt("experience_yrs"));
        doctor.setConsultationFee(rs.getDouble("consultation_fee"));
        doctor.setAvailableDays(rs.getString("available_days"));
        doctor.setFullName(rs.getString("full_name"));
        doctor.setEmail(rs.getString("email"));
        doctor.setPhone(rs.getString("phone"));
        doctor.setDeptName(rs.getString("dept_name"));
        doctor.setCreatedAt(rs.getTimestamp("created_at"));
        return doctor;
    }
}
