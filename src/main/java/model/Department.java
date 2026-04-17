package model;

import java.sql.Timestamp;

public class Department {
    private int deptId;
    private String deptName;
    private String description;
    private Timestamp createdAt;

    public Department() {
    }

    public Department(int deptId, String deptName, String description, Timestamp createdAt) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
