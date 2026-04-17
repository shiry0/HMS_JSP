<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("pageTitle", "Login");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="auth-shell">
    <div class="auth-showcase">
        <div>
            <span class="pill">Portal Login</span>
            <h1 class="hero-title">Sign in to MediCore.</h1>
            <p class="hero-copy">Access appointments, records, and billing in one place.</p>
        </div>
        <div class="glass-card">
            <strong>Role-based access</strong>
            <p>Admins manage operations, doctors handle visits, and patients follow care.</p>
        </div>
    </div>
    <div class="auth-card">
        <span class="page-eyebrow">Portal Access</span>
        <h2 class="section-title">Welcome back to MediCore</h2>
        <p class="section-subtitle">Enter your email and password to continue.</p>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/login" class="stack-form">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required value="<%= request.getParameter("email") == null ? "" : request.getParameter("email") %>">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button class="btn btn-primary" type="submit">Login</button>
        </form>
        <p class="auth-links">
            <a href="<%= request.getContextPath() %>/forgot-password">Forgot password?</a>
            <a href="<%= request.getContextPath() %>/register">Create account</a>
        </p>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
