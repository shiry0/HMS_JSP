<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%
User indexUser = (User) session.getAttribute("loggedUser");
if (indexUser != null) {
    if ("admin".equalsIgnoreCase(indexUser.getRole())) {
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        return;
    } else if ("doctor".equalsIgnoreCase(indexUser.getRole())) {
        response.sendRedirect(request.getContextPath() + "/doctor/dashboard");
        return;
    } else {
        response.sendRedirect(request.getContextPath() + "/patient/dashboard");
        return;
    }
}
request.setAttribute("pageTitle", "MediCore HMS");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="landing-hero">
    <div class="landing-hero-copy">
        <span class="page-eyebrow"><span class="material-symbols-outlined">clinical_notes</span> Hospital Workflow</span>
        <h1 class="hero-title">A simpler workspace for daily <span>hospital tasks.</span></h1>
        <p class="hero-copy">Manage appointments, records, and billing from one clear portal.</p>
        <div class="hero-actions">
            <a class="btn btn-primary" href="<%= request.getContextPath() %>/login"><span class="material-symbols-outlined">login</span> Enter Portal</a>
            <a class="btn btn-secondary" href="<%= request.getContextPath() %>/register"><span class="material-symbols-outlined">person_add</span> Register as Patient</a>
        </div>
    </div>
    <div class="dashboard-preview" aria-hidden="true">
        <div class="preview-sidebar">
            <span class="material-symbols-outlined">health_and_safety</span>
            <span class="material-symbols-outlined">home</span>
            <span class="material-symbols-outlined">event</span>
            <span class="material-symbols-outlined">groups</span>
            <span class="material-symbols-outlined">payments</span>
        </div>
        <div class="preview-screen">
            <div class="preview-topbar">
                <strong>Dashboard</strong>
                <span class="material-symbols-outlined">notifications</span>
            </div>
            <div class="preview-metrics">
                <div><strong>128</strong><span>Appointments</span></div>
                <div><strong>532</strong><span>Patients</span></div>
                <div><strong>$24,680</strong><span>Revenue</span></div>
            </div>
            <div class="preview-row"><span>09:00 AM</span><strong>John Doe</strong><em>Completed</em></div>
            <div class="preview-row"><span>10:30 AM</span><strong>Sarah Johnson</strong><em>Scheduled</em></div>
            <div class="preview-row"><span>12:00 PM</span><strong>Michael Brown</strong><em>Scheduled</em></div>
        </div>
    </div>
    <aside class="role-snapshot">
        <span class="section-kicker"><span class="material-symbols-outlined">hub</span> System Snapshot</span>
        <h2 class="section-title">Built for every role.</h2>
        <p class="section-subtitle">Each user sees the tools they need most.</p>
        <div class="role-card">
            <span class="role-icon blue"><span class="material-symbols-outlined">admin_panel_settings</span></span>
            <div><strong>Admin</strong><p>Manage staff, patients, departments, and revenue.</p></div>
            <span class="material-symbols-outlined">chevron_right</span>
        </div>
        <div class="role-card">
            <span class="role-icon green"><span class="material-symbols-outlined">stethoscope</span></span>
            <div><strong>Doctor</strong><p>Handle appointments, records, and prescriptions.</p></div>
            <span class="material-symbols-outlined">chevron_right</span>
        </div>
        <div class="role-card">
            <span class="role-icon violet"><span class="material-symbols-outlined">favorite</span></span>
            <div><strong>Patient</strong><p>Book visits, view records, and track bills.</p></div>
            <span class="material-symbols-outlined">chevron_right</span>
        </div>
    </aside>
</section>

<section class="medical-news-section">
    <div class="toolbar">
        <div class="toolbar-block">
            <span class="section-kicker"><span class="material-symbols-outlined">newspaper</span> Live Medical News</span>
            <h2 class="section-title">Current health research and care updates.</h2>
        </div>
        <a class="btn btn-secondary" href="<%= request.getContextPath() %>/news">View all news <span class="material-symbols-outlined">arrow_forward</span></a>
    </div>
    <div id="medicalNewsGrid" class="medical-news-grid" aria-live="polite">
        <article class="news-card news-card-loading">
            <span class="feature-icon"><span class="material-symbols-outlined">hourglass_empty</span></span>
            <strong>Loading health topics...</strong>
            <p>Fetching preventive care and wellness guidance.</p>
        </article>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
