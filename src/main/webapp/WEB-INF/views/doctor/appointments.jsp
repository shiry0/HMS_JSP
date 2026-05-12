<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Appointment"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
String searchKeyword = (String) request.getAttribute("searchKeyword");
String selectedStatus = (String) request.getAttribute("selectedStatus");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="card">
    <div class="toolbar">
        <div class="toolbar-block">
            <span class="page-eyebrow">Doctor Workspace</span>
            <h1 class="section-title">Appointments</h1>
            <p class="section-subtitle">Check today's visits or filter by date.</p>
        </div>
        <form method="get" action="<%= request.getContextPath() %>/doctor/appointments" class="search-row appointment-search">
            <input type="text" name="q" placeholder="Search patient or reason" value="<%= searchKeyword == null ? "" : searchKeyword %>">
            <select name="status">
                <option value="">All Status</option>
                <option value="Pending" <%= "Pending".equalsIgnoreCase(selectedStatus) ? "selected" : "" %>>Pending</option>
                <option value="Confirmed" <%= "Confirmed".equalsIgnoreCase(selectedStatus) ? "selected" : "" %>>Confirmed</option>
                <option value="Completed" <%= "Completed".equalsIgnoreCase(selectedStatus) ? "selected" : "" %>>Completed</option>
                <option value="Cancelled" <%= "Cancelled".equalsIgnoreCase(selectedStatus) ? "selected" : "" %>>Cancelled</option>
            </select>
            <input type="date" name="date" value="<%= request.getParameter("date") == null ? "" : request.getParameter("date") %>">
            <button class="btn btn-secondary" type="submit">Filter</button>
        </form>
    </div>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>
    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Patient</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Reason</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (appointments == null || appointments.isEmpty()) { %>
                    <tr><td colspan="6">No appointments found.</td></tr>
                <% } else {
                    for (Appointment appointment : appointments) {
                        boolean isPending = "Pending".equalsIgnoreCase(appointment.getStatus());
                        boolean isFinalStatus = "Completed".equalsIgnoreCase(appointment.getStatus())
                                || "Cancelled".equalsIgnoreCase(appointment.getStatus());
                    %>
                    <tr>
                        <td><%= appointment.getPatientName() %></td>
                        <td><%= appointment.getApptDate() %></td>
                        <td><%= appointment.getApptTime() %></td>
                        <td><%= appointment.getReason() %></td>
                        <td><span class="badge badge-<%= appointment.getStatus().toLowerCase() %>"><%= appointment.getStatus() %></span></td>
                        <td>
                            <div class="inline-actions">
                                <% if (isPending) { %>
                                <form method="post" action="<%= request.getContextPath() %>/doctor/update-appointment">
                                    <input type="hidden" name="apptId" value="<%= appointment.getApptId() %>">
                                    <input type="hidden" name="action" value="confirm">
                                    <button class="btn btn-warning" type="submit">Confirm</button>
                                </form>
                                <% } %>
                                <% if (!isFinalStatus) { %>
                                <form method="post" action="<%= request.getContextPath() %>/doctor/update-appointment">
                                    <input type="hidden" name="apptId" value="<%= appointment.getApptId() %>">
                                    <input type="hidden" name="action" value="complete">
                                    <button class="btn btn-success" type="submit">Complete</button>
                                </form>
                                <form method="post" action="<%= request.getContextPath() %>/doctor/cancel-appointment">
                                    <input type="hidden" name="apptId" value="<%= appointment.getApptId() %>">
                                    <button class="btn btn-danger btn-cancel-no-fee" type="submit">Cancel</button>
                                </form>
                                <% } %>
                                <a class="btn btn-secondary" href="<%= request.getContextPath() %>/doctor/patient-record?patientId=<%= appointment.getPatientId() %>&apptId=<%= appointment.getApptId() %>">Record</a>
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
