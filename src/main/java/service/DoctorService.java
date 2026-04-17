package service;

import dao.DepartmentDAO;
import dao.DoctorDAO;
import dao.UserDAO;
import model.Department;
import model.Doctor;
import model.User;
import util.PasswordUtil;
import util.ValidationUtil;

public class DoctorService {
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final UserDAO userDAO = new UserDAO();

    public void addDoctor(String fullName, String email, String phone, String password, int deptId, String specialization,
            String qualification, int experienceYrs, double fee, String availableDays) throws Exception {
        validateDoctorInput(fullName, email, phone, specialization, qualification, fee);
        if (!ValidationUtil.isValidPassword(password)) {
            throw new Exception("Password must be 8+ chars with 1 uppercase, 1 digit, and 1 special character.");
        }
        ensureUniqueCredentials(email, phone, 0);
        ensureDepartmentExists(deptId);

        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone.trim());
        user.setPassword(PasswordUtil.hashMD5(password));
        user.setRole("doctor");
        user.setActive(true);
        user.setFailedAttempts(0);

        int userId = userDAO.insertUser(user);

        Doctor doctor = new Doctor();
        doctor.setUserId(userId);
        doctor.setDeptId(deptId);
        doctor.setSpecialization(specialization.trim());
        doctor.setQualification(qualification.trim());
        doctor.setExperienceYrs(Math.max(experienceYrs, 0));
        doctor.setConsultationFee(fee);
        doctor.setAvailableDays(availableDays == null ? "" : availableDays.trim());
        doctorDAO.insertDoctor(doctor);
    }

    public void updateDoctor(int doctorId, String fullName, String email, String phone, int deptId, String specialization,
            String qualification, int experienceYrs, double fee, String availableDays, boolean active) throws Exception {
        Doctor existing = doctorDAO.getDoctorById(doctorId);
        if (existing == null) {
            throw new Exception("Doctor not found.");
        }

        validateDoctorInput(fullName, email, phone, specialization, qualification, fee);
        ensureUniqueCredentials(email, phone, existing.getUserId());
        ensureDepartmentExists(deptId);

        User user = userDAO.getUserById(existing.getUserId());
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone.trim());
        user.setActive(active);
        userDAO.updateBasicProfile(user);

        existing.setDeptId(deptId);
        existing.setSpecialization(specialization.trim());
        existing.setQualification(qualification.trim());
        existing.setExperienceYrs(Math.max(experienceYrs, 0));
        existing.setConsultationFee(fee);
        existing.setAvailableDays(availableDays == null ? "" : availableDays.trim());
        doctorDAO.updateDoctor(existing);
    }

    public void deleteDoctor(int doctorId) throws Exception {
        if (doctorDAO.getDoctorById(doctorId) == null) {
            throw new Exception("Doctor not found.");
        }
        doctorDAO.deleteDoctor(doctorId);
    }

    private void validateDoctorInput(String fullName, String email, String phone, String specialization,
            String qualification, double fee) throws Exception {
        if (!ValidationUtil.isValidName(fullName)) {
            throw new Exception("Name must contain letters and spaces only.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new Exception("Enter a valid email address.");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new Exception("Phone number must be exactly 10 digits.");
        }
        if (!ValidationUtil.isNotEmpty(specialization)) {
            throw new Exception("Specialization is required.");
        }
        if (!ValidationUtil.isNotEmpty(qualification)) {
            throw new Exception("Qualification is required.");
        }
        if (fee < 0) {
            throw new Exception("Consultation fee cannot be negative.");
        }
    }

    private void ensureUniqueCredentials(String email, String phone, int currentUserId) throws Exception {
        User emailOwner = userDAO.getUserByEmail(email.trim());
        if (emailOwner != null && emailOwner.getUserId() != currentUserId) {
            throw new Exception("Email already registered.");
        }
        User phoneOwner = userDAO.getUserByPhone(phone.trim());
        if (phoneOwner != null && phoneOwner.getUserId() != currentUserId) {
            throw new Exception("Phone number already registered.");
        }
    }

    private void ensureDepartmentExists(int deptId) throws Exception {
        Department department = departmentDAO.getDepartmentById(deptId);
        if (department == null) {
            throw new Exception("Selected department does not exist.");
        }
    }
}
