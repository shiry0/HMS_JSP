package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Bill;

public class BillDAO {

    public void insertBill(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills (patient_id, appt_id, consultation_fee, medicine_fee, test_fee, late_fee, total_amount, paid_amount, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bill.getPatientId());
            if (bill.getApptId() > 0) {
                ps.setInt(2, bill.getApptId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setDouble(3, bill.getConsultationFee());
            ps.setDouble(4, bill.getMedicineFee());
            ps.setDouble(5, bill.getTestFee());
            ps.setDouble(6, bill.getLateFee());
            ps.setDouble(7, bill.getTotalAmount());
            ps.setDouble(8, bill.getPaidAmount());
            ps.setString(9, bill.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    bill.setBillId(rs.getInt(1));
                }
            }
        }
    }

    public List<Bill> getBillsByPatient(int patientId) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String sql = baseSelect() + " WHERE b.patient_id = ? ORDER BY b.bill_date DESC";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bills.add(mapBill(rs));
                }
            }
        }
        return bills;
    }

    public List<Bill> getRecentBillsByPatient(int patientId, int limit) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String sql = baseSelect() + " WHERE b.patient_id = ? ORDER BY b.bill_date DESC LIMIT ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bills.add(mapBill(rs));
                }
            }
        }
        return bills;
    }

    public List<Bill> getAllBills() throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String sql = baseSelect() + " ORDER BY b.bill_date DESC";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                bills.add(mapBill(rs));
            }
        }
        return bills;
    }

    public void updatePayment(int billId, double paidAmount, String status) throws SQLException {
        String sql = "UPDATE bills SET paid_amount = ?, status = ? WHERE bill_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, paidAmount);
            ps.setString(2, status);
            ps.setInt(3, billId);
            ps.executeUpdate();
        }
    }

    public Bill getBillById(int billId) throws SQLException {
        String sql = baseSelect() + " WHERE b.bill_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapBill(rs) : null;
            }
        }
    }

    public Bill getBillByAppointmentId(int apptId) throws SQLException {
        String sql = baseSelect() + " WHERE b.appt_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, apptId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapBill(rs) : null;
            }
        }
    }

    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(paid_amount), 0) FROM bills";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }

    private String baseSelect() {
        return "SELECT b.*, u.full_name AS patient_name "
                + "FROM bills b "
                + "JOIN patients p ON b.patient_id = p.patient_id "
                + "JOIN users u ON p.user_id = u.user_id";
    }

    private Bill mapBill(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setBillId(rs.getInt("bill_id"));
        bill.setPatientId(rs.getInt("patient_id"));
        bill.setApptId(rs.getInt("appt_id"));
        bill.setConsultationFee(rs.getDouble("consultation_fee"));
        bill.setMedicineFee(rs.getDouble("medicine_fee"));
        bill.setTestFee(rs.getDouble("test_fee"));
        bill.setLateFee(rs.getDouble("late_fee"));
        bill.setTotalAmount(rs.getDouble("total_amount"));
        bill.setPaidAmount(rs.getDouble("paid_amount"));
        bill.setStatus(rs.getString("status"));
        bill.setBillDate(rs.getTimestamp("bill_date"));
        bill.setPatientName(rs.getString("patient_name"));
        return bill;
    }
}
