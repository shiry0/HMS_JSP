package service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import dao.PatientDAO;
import dao.UserDAO;
import model.Patient;
import model.User;
import util.PasswordUtil;
import util.ValidationUtil;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    public User login(String email, String password) throws Exception {
        if (!ValidationUtil.isNotEmpty(email) || !ValidationUtil.isNotEmpty(password)) {
            throw new Exception("Email and password are required.");
        }

        User user = userDAO.getUserByEmail(email.trim());
        if (user == null) {
            throw new Exception("No account found with that email.");
        }
        if (!user.isActive()) {
            throw new Exception("This account is currently inactive.");
        }
        if (user.getLockedUntil() != null && user.getLockedUntil().after(Timestamp.from(Instant.now()))) {
            long minutes = ChronoUnit.MINUTES.between(Instant.now(), user.getLockedUntil().toInstant()) + 1;
            throw new Exception("Account locked. Try again in " + minutes + " minute(s).");
        }

        if (!PasswordUtil.verify(password, user.getPassword())) {
            int attempts = user.getFailedAttempts() + 1;
            userDAO.updateFailedAttempts(user.getUserId(), attempts);
            if (attempts >= 5) {
                Timestamp lockUntil = Timestamp.from(Instant.now().plus(15, ChronoUnit.MINUTES));
                userDAO.lockAccount(user.getUserId(), lockUntil);
                throw new Exception("Account locked for 15 minutes after too many failed attempts.");
            }
            throw new Exception("Invalid credentials.");
        }

        userDAO.clearLoginAttempts(user.getUserId());
        return userDAO.getUserById(user.getUserId());
    }

    public void register(String fullName, String email, String phone, String password, String role) throws Exception {
        validateRegistration(fullName, email, phone, password);
        if (userDAO.getUserByEmail(email.trim()) != null) {
            throw new Exception("Email already registered.");
        }
        if (userDAO.getUserByPhone(phone.trim()) != null) {
            throw new Exception("Phone number already registered.");
        }

        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone.trim());
        user.setPassword(PasswordUtil.hashMD5(password));
        user.setRole(role == null || role.isBlank() ? "patient" : role.trim().toLowerCase());
        user.setActive(true);
        user.setFailedAttempts(0);

        int userId = userDAO.insertUser(user);
        if ("patient".equals(user.getRole())) {
            Patient patient = new Patient();
            patient.setUserId(userId);
            patientDAO.insertPatient(patient);
        }
    }

    public void resetPassword(String email, String phone, String newPassword) throws Exception {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new Exception("Enter a valid email address.");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new Exception("Phone number must be exactly 10 digits.");
        }
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new Exception("Password must be 8+ chars with 1 uppercase, 1 digit, and 1 special character.");
        }

        User user = userDAO.getUserByEmail(email.trim());
        if (user == null) {
            throw new Exception("No account found with that email.");
        }
        if (!phone.trim().equals(user.getPhone())) {
            throw new Exception("Details do not match our records.");
        }

        userDAO.resetPassword(user.getUserId(), PasswordUtil.hashMD5(newPassword));
    }

    private void validateRegistration(String fullName, String email, String phone, String password) throws SQLException, Exception {
        if (!ValidationUtil.isValidName(fullName)) {
            throw new Exception("Name must contain letters and spaces only.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new Exception("Enter a valid email address.");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new Exception("Phone number must be exactly 10 digits.");
        }
        if (!ValidationUtil.isValidPassword(password)) {
            throw new Exception("Password must be 8+ chars with 1 uppercase, 1 digit, and 1 special character.");
        }
    }
}
