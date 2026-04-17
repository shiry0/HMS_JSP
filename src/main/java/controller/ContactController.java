package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import util.ValidationUtil;

@WebServlet("/contact")
public class ContactController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Contact");
        forward(req, resp, "/contact.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String message = req.getParameter("message");

        if (!ValidationUtil.isValidName(name)) {
            req.setAttribute("error", "Please enter a valid name.");
        } else if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("error", "Please enter a valid email address.");
        } else if (!ValidationUtil.isNotEmpty(message)) {
            req.setAttribute("error", "Message cannot be empty.");
        } else {
            req.setAttribute("success", "Thanks for contacting MediCore. We will get back to you soon.");
        }
        req.setAttribute("pageTitle", "Contact");
        forward(req, resp, "/contact.jsp");
    }
}

