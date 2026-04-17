package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.AuthService;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends BaseController {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forward(req, resp, "/WEB-INF/views/auth/forgot-password.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            authService.resetPassword(req.getParameter("email"), req.getParameter("phone"),
                    req.getParameter("newPassword"));
            setFlash(req, "success", "Password reset successful. Please log in.");
            redirect(req, resp, "/login");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            forward(req, resp, "/WEB-INF/views/auth/forgot-password.jsp");
        }
    }
}

