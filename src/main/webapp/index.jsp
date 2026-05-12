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
<section class="public-hero">
    <div class="hero-panel">
        <span class="page-eyebrow">Hospital Workflow</span>
        <h1 class="hero-title">A simpler workspace for daily hospital tasks.</h1>
        <p class="hero-copy">Manage appointments, records, and billing from one clear portal.</p>
        <div class="hero-actions">
            <a class="btn btn-secondary" href="<%= request.getContextPath() %>/login">Enter Portal</a>
            <a class="btn btn-ghost" href="<%= request.getContextPath() %>/register">Register as Patient</a>
        </div>
    </div>
    <div class="public-hero-visual card">
        <div>
            <span class="section-kicker">System Snapshot</span>
            <h2 class="section-title">Built for every role.</h2>
            <p class="section-subtitle">Each user sees the tools they need most.</p>
        </div>
        <div class="public-stats">
            <div class="glass-card">
                <strong>Admin</strong>
                <p>Manage staff, patients, departments, and revenue.</p>
            </div>
            <div class="glass-card">
                <strong>Doctor</strong>
                <p>Handle appointments, records, and prescriptions.</p>
            </div>
            <div class="glass-card">
                <strong>Patient</strong>
                <p>Book visits, view records, and track bills.</p>
            </div>
        </div>
    </div>
</section>

<section class="medical-news-section">
    <div class="toolbar">
        <div class="toolbar-block">
            <span class="section-kicker">Live Medical News</span>
            <h2 class="section-title">Current health research and care updates.</h2>
        </div>
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
