<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Patient"%>
<%@ page import="model.Appointment"%>
<%@ page import="model.Bill"%>
<%@ page import="model.Doctor"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
Patient patient = (Patient) request.getAttribute("patient");
List<Appointment> upcomingAppointments = (List<Appointment>) request.getAttribute("upcomingAppointments");
List<Bill> recentBills = (List<Bill>) request.getAttribute("recentBills");
List<Doctor> recentDoctors = (List<Doctor>) request.getAttribute("recentDoctors");
List<Doctor> searchResults = (List<Doctor>) request.getAttribute("searchResults");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="hero-panel">
    <span class="page-eyebrow">Patient Workspace</span>
    <h1 class="hero-title">Welcome, <%= patient.getFullName() %></h1>
    <p class="hero-copy">Book visits, check records, and track bills from one place.</p>
    <div class="hero-actions">
        <a class="btn btn-secondary" href="<%= request.getContextPath() %>/patient/book-appointment">Book Appointment</a>
        <a class="btn btn-secondary" href="<%= request.getContextPath() %>/patient/medical-records">Records</a>
        <a class="btn btn-ghost" href="<%= request.getContextPath() %>/patient/billing">Billing</a>
    </div>
</section>

<section class="card">
    <div class="toolbar">
        <div class="toolbar-block">
            <span class="section-kicker">Doctor Finder</span>
            <h2 class="section-title">Search Doctors</h2>
        </div>
        <form method="get" action="<%= request.getContextPath() %>/patient/dashboard" class="search-row">
            <input type="text" name="q" placeholder="Search by doctor, department, or specialization" value="<%= request.getAttribute("searchKeyword") == null ? "" : request.getAttribute("searchKeyword") %>">
            <button class="btn btn-secondary" type="submit">Search</button>
        </form>
    </div>
    <% if (searchResults != null) { %>
        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>Doctor</th>
                        <th>Department</th>
                        <th>Specialization</th>
                        <th>Fee</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (searchResults.isEmpty()) { %>
                        <tr><td colspan="4">No matching doctors found.</td></tr>
                    <% } else {
                        for (Doctor doctor : searchResults) { %>
                        <tr>
                            <td><%= doctor.getFullName() %></td>
                            <td><%= doctor.getDeptName() %></td>
                            <td><%= doctor.getSpecialization() %></td>
                            <td>Rs. <%= String.format("%.2f", doctor.getConsultationFee()) %></td>
                        </tr>
                    <%  }
                    } %>
                </tbody>
            </table>
        </div>
    <% } %>
</section>

<section class="grid-three">
    <div class="card">
        <span class="section-kicker">Appointments</span>
        <h2 class="section-title">Upcoming Appointments</h2>
        <% if (upcomingAppointments == null || upcomingAppointments.isEmpty()) { %>
            <div class="empty-state">No upcoming appointments.</div>
        <% } else {
            for (Appointment appointment : upcomingAppointments) { %>
            <div class="record-card">
                <h3><%= appointment.getApptDate() %></h3>
                <p class="muted-copy">Dr. <%= appointment.getDoctorName() %> - <%= appointment.getDeptName() %> - <%= appointment.getApptTime() %></p>
            </div>
        <%  }
        } %>
    </div>
    <div class="card">
        <span class="section-kicker">Billing</span>
        <h2 class="section-title">Recent Bills</h2>
        <% if (recentBills == null || recentBills.isEmpty()) { %>
            <div class="empty-state">No bills available.</div>
        <% } else {
            for (Bill bill : recentBills) { %>
            <div class="record-card">
                <h3>Rs. <%= String.format("%.2f", bill.getTotalAmount()) %></h3>
                <p class="muted-copy"><span class="badge badge-<%= bill.getStatus().toLowerCase() %>"><%= bill.getStatus() %></span></p>
            </div>
        <%  }
        } %>
    </div>
    <div class="card">
        <span class="section-kicker">Doctors</span>
        <h2 class="section-title">Recently Added Doctors</h2>
        <% if (recentDoctors == null || recentDoctors.isEmpty()) { %>
            <div class="empty-state">No recent doctors available.</div>
        <% } else {
            for (Doctor doctor : recentDoctors) { %>
            <div class="record-card">
                <h3>Dr. <%= doctor.getFullName() %></h3>
                <p class="muted-copy"><%= doctor.getDeptName() %> - <%= doctor.getSpecialization() %></p>
            </div>
        <%  }
        } %>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
