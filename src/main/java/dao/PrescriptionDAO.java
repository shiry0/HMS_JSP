package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Prescription;

public class PrescriptionDAO {

    public void insertPrescription(Prescription prescription) throws SQLException {
        String sql = "INSERT INTO prescriptions (record_id, medicine_name, dosage, frequency, duration, instructions) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, prescription.getRecordId());
            ps.setString(2, prescription.getMedicineName());
            ps.setString(3, prescription.getDosage());
            ps.setString(4, prescription.getFrequency());
            ps.setString(5, prescription.getDuration());
            ps.setString(6, prescription.getInstructions());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    prescription.setPrescriptionId(rs.getInt(1));
                }
            }
        }
    }

    public void insertPrescriptions(List<Prescription> prescriptions) throws SQLException {
        for (Prescription prescription : prescriptions) {
            insertPrescription(prescription);
        }
    }

    public List<Prescription> getByRecordId(int recordId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE record_id = ? ORDER BY prescription_id";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prescriptions.add(mapPrescription(rs));
                }
            }
        }
        return prescriptions;
    }

    public void deleteByRecordId(int recordId) throws SQLException {
        String sql = "DELETE FROM prescriptions WHERE record_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            ps.executeUpdate();
        }
    }

    private Prescription mapPrescription(ResultSet rs) throws SQLException {
        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(rs.getInt("prescription_id"));
        prescription.setRecordId(rs.getInt("record_id"));
        prescription.setMedicineName(rs.getString("medicine_name"));
        prescription.setDosage(rs.getString("dosage"));
        prescription.setFrequency(rs.getString("frequency"));
        prescription.setDuration(rs.getString("duration"));
        prescription.setInstructions(rs.getString("instructions"));
        return prescription;
    }
}
