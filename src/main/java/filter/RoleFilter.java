package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;

@WebFilter({ "/admin/*", "/doctor/*", "/patient/*" })
public class RoleFilter implements Filter {

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("loggedUser");
        String uri = req.getRequestURI();

        if (uri.contains("/admin/") && !"admin".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/error403.jsp");
            return;
        }
        if (uri.contains("/doctor/") && !"doctor".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/error403.jsp");
            return;
        }
        if (uri.contains("/patient/") && !"patient".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/error403.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}

