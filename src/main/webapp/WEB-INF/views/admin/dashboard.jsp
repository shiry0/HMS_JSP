<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Appointment"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
List<Appointment> todayAppointments = (List<Appointment>) request.getAttribute("todayAppointments");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<style>
    .admin-appointments-section {
        margin-top: 3.5rem;
    }
</style>
<section class="hero-panel">
    <span class="page-eyebrow">Admin Control</span>
    <h2 class="hero-title">Clinical overview.</h2>
    <p class="hero-copy">See doctors, patients, appointments, and revenue at a glance.</p>
    <div class="hero-actions">
        <a class="btn btn-secondary" href="<%= request.getContextPath() %>/admin/manage-doctors">Doctors</a>
        <a class="btn btn-secondary" href="<%= request.getContextPath() %>/admin/manage-patients">Patients</a>
        <a class="btn btn-ghost" href="<%= request.getContextPath() %>/admin/departments">Departments</a>
    </div>
</section>

<section class="grid-four dashboard-metrics">
    <div class="metric-card">
        <span class="metric-card-icon"><span class="material-symbols-outlined">medical_services</span></span>
        <div class="stat-value"><%= request.getAttribute("doctorCount") %></div>
        <div class="stat-label">Total Doctors</div>
    </div>
    <div class="metric-card">
        <span class="metric-card-icon"><span class="material-symbols-outlined">groups</span></span>
        <div class="stat-value"><%= request.getAttribute("patientCount") %></div>
        <div class="stat-label">Total Patients</div>
    </div>
    <div class="metric-card">
        <span class="metric-card-icon"><span class="material-symbols-outlined">event_upcoming</span></span>
        <div class="stat-value"><%= request.getAttribute("todayAppointmentsCount") %></div>
        <div class="stat-label">Today's Appointments</div>
    </div>
    <div class="metric-card accent">
        <span class="metric-card-icon"><span class="material-symbols-outlined">payments</span></span>
        <div class="stat-value">Rs. <%= String.format("%.2f", request.getAttribute("totalRevenue")) %></div>
        <div class="stat-label">Total Revenue</div>
    </div>
</section>

<section class="card admin-appointments-section">
    <div class="toolbar">
        <div class="toolbar-block">
            <span class="section-kicker">Live schedule</span>
            <h2 class="section-title">Today's Appointments</h2>
            <p class="section-subtitle">Today's schedule across staff and patients.</p>
        </div>
        <div class="inline-actions">
            <a class="btn btn-primary" href="<%= request.getContextPath() %>/admin/manage-doctors">Doctors</a>
            <a class="btn btn-secondary" href="<%= request.getContextPath() %>/admin/manage-patients">Patients</a>
            <a class="btn btn-secondary" href="<%= request.getContextPath() %>/admin/departments">Departments</a>
        </div>
    </div>
    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Patient</th>
                    <th>Doctor</th>
                    <th>Department</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <% if (todayAppointments == null || todayAppointments.isEmpty()) { %>
                    <tr><td colspan="6">No appointments scheduled for today.</td></tr>
                <% } else {
                    for (Appointment appointment : todayAppointments) { %>
                    <tr>
                        <td><%= appointment.getPatientName() %></td>
                        <td><%= appointment.getDoctorName() %></td>
                        <td><%= appointment.getDeptName() %></td>
                        <td><%= appointment.getApptDate() %></td>
                        <td><%= appointment.getApptTime() %></td>
                        <td><span class="badge badge-<%= appointment.getStatus().toLowerCase() %>"><%= appointment.getStatus() %></span></td>
                    </tr>
                <%  }
                } %>
            </tbody>
        </table>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
