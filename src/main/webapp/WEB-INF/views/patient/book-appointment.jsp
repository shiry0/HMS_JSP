<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Department"%>
<%@ page import="model.Doctor"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
List<Department> departments = (List<Department>) request.getAttribute("departments");
List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");
Integer selectedDeptId = (Integer) request.getAttribute("selectedDeptId");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="split-grid">
    <div class="hero-panel">
        <span class="page-eyebrow">Booking</span>
        <h1 class="section-title">Book your next appointment.</h1>
        <p class="hero-copy">Choose a doctor, date, and time, then add a reason.</p>
    </div>
    <div class="card">
        <span class="section-kicker">Appointment Form</span>
        <h2 class="section-title">Schedule a visit</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/patient/book-appointment" class="grid-form">
        <div class="form-group">
            <label for="deptId">Department</label>
            <select id="deptId" name="deptId">
                <option value="">-- Select Department --</option>
                <% for (Department department : departments) { %>
                    <option value="<%= department.getDeptId() %>" <%= selectedDeptId != null && department.getDeptId() == selectedDeptId ? "selected" : "" %>><%= department.getDeptName() %></option>
                <% } %>
            </select>
        </div>
        <div class="form-group">
            <label for="doctorId">Doctor</label>
            <select id="doctorId" name="doctorId" required <%= selectedDeptId == null || selectedDeptId == 0 ? "disabled" : "" %>>
                <option value="">-- Select Doctor --</option>
                <% if (selectedDeptId != null && selectedDeptId > 0) {
                    for (Doctor doctor : doctors) { %>
                        <option value="<%= doctor.getDoctorId() %>" data-available-days="<%= doctor.getAvailableDays() == null ? "" : doctor.getAvailableDays().replace("\"", "&quot;") %>">Dr. <%= doctor.getFullName() %> - <%= doctor.getSpecialization() %><%= doctor.getAvailableDays() == null || doctor.getAvailableDays().isBlank() ? "" : " (" + doctor.getAvailableDays() + ")" %></option>
                <%  }
                } %>
            </select>
            <p class="form-note" id="doctorAvailabilityNote"><%= selectedDeptId == null || selectedDeptId == 0 ? "Select a department first." : "Select a doctor to see available days." %></p>
        </div>
        <div class="form-group">
            <label>Appointment Date</label>
            <input type="date" name="apptDate" required>
        </div>
        <div class="form-group">
            <label>Appointment Time</label>
            <input type="time" name="apptTime" required>
        </div>
        <div class="form-group full-span">
            <label>Reason</label>
            <textarea name="reason" rows="4" required></textarea>
        </div>
        <div class="full-span">
            <button class="btn btn-primary" type="submit">Book Appointment</button>
        </div>
        </form>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
