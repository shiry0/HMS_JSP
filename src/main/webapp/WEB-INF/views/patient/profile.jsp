<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Patient"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
Patient patient = (Patient) request.getAttribute("patient");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="split-grid">
    <div class="hero-panel">
        <span class="page-eyebrow">Profile</span>
        <h1 class="section-title">Keep your patient details current.</h1>
        <p class="hero-copy">Update your details and password here.</p>
    </div>
    <div class="card">
        <span class="section-kicker">Profile Form</span>
        <h2 class="section-title">My Profile</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/patient/update-profile" class="grid-form">
        <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="fullName" required value="<%= patient == null ? "" : patient.getFullName() %>">
        </div>
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" required value="<%= patient == null ? "" : patient.getEmail() %>">
        </div>
        <div class="form-group">
            <label>Phone</label>
            <input type="text" name="phone" required value="<%= patient == null ? "" : patient.getPhone() %>">
        </div>
        <div class="form-group">
            <label>Date of Birth</label>
            <input type="date" name="dob" value="<%= patient == null || patient.getDob() == null ? "" : patient.getDob() %>">
        </div>
        <div class="form-group">
            <label>Gender</label>
            <select name="gender">
                <option value="">-- Select Gender --</option>
                <option value="Male" <%= patient != null && "Male".equals(patient.getGender()) ? "selected" : "" %>>Male</option>
                <option value="Female" <%= patient != null && "Female".equals(patient.getGender()) ? "selected" : "" %>>Female</option>
                <option value="Other" <%= patient != null && "Other".equals(patient.getGender()) ? "selected" : "" %>>Other</option>
            </select>
        </div>
        <div class="form-group">
            <label>Blood Group</label>
            <input type="text" name="bloodGroup" value="<%= patient == null ? "" : patient.getBloodGroup() %>">
        </div>
        <div class="form-group">
            <label>Emergency Contact</label>
            <input type="text" name="emergencyContact" value="<%= patient == null ? "" : patient.getEmergencyContact() %>">
        </div>
        <div class="form-group full-span">
            <label>Address</label>
            <textarea name="address" rows="3"><%= patient == null ? "" : patient.getAddress() %></textarea>
        </div>
        <div class="form-group full-span">
            <label>New Password</label>
            <input type="password" name="newPassword" placeholder="Leave blank to keep current password">
        </div>
        <div class="full-span">
            <button class="btn btn-primary" type="submit">Update Profile</button>
        </div>
        </form>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
