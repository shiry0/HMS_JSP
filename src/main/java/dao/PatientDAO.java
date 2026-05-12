package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Patient;

public class PatientDAO {

    public void insertPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (user_id, dob, gender, blood_group, address, emergency_contact) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patient.getUserId());
            ps.setDate(2, patient.getDob());
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getBloodGroup());
            ps.setString(5, patient.getAddress());
            ps.setString(6, patient.getEmergencyContact());
            ps.executeUpdate();
        }
    }

    public Patient getPatientByUserId(int userId) throws SQLException {
        String sql = baseSelect() + " WHERE p.user_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapPatient(rs) : null;
            }
        }
    }

    public Patient getPatientById(int patientId) throws SQLException {
        String sql = baseSelect() + " WHERE p.patient_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapPatient(rs) : null;
            }
        }
    }

    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = baseSelect() + " ORDER BY u.full_name";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
        }
        return patients;
    }

    public void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET dob = ?, gender = ?, blood_group = ?, address = ?, emergency_contact = ? "
                + "WHERE patient_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, patient.getDob());
            ps.setString(2, patient.getGender());
            ps.setString(3, patient.getBloodGroup());
            ps.setString(4, patient.getAddress());
            ps.setString(5, patient.getEmergencyContact());
            ps.setInt(6, patient.getPatientId());
            ps.executeUpdate();
        }
    }

    public List<Patient> searchPatients(String keyword) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = baseSelect()
                + " WHERE u.full_name LIKE ? OR u.email LIKE ? OR u.phone LIKE ? OR p.blood_group LIKE ? ORDER BY u.full_name";
        String term = "%" + keyword + "%";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, term);
            ps.setString(2, term);
            ps.setString(3, term);
            ps.setString(4, term);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapPatient(rs));
                }
            }
        }
        return patients;
    }

    public void deletePatient(int patientId) throws SQLException {
        String sql = "DELETE u FROM users u JOIN patients p ON u.user_id = p.user_id WHERE p.patient_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.executeUpdate();
        }
    }

    public int countPatients() throws SQLException {
        String sql = "SELECT COUNT(*) FROM patients";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private String baseSelect() {
        return "SELECT p.*, u.full_name, u.email, u.phone, u.is_active, u.locked_until, u.created_at "
                + "FROM patients p JOIN users u ON p.user_id = u.user_id";
    }

    private Patient mapPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setUserId(rs.getInt("user_id"));
        patient.setActive(rs.getBoolean("is_active"));
        patient.setDob(rs.getDate("dob"));
        patient.setGender(rs.getString("gender"));
        patient.setBloodGroup(rs.getString("blood_group"));
        patient.setAddress(rs.getString("address"));
        patient.setEmergencyContact(rs.getString("emergency_contact"));
        patient.setFullName(rs.getString("full_name"));
        patient.setEmail(rs.getString("email"));
        patient.setPhone(rs.getString("phone"));
        patient.setLockedUntil(rs.getTimestamp("locked_until"));
        patient.setCreatedAt(rs.getTimestamp("created_at"));
        return patient;
    }
}
