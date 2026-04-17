<%@ page import="model.User"%>
<%
User navUser = (User) session.getAttribute("loggedUser");
String role = navUser == null ? "" : navUser.getRole();
String currentPath = request.getRequestURI().substring(request.getContextPath().length());
boolean homeActive = "/".equals(currentPath) || "".equals(currentPath) || "/index.jsp".equals(currentPath);
%>
<% if (navUser == null) { %>
<header class="public-nav">
    <div class="public-nav-inner">
        <a class="nav-brand" href="<%= request.getContextPath() %>/index.jsp">
            <span class="brand-mark"><span class="material-symbols-outlined">health_and_safety</span></span>
            <span class="brand-copy">
                <strong>MediCore HMS</strong>
                <small>Hospital management reimagined</small>
            </span>
        </a>
        <nav class="nav-links">
            <a class="nav-link <%= homeActive ? "is-active" : "" %>" href="<%= request.getContextPath() %>/index.jsp">Home</a>
            <a class="nav-link <%= "/about.jsp".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/about.jsp">About</a>
            <a class="nav-link <%= "/contact".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/contact">Contact</a>
        </nav>
        <div class="nav-actions">
            <a class="btn btn-ghost" href="<%= request.getContextPath() %>/login">Login</a>
            <a class="btn btn-primary" href="<%= request.getContextPath() %>/register">Register</a>
        </div>
    </div>
</header>
<% } else { %>
<aside class="app-sidebar">
    <a class="sidebar-brand" href="<%= request.getContextPath() %>/<%= "admin".equalsIgnoreCase(role) ? "admin/dashboard" : ("doctor".equalsIgnoreCase(role) ? "doctor/dashboard" : "patient/dashboard") %>">
        <span class="brand-mark"><span class="material-symbols-outlined">health_and_safety</span></span>
        <span class="brand-copy">
            <strong>MediCore HMS</strong>
            <small><%= role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase() %> workspace</small>
        </span>
    </a>
    <p class="sidebar-section-label">Navigation</p>
    <nav class="sidebar-nav">
        <% if ("admin".equalsIgnoreCase(role)) { %>
            <a class="sidebar-link <%= "/admin/dashboard".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/admin/dashboard"><span class="material-symbols-outlined">dashboard</span><span>Dashboard</span></a>
            <a class="sidebar-link <%= "/admin/manage-doctors".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/admin/manage-doctors"><span class="material-symbols-outlined">medical_services</span><span>Doctors</span></a>
            <a class="sidebar-link <%= "/admin/manage-patients".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/admin/manage-patients"><span class="material-symbols-outlined">groups</span><span>Patients</span></a>
            <a class="sidebar-link <%= "/admin/departments".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/admin/departments"><span class="material-symbols-outlined">apartment</span><span>Departments</span></a>
        <% } else if ("doctor".equalsIgnoreCase(role)) { %>
            <a class="sidebar-link <%= "/doctor/dashboard".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/doctor/dashboard"><span class="material-symbols-outlined">dashboard</span><span>Dashboard</span></a>
            <a class="sidebar-link <%= "/doctor/appointments".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/doctor/appointments"><span class="material-symbols-outlined">event_upcoming</span><span>Appointments</span></a>
            <a class="sidebar-link <%= "/doctor/patient-record".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/doctor/appointments"><span class="material-symbols-outlined">description</span><span>Patient Records</span></a>
        <% } else { %>
            <a class="sidebar-link <%= "/patient/dashboard".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/patient/dashboard"><span class="material-symbols-outlined">dashboard</span><span>Dashboard</span></a>
            <a class="sidebar-link <%= "/patient/book-appointment".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/patient/book-appointment"><span class="material-symbols-outlined">event_available</span><span>Book Appointment</span></a>
            <a class="sidebar-link <%= "/patient/my-appointments".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/patient/my-appointments"><span class="material-symbols-outlined">event</span><span>My Appointments</span></a>
            <a class="sidebar-link <%= "/patient/medical-records".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/patient/medical-records"><span class="material-symbols-outlined">description</span><span>Medical Records</span></a>
            <a class="sidebar-link <%= "/patient/billing".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/patient/billing"><span class="material-symbols-outlined">payments</span><span>Billing</span></a>
            <a class="sidebar-link <%= "/patient/profile".equals(currentPath) ? "is-active" : "" %>" href="<%= request.getContextPath() %>/patient/profile"><span class="material-symbols-outlined">person</span><span>Profile</span></a>
        <% } %>
    </nav>
    <div class="sidebar-footer">
        <div class="sidebar-user-card">
            <div class="user-avatar"><%= navUser.getFullName().substring(0, 1).toUpperCase() %></div>
            <div>
                <strong><%= navUser.getFullName() %></strong>
                <span><%= role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase() %> account</span>
            </div>
        </div>
        <a class="btn btn-secondary btn-block" href="<%= request.getContextPath() %>/logout">Logout</a>
    </div>
</aside>
<% } %>
