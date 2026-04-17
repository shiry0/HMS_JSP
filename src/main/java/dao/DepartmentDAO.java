package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Department;

public class DepartmentDAO {

    public List<Department> getAllDepartments() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments ORDER BY dept_name";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                departments.add(mapDepartment(rs));
            }
        }
        return departments;
    }

    public Department getDepartmentById(int deptId) throws SQLException {
        String sql = "SELECT * FROM departments WHERE dept_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapDepartment(rs) : null;
            }
        }
    }

    public void insertDepartment(Department department) throws SQLException {
        String sql = "INSERT INTO departments (dept_name, description) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, department.getDeptName());
            ps.setString(2, department.getDescription());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    department.setDeptId(rs.getInt(1));
                }
            }
        }
    }

    public void updateDepartment(Department department) throws SQLException {
        String sql = "UPDATE departments SET dept_name = ?, description = ? WHERE dept_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, department.getDeptName());
            ps.setString(2, department.getDescription());
            ps.setInt(3, department.getDeptId());
            ps.executeUpdate();
        }
    }

    public void deleteDepartment(int deptId) throws SQLException {
        String sql = "DELETE FROM departments WHERE dept_id = ?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            ps.executeUpdate();
        }
    }

    private Department mapDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setDeptId(rs.getInt("dept_id"));
        department.setDeptName(rs.getString("dept_name"));
        department.setDescription(rs.getString("description"));
        department.setCreatedAt(rs.getTimestamp("created_at"));
        return department;
    }
}
