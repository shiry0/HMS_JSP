package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.MedicalRecord;

public class MedicalRecordDAO {

    public int insertRecord(MedicalRecord record) throws SQLException {
        String sql = "INSERT INTO medical_records (patient_id, doctor_id, appt_id, diagnosis, symptoms, treatment, record_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, record.getPatientId());
            ps.setInt(2, record.getDoctorId());
            if (record.getApptId() > 0) {
                ps.setInt(3, record.getApptId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setString(4, record.getDiagnosis());
            ps.setString(5, record.getSymptoms());
            ps.setString(6, record.getTreatment());
            ps.setDate(7, record.getRecordDate());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<MedicalRecord> getRecordsByPatient(int patientId) throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = baseSelect() + " WHERE mr.patient_id = ? ORDER BY mr.record_date DESC";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRecord(rs));
                }
            }
        }
        return records;
    }

    public MedicalRecord getRecordById(int recordId) throws SQLException {
        String sql = baseSelect() + " WHERE mr.record_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRecord(rs) : null;
            }
        }
    }

    public List<MedicalRecord> getRecordsByDoctor(int doctorId, String keyword) throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        StringBuilder sql = new StringBuilder(baseSelect()).append(" WHERE mr.doctor_id = ?");
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        if (hasKeyword) {
            sql.append(" AND (LOWER(pu.full_name) LIKE ? OR LOWER(mr.diagnosis) LIKE ? OR LOWER(mr.symptoms) LIKE ? OR LOWER(mr.treatment) LIKE ?)");
        }
        sql.append(" ORDER BY mr.record_date DESC");

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setInt(1, doctorId);
            if (hasKeyword) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                ps.setString(2, pattern);
                ps.setString(3, pattern);
                ps.setString(4, pattern);
                ps.setString(5, pattern);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRecord(rs));
                }
            }
        }
        return records;
    }

    public void updateRecord(MedicalRecord record) throws SQLException {
        String sql = "UPDATE medical_records SET diagnosis = ?, symptoms = ?, treatment = ?, record_date = ? WHERE record_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, record.getDiagnosis());
            ps.setString(2, record.getSymptoms());
            ps.setString(3, record.getTreatment());
            ps.setDate(4, record.getRecordDate());
            ps.setInt(5, record.getRecordId());
            ps.executeUpdate();
        }
    }

    public void deleteRecord(int recordId) throws SQLException {
        String sql = "DELETE FROM medical_records WHERE record_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            ps.executeUpdate();
        }
    }

    private String baseSelect() {
        return "SELECT mr.*, du.full_name AS doctor_name, pu.full_name AS patient_name "
                + "FROM medical_records mr "
                + "JOIN doctors d ON mr.doctor_id = d.doctor_id "
                + "JOIN users du ON d.user_id = du.user_id "
                + "JOIN patients p ON mr.patient_id = p.patient_id "
                + "JOIN users pu ON p.user_id = pu.user_id";
    }

    private MedicalRecord mapRecord(ResultSet rs) throws SQLException {
        MedicalRecord record = new MedicalRecord();
        record.setRecordId(rs.getInt("record_id"));
        record.setPatientId(rs.getInt("patient_id"));
        record.setDoctorId(rs.getInt("doctor_id"));
        record.setApptId(rs.getInt("appt_id"));
        record.setDiagnosis(rs.getString("diagnosis"));
        record.setSymptoms(rs.getString("symptoms"));
        record.setTreatment(rs.getString("treatment"));
        record.setRecordDate(rs.getDate("record_date"));
        record.setDoctorName(rs.getString("doctor_name"));
        record.setPatientName(rs.getString("patient_name"));
        return record;
    }
}
