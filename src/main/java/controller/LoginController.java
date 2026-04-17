package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import service.AuthService;

@WebServlet("/login")
public class LoginController extends BaseController {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forward(req, resp, "/WEB-INF/views/auth/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = authService.login(req.getParameter("email"), req.getParameter("password"));
            HttpSession session = req.getSession();
            session.setAttribute("loggedUser", user);
            session.setAttribute("role", user.getRole());

            if ("admin".equalsIgnoreCase(user.getRole())) {
                redirect(req, resp, "/admin/dashboard");
            } else if ("doctor".equalsIgnoreCase(user.getRole())) {
                redirect(req, resp, "/doctor/dashboard");
            } else {
                redirect(req, resp, "/patient/dashboard");
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            forward(req, resp, "/WEB-INF/views/auth/login.jsp");
        }
    }
}

