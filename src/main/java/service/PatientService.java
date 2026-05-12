package service;

import dao.PatientDAO;
import dao.UserDAO;
import model.Patient;
import model.User;
import util.PasswordUtil;
import util.ValidationUtil;

public class PatientService {
    private final PatientDAO patientDAO = new PatientDAO();
    private final UserDAO userDAO = new UserDAO();

    public void addPatient(String fullName, String email, String phone, String password, java.sql.Date dob, String gender,
            String bloodGroup, String address, String emergencyContact) throws Exception {
        validatePatientInput(fullName, email, phone, dob, address);
        if (!ValidationUtil.isValidPassword(password)) {
            throw new Exception("Password must be 8+ chars with 1 uppercase, 1 digit, and 1 special character.");
        }
        ensureUniqueCredentials(email, phone, 0);

        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone.trim());
        user.setPassword(PasswordUtil.hashMD5(password));
        user.setRole("patient");
        user.setActive(true);
        user.setFailedAttempts(0);

        int userId = userDAO.insertUser(user);

        Patient patient = new Patient();
        patient.setUserId(userId);
        patient.setDob(dob);
        patient.setGender(gender);
        patient.setBloodGroup(bloodGroup);
        patient.setAddress(address == null ? "" : address.trim());
        patient.setEmergencyContact(emergencyContact == null ? "" : emergencyContact.trim());
        patientDAO.insertPatient(patient);
    }

    public void updatePatientByAdmin(int patientId, String fullName, String email, String phone, java.sql.Date dob,
            String gender, String bloodGroup, String address, String emergencyContact, boolean active) throws Exception {
        Patient patient = patientDAO.getPatientById(patientId);
        if (patient == null) {
            throw new Exception("Patient not found.");
        }

        validatePatientInput(fullName, email, phone, dob, address);
        ensureUniqueCredentials(email, phone, patient.getUserId());

        User user = userDAO.getUserById(patient.getUserId());
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone.trim());
        user.setActive(active);
        userDAO.updateBasicProfile(user);

        patient.setDob(dob);
        patient.setGender(gender);
        patient.setBloodGroup(bloodGroup);
        patient.setAddress(address == null ? "" : address.trim());
        patient.setEmergencyContact(emergencyContact == null ? "" : emergencyContact.trim());
        patientDAO.updatePatient(patient);
    }

    public void updateProfile(int userId, String fullName, String email, String phone, java.sql.Date dob, String gender,
            String bloodGroup, String address, String emergencyContact, String newPassword) throws Exception {
        Patient patient = patientDAO.getPatientByUserId(userId);
        if (patient == null) {
            throw new Exception("Patient profile not found.");
        }

        validatePatientInput(fullName, email, phone, dob, address);
        ensureUniqueCredentials(email, phone, userId);

        User user = userDAO.getUserById(userId);
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone.trim());
        user.setActive(true);
        userDAO.updateBasicProfile(user);

        patient.setDob(dob);
        patient.setGender(gender);
        patient.setBloodGroup(bloodGroup);
        patient.setAddress(address == null ? "" : address.trim());
        patient.setEmergencyContact(emergencyContact == null ? "" : emergencyContact.trim());
        patientDAO.updatePatient(patient);

        if (newPassword != null && !newPassword.isBlank()) {
            if (!ValidationUtil.isValidPassword(newPassword)) {
                throw new Exception("New password must be 8+ chars with 1 uppercase, 1 digit, and 1 special character.");
            }
            userDAO.resetPassword(userId, PasswordUtil.hashMD5(newPassword.trim()));
        }
    }

    public void updatePatientByDoctor(int patientId, String fullName, java.sql.Date dob, String gender,
            String bloodGroup, String address, String emergencyContact) throws Exception {
        Patient patient = patientDAO.getPatientById(patientId);
        if (patient == null) {
            throw new Exception("Patient not found.");
        }
        if (!ValidationUtil.isValidName(fullName)) {
            throw new Exception("Name must contain letters and spaces only.");
        }
        if (dob != null && !dob.before(new java.sql.Date(System.currentTimeMillis()))) {
            throw new Exception("Date of birth must be in the past.");
        }

        User user = userDAO.getUserById(patient.getUserId());
        user.setFullName(fullName.trim());
        userDAO.updateBasicProfile(user);

        patient.setDob(dob);
        patient.setGender(gender);
        patient.setBloodGroup(bloodGroup);
        patient.setAddress(address == null ? "" : address.trim());
        patient.setEmergencyContact(emergencyContact == null ? "" : emergencyContact.trim());
        patientDAO.updatePatient(patient);
    }

    public void deletePatient(int patientId) throws Exception {
        if (patientDAO.getPatientById(patientId) == null) {
            throw new Exception("Patient not found.");
        }
        patientDAO.deletePatient(patientId);
    }

    public void unblockPatient(int patientId) throws Exception {
        Patient patient = patientDAO.getPatientById(patientId);
        if (patient == null) {
            throw new Exception("Patient not found.");
        }
        userDAO.clearLoginAttempts(patient.getUserId());
    }

    private void validatePatientInput(String fullName, String email, String phone, java.sql.Date dob, String address)
            throws Exception {
        if (!ValidationUtil.isValidName(fullName)) {
            throw new Exception("Name must contain letters and spaces only.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new Exception("Enter a valid email address.");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new Exception("Phone number must be exactly 10 digits.");
        }
        if (dob != null && !dob.before(new java.sql.Date(System.currentTimeMillis()))) {
            throw new Exception("Date of birth must be in the past.");
        }
        if (address != null && address.trim().isEmpty()) {
            throw new Exception("Address cannot be empty.");
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
}
