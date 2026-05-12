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
import controller.BaseController;

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
        boolean isAuthPage = uri.equals(contextPath + "/login")
                || uri.equals(contextPath + "/register")
                || uri.equals(contextPath + "/forgot-password");
        boolean isStaticAsset = uri.startsWith(contextPath + "/css/")
                || uri.startsWith(contextPath + "/js/")
                || uri.startsWith(contextPath + "/images/");

        if (!isStaticAsset) {
            preventBrowserCache(resp);
        }

        if (isAuthPage && session != null && session.getAttribute("loggedUser") instanceof User) {
            User user = (User) session.getAttribute("loggedUser");
            resp.sendRedirect(contextPath + BaseController.dashboardPathFor(user));
            return;
        }

        boolean isPublic = uri.equals(contextPath + "/")
                || uri.equals(contextPath + "/index.jsp")
                || isAuthPage
                || uri.equals(contextPath + "/api/medical-news")
                || uri.equals(contextPath + "/about.jsp")
                || uri.equals(contextPath + "/contact")
                || uri.equals(contextPath + "/contact.jsp")
                || uri.equals(contextPath + "/error403.jsp")
                || uri.equals(contextPath + "/error404.jsp")
                || uri.equals(contextPath + "/error500.jsp")
                || isStaticAsset;

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

    private void preventBrowserCache(HttpServletResponse resp) {
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
    }
}
