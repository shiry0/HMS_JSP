package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import model.Appointment;

public class AppointmentDAO {

    public void insertAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appt_date, appt_time, status, reason, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDoctorId());
            ps.setDate(3, appointment.getApptDate());
            ps.setTime(4, appointment.getApptTime());
            ps.setString(5, appointment.getStatus());
            ps.setString(6, appointment.getReason());
            ps.setString(7, appointment.getNotes());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    appointment.setApptId(rs.getInt(1));
                }
            }
        }
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = baseSelect() + " WHERE a.patient_id = ? ORDER BY a.appt_date DESC, a.appt_time DESC";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapAppointment(rs));
                }
            }
        }
        return appointments;
    }

    public List<Appointment> getUpcomingAppointmentsByPatient(int patientId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = baseSelect()
                + " WHERE a.patient_id = ? AND a.appt_date >= CURDATE() ORDER BY a.appt_date, a.appt_time";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapAppointment(rs));
                }
            }
        }
        return appointments;
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = baseSelect() + " WHERE a.doctor_id = ? ORDER BY a.appt_date DESC, a.appt_time DESC";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapAppointment(rs));
                }
            }
        }
        return appointments;
    }

    public List<Appointment> getAppointmentsByDoctorOnDate(int doctorId, Date date) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = baseSelect() + " WHERE a.doctor_id = ? AND a.appt_date = ? ORDER BY a.appt_time";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapAppointment(rs));
                }
            }
        }
        return appointments;
    }

    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = baseSelect() + " ORDER BY a.appt_date DESC, a.appt_time DESC";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                appointments.add(mapAppointment(rs));
            }
        }
        return appointments;
    }

    public List<Appointment> getTodayAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = baseSelect() + " WHERE a.appt_date = CURDATE() ORDER BY a.appt_time";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                appointments.add(mapAppointment(rs));
            }
        }
        return appointments;
    }

    public List<Appointment> getTodayAppointmentsForDoctor(int doctorId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = baseSelect() + " WHERE a.doctor_id = ? AND a.appt_date = CURDATE() ORDER BY a.appt_time";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapAppointment(rs));
                }
            }
        }
        return appointments;
    }

    public List<Appointment> getMissedAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = baseSelect()
                + " WHERE a.appt_date < CURDATE() AND a.status IN ('Pending', 'Confirmed') ORDER BY a.appt_date";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                appointments.add(mapAppointment(rs));
            }
        }
        return appointments;
    }

    public Appointment getAppointmentById(int apptId) throws SQLException {
        String sql = baseSelect() + " WHERE a.appt_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, apptId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapAppointment(rs) : null;
            }
        }
    }

    public void updateStatus(int apptId, String status) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE appt_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, apptId);
            ps.executeUpdate();
        }
    }

    public void cancelAppointment(int apptId) throws SQLException {
        updateStatus(apptId, "Cancelled");
    }

    public void cancelAppointment(int apptId, String notes) throws SQLException {
        String sql = "UPDATE appointments SET status = ?, notes = ? WHERE appt_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "Cancelled");
            ps.setString(2, notes);
            ps.setInt(3, apptId);
            ps.executeUpdate();
        }
    }

    public boolean isDoctorAvailable(int doctorId, Date date, Time time) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appt_date = ? AND appt_time = ? "
                + "AND status <> 'Cancelled'";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, date);
            ps.setTime(3, time);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    }

    public int countTodayAppointments() throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE appt_date = CURDATE()";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public boolean belongsToPatient(int apptId, int patientId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE appt_id = ? AND patient_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, apptId);
            ps.setInt(2, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean belongsToDoctor(int apptId, int doctorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE appt_id = ? AND doctor_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, apptId);
            ps.setInt(2, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private String baseSelect() {
        return "SELECT a.*, pu.full_name AS patient_name, du.full_name AS doctor_name, dept.dept_name "
                + "FROM appointments a "
                + "JOIN patients p ON a.patient_id = p.patient_id "
                + "JOIN users pu ON p.user_id = pu.user_id "
                + "JOIN doctors d ON a.doctor_id = d.doctor_id "
                + "JOIN users du ON d.user_id = du.user_id "
                + "JOIN departments dept ON d.dept_id = dept.dept_id";
    }

    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setApptId(rs.getInt("appt_id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        appointment.setApptDate(rs.getDate("appt_date"));
        appointment.setApptTime(rs.getTime("appt_time"));
        appointment.setStatus(rs.getString("status"));
        appointment.setReason(rs.getString("reason"));
        appointment.setNotes(rs.getString("notes"));
        appointment.setCreatedAt(rs.getTimestamp("created_at"));
        appointment.setPatientName(rs.getString("patient_name"));
        appointment.setDoctorName(rs.getString("doctor_name"));
        appointment.setDeptName(rs.getString("dept_name"));
        return appointment;
    }
}
