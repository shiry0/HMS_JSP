<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("pageTitle", "Reset Password");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="auth-shell">
    <div class="auth-showcase">
        <div>
            <span class="pill">Recovery Flow</span>
            <h1 class="hero-title">Reset your password.</h1>
            <p class="hero-copy">Use your email and phone number to restore access.</p>
        </div>
        <div class="glass-card">
            <strong>Quick verification</strong>
            <p>We verify your registered details before updating the password.</p>
        </div>
    </div>
    <div class="auth-card">
        <span class="page-eyebrow">Reset Password</span>
        <h2 class="section-title">Restore your MediCore access</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/forgot-password" class="stack-form">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="phone">Phone</label>
                <input type="text" id="phone" name="phone" maxlength="10" required>
            </div>
            <div class="form-group">
                <label for="newPassword">New Password</label>
                <input type="password" id="newPassword" name="newPassword" required>
            </div>
            <button class="btn btn-primary" type="submit">Reset Password</button>
        </form>
        <p class="auth-links">
            <a href="<%= request.getContextPath() %>/login">Back to login</a>
        </p>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
