<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.MedicalRecord"%>
<%@ page import="model.Prescription"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
List<MedicalRecord> records = (List<MedicalRecord>) request.getAttribute("records");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="card">
    <span class="page-eyebrow">Patient Workspace</span>
    <h1 class="section-title">Medical Records</h1>
    <p class="section-subtitle">See diagnoses, treatment, and prescriptions.</p>
    <% if (records == null || records.isEmpty()) { %>
        <div class="empty-state">No medical records available.</div>
    <% } else {
        for (MedicalRecord record : records) { %>
        <article class="record-card">
            <h2><%= record.getDiagnosis() %></h2>
            <p class="muted-copy"><strong>Date:</strong> <%= record.getRecordDate() %></p>
            <p class="muted-copy"><strong>Doctor:</strong> <%= record.getDoctorName() %></p>
            <p class="muted-copy"><strong>Symptoms:</strong> <%= record.getSymptoms() %></p>
            <p class="muted-copy"><strong>Treatment:</strong> <%= record.getTreatment() %></p>
            <% if (record.getPrescriptions() != null && !record.getPrescriptions().isEmpty()) { %>
                <h3>Prescriptions</h3>
                <ul class="plain-list">
                    <% for (Prescription prescription : record.getPrescriptions()) { %>
                        <li><strong><%= prescription.getMedicineName() %></strong> - <%= prescription.getDosage() %>, <%= prescription.getFrequency() %>, <%= prescription.getDuration() %></li>
                    <% } %>
                </ul>
            <% } %>
        </article>
    <%  }
    } %>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
