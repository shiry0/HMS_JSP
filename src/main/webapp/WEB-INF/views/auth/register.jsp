<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("pageTitle", "Register");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="auth-shell">
    <div class="auth-showcase">
        <div>
            <span class="pill">New patient onboarding</span>
            <h1 class="hero-title">Create your patient account.</h1>
            <p class="hero-copy">Register to book visits, view records, and track bills.</p>
        </div>
        <div class="glass-card">
            <strong>Patient access</strong>
            <p>Booking, records, billing, and profile updates in one place.</p>
        </div>
    </div>
    <div class="auth-card">
        <span class="page-eyebrow">Patient Registration</span>
        <h2 class="section-title">Open your MediCore account</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/register" class="stack-form">
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" required value="<%= request.getParameter("fullName") == null ? "" : request.getParameter("fullName") %>">
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required value="<%= request.getParameter("email") == null ? "" : request.getParameter("email") %>">
            </div>
            <div class="form-group">
                <label for="phone">Phone</label>
                <input type="text" id="phone" name="phone" maxlength="10" required value="<%= request.getParameter("phone") == null ? "" : request.getParameter("phone") %>">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <button class="btn btn-primary" type="submit">Register</button>
        </form>
        <p class="auth-links">
            <a href="<%= request.getContextPath() %>/login">Back to login</a>
        </p>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
