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
        if (getLoggedUser(req) != null) {
            redirectLoggedUser(req, resp);
            return;
        }
        forward(req, resp, "/WEB-INF/views/auth/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = authService.login(req.getParameter("email"), req.getParameter("password"));
            HttpSession session = req.getSession();
            session.setAttribute("loggedUser", user);
            session.setAttribute("role", user.getRole());

            redirect(req, resp, dashboardPathFor(user));
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            forward(req, resp, "/WEB-INF/views/auth/login.jsp");
        }
    }
}

