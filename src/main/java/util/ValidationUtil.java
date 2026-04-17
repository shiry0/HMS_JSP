package util;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[0-9]{10}$");
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-z ]{2,100}$");
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%!]).{8,}$");
    }

    public static boolean isPositiveAmount(double amount) {
        return amount > 0;
    }
}
