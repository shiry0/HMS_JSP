package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.AuthService;

@WebServlet("/register")
public class RegisterController extends BaseController {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (getLoggedUser(req) != null) {
            redirectLoggedUser(req, resp);
            return;
        }
        forward(req, resp, "/WEB-INF/views/auth/register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (getLoggedUser(req) != null) {
            redirectLoggedUser(req, resp);
            return;
        }
        try {
            String fullName = req.getParameter("fullName");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String password = req.getParameter("password");
            String confirmPassword = req.getParameter("confirmPassword");

            if (password == null || !password.equals(confirmPassword)) {
                throw new Exception("Passwords do not match.");
            }

            authService.register(fullName, email, phone, password, "patient");
            setFlash(req, "success", "Registration successful. You can now log in.");
            redirect(req, resp, "/login");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            forward(req, resp, "/WEB-INF/views/auth/register.jsp");
        }
    }
}

