package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        boolean isPublic = uri.equals(contextPath + "/")
                || uri.equals(contextPath + "/index.jsp")
                || uri.equals(contextPath + "/login")
                || uri.equals(contextPath + "/register")
                || uri.equals(contextPath + "/forgot-password")
                || uri.equals(contextPath + "/about.jsp")
                || uri.equals(contextPath + "/contact")
                || uri.equals(contextPath + "/contact.jsp")
                || uri.equals(contextPath + "/error403.jsp")
                || uri.equals(contextPath + "/error404.jsp")
                || uri.equals(contextPath + "/error500.jsp")
                || uri.startsWith(contextPath + "/css/")
                || uri.startsWith(contextPath + "/js/");

        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}

