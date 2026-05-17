<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("pageTitle", "About");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="public-page-hero">
    <span class="page-eyebrow"><span class="material-symbols-outlined">info</span> About MediCore</span>
    <h1 class="page-title">Simple hospital management for daily work.</h1>
    <p class="hero-copy">MediCore brings registration, scheduling, records, and billing into one Java web application.</p>
</section>

<section class="public-section-grid">
    <article class="public-panel prose-card">
        <span class="section-kicker"><span class="material-symbols-outlined">flag</span> Mission</span>
        <h2 class="section-title">Cut extra steps.</h2>
        <p>MediCore replaces scattered manual work with one cleaner system for staff and patients.</p>
        <p>The project uses Java EE, JSP, Servlets, CSS, JavaScript, and MySQL with separate roles for admin, doctor, and patient users.</p>
    </article>
    <article class="public-panel accent-panel">
        <span class="page-eyebrow"><span class="material-symbols-outlined">account_tree</span> Core Scope</span>
        <h2 class="section-title">Main modules.</h2>
        <div class="timeline">
            <div class="timeline-item">
                <span class="timeline-dot"></span>
                <div><h3>Access</h3><p class="muted-copy">Login, registration, and password reset.</p></div>
            </div>
            <div class="timeline-item">
                <span class="timeline-dot"></span>
                <div><h3>Clinical work</h3><p class="muted-copy">Doctors, appointments, and patient records.</p></div>
            </div>
            <div class="timeline-item">
                <span class="timeline-dot"></span>
                <div><h3>Billing</h3><p class="muted-copy">Bills, payments, and status tracking.</p></div>
            </div>
        </div>
    </article>
</section>

<section class="public-feature-grid">
    <article class="feature-card">
        <span class="feature-icon"><span class="material-symbols-outlined">favorite</span></span>
        <strong>Cardiology</strong>
        <p>Department-based scheduling for heart care.</p>
    </article>
    <article class="feature-card">
        <span class="feature-icon"><span class="material-symbols-outlined">psychology</span></span>
        <strong>Neurology</strong>
        <p>Visits, records, and follow-up planning.</p>
    </article>
    <article class="feature-card">
        <span class="feature-icon"><span class="material-symbols-outlined">accessibility_new</span></span>
        <strong>Orthopedics</strong>
        <p>Appointments and treatment tracking.</p>
    </article>
    <article class="feature-card accent">
        <span class="feature-icon"><span class="material-symbols-outlined">child_care</span></span>
        <strong>Pediatrics</strong>
        <p>Accessible care journeys for younger patients.</p>
    </article>
    <article class="feature-card">
        <span class="feature-icon"><span class="material-symbols-outlined">medical_services</span></span>
        <strong>General Medicine</strong>
        <p>Everyday intake, billing, and documentation.</p>
    </article>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
