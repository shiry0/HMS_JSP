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
String searchKeyword = (String) request.getAttribute("searchKeyword");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="card">
    <div class="toolbar">
        <div class="toolbar-block">
            <span class="page-eyebrow">Doctor Workspace</span>
            <h1 class="section-title">Patient Records</h1>
            <p class="section-subtitle">Search diagnoses, symptoms, treatments, and patient names from your saved clinical notes.</p>
        </div>
        <form method="get" action="<%= request.getContextPath() %>/doctor/records" class="search-row">
            <input type="text" name="q" placeholder="Search records" value="<%= searchKeyword == null ? "" : searchKeyword %>">
            <button class="btn btn-secondary" type="submit">Search</button>
        </form>
    </div>
    <% if (records == null || records.isEmpty()) { %>
        <div class="empty-state">No patient records found.</div>
    <% } else { %>
        <div class="records-grid">
        <% for (MedicalRecord record : records) { %>
            <article class="record-card clinical-record-card">
                <div class="record-card-head">
                    <div>
                        <span class="info-label"><%= record.getRecordDate() %></span>
                        <h3><%= record.getDiagnosis() %></h3>
                    </div>
                    <div class="inline-actions">
                        <a class="btn btn-secondary" href="<%= request.getContextPath() %>/doctor/patient-record?patientId=<%= record.getPatientId() %>&apptId=<%= record.getApptId() %>">Open</a>
                        <form method="post" action="<%= request.getContextPath() %>/doctor/delete-record">
                            <input type="hidden" name="recordId" value="<%= record.getRecordId() %>">
                            <button class="btn btn-danger btn-delete" type="submit">Delete</button>
                        </form>
                    </div>
                </div>
                <p class="muted-copy"><strong>Patient:</strong> <%= record.getPatientName() %></p>
                <p class="muted-copy"><strong>Symptoms:</strong> <%= record.getSymptoms() == null || record.getSymptoms().isBlank() ? "-" : record.getSymptoms() %></p>
                <p class="muted-copy"><strong>Treatment:</strong> <%= record.getTreatment() %></p>
                <% if (record.getPrescriptions() != null && !record.getPrescriptions().isEmpty()) { %>
                    <ul class="plain-list">
                        <% for (Prescription prescription : record.getPrescriptions()) { %>
                            <li><strong><%= prescription.getMedicineName() %></strong> - <%= prescription.getDosage() %>, <%= prescription.getFrequency() %>, <%= prescription.getDuration() %></li>
                        <% } %>
                    </ul>
                <% } %>
            </article>
        <% } %>
        </div>
    <% } %>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
