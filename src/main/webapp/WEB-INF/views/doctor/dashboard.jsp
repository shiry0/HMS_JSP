<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Doctor"%>
<%@ page import="model.Appointment"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
Doctor doctor = (Doctor) request.getAttribute("doctor");
List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="hero-panel">
    <span class="page-eyebrow">Doctor Workspace</span>
    <h1 class="hero-title">Welcome, Dr. <%= doctor.getFullName() %></h1>
    <p class="hero-copy"><%= doctor.getDeptName() %> / <%= doctor.getSpecialization() %>. Review today's appointments and records.</p>
    <div class="hero-actions">
        <a class="btn btn-secondary" href="<%= request.getContextPath() %>/doctor/appointments">Appointments</a>
    </div>
</section>

<section class="grid-two">
    <div class="metric-card">
        <span class="metric-card-icon"><span class="material-symbols-outlined">event_upcoming</span></span>
        <div class="stat-value"><%= appointments == null ? 0 : appointments.size() %></div>
        <div class="stat-label">Appointments Today</div>
    </div>
    <div class="metric-card">
        <span class="metric-card-icon"><span class="material-symbols-outlined">medical_services</span></span>
        <div class="stat-value"><%= doctor.getExperienceYrs() %> yrs</div>
        <div class="stat-label">Clinical Experience</div>
    </div>
</section>

<section class="card">
    <span class="section-kicker">Today's Schedule</span>
    <h2 class="section-title">Today's Appointments</h2>
    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Patient</th>
                    <th>Time</th>
                    <th>Reason</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (appointments == null || appointments.isEmpty()) { %>
                    <tr><td colspan="5">No appointments scheduled for today.</td></tr>
                <% } else {
                    for (Appointment appointment : appointments) { %>
                    <tr>
                        <td><%= appointment.getPatientName() %></td>
                        <td><%= appointment.getApptTime() %></td>
                        <td><%= appointment.getReason() %></td>
                        <td><span class="badge badge-<%= appointment.getStatus().toLowerCase() %>"><%= appointment.getStatus() %></span></td>
                        <td>
                            <div class="inline-actions">
                                <form method="post" action="<%= request.getContextPath() %>/doctor/update-appointment">
                                    <input type="hidden" name="apptId" value="<%= appointment.getApptId() %>">
                                    <input type="hidden" name="action" value="confirm">
                                    <button class="btn btn-warning" type="submit">Confirm</button>
                                </form>
                                <form method="post" action="<%= request.getContextPath() %>/doctor/update-appointment">
                                    <input type="hidden" name="apptId" value="<%= appointment.getApptId() %>">
                                    <input type="hidden" name="action" value="complete">
                                    <button class="btn btn-success" type="submit">Complete</button>
                                </form>
                                <a class="btn btn-secondary" href="<%= request.getContextPath() %>/doctor/patient-record?patientId=<%= appointment.getPatientId() %>&apptId=<%= appointment.getApptId() %>">Records</a>
                            </div>
                        </td>
                    </tr>
                <%  }
                } %>
            </tbody>
        </table>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
