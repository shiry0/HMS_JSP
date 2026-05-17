<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("pageTitle", "Contact");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="public-section-grid contact-layout">
    <div class="public-panel">
        <span class="page-eyebrow"><span class="material-symbols-outlined">mail</span> Contact</span>
        <h1 class="section-title">Contact MediCore.</h1>
        <p class="section-subtitle">Send a question, support request, or feedback.</p>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("success") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %>/contact" class="stack-form">
            <div class="form-group">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="message">Message</label>
                <textarea id="message" name="message" rows="5" required></textarea>
            </div>
            <button class="btn btn-primary" type="submit">Send Message</button>
        </form>
    </div>
    <div class="public-panel accent-panel">
        <span class="page-eyebrow"><span class="material-symbols-outlined">support_agent</span> Hospital Information</span>
        <h2 class="section-title">Quick support details.</h2>
        <div class="timeline">
            <div class="timeline-item">
                <span class="timeline-dot"></span>
                <div><h3>Address</h3><p class="muted-copy">MediCore Hospital, City Centre</p></div>
            </div>
            <div class="timeline-item">
                <span class="timeline-dot"></span>
                <div><h3>Phone</h3><p class="muted-copy">9800000000</p></div>
            </div>
            <div class="timeline-item">
                <span class="timeline-dot"></span>
                <div><h3>Email</h3><p class="muted-copy">support@medicore.com</p></div>
            </div>
            <div class="timeline-item">
                <span class="timeline-dot"></span>
                <div><h3>Working Hours</h3><p class="muted-copy">08:00 - 17:00</p></div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
