<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Appointment"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="card">
    <span class="page-eyebrow">Patient Workspace</span>
    <h1 class="section-title">My Appointments</h1>
    <p class="section-subtitle">Check visit status and cancel upcoming bookings.</p>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>
    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Doctor</th>
                    <th>Department</th>
                    <th>Status</th>
                    <th>Reason</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (appointments == null || appointments.isEmpty()) { %>
                    <tr><td colspan="7">No appointments found.</td></tr>
                <% } else {
                    for (Appointment appointment : appointments) { %>
                    <tr>
                        <td><%= appointment.getApptDate() %></td>
                        <td><%= appointment.getApptTime() %></td>
                        <td><%= appointment.getDoctorName() %></td>
                        <td><%= appointment.getDeptName() %></td>
                        <td><span class="badge badge-<%= appointment.getStatus().toLowerCase() %>"><%= appointment.getStatus() %></span></td>
                        <td><%= appointment.getReason() %></td>
                        <td>
                            <% if (!"Completed".equalsIgnoreCase(appointment.getStatus()) && !"Cancelled".equalsIgnoreCase(appointment.getStatus())) { %>
                            <form method="post" action="<%= request.getContextPath() %>/patient/cancel">
                                <input type="hidden" name="apptId" value="<%= appointment.getApptId() %>">
                                <button class="btn btn-danger" type="submit">Cancel</button>
                            </form>
                            <% } %>
                        </td>
                    </tr>
                <%  }
                } %>
            </tbody>
        </table>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
