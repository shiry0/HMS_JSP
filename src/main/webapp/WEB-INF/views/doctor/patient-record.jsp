<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Patient"%>
<%@ page import="model.MedicalRecord"%>
<%@ page import="model.Prescription"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
Patient patient = (Patient) request.getAttribute("patient");
List<MedicalRecord> records = (List<MedicalRecord>) request.getAttribute("records");
Integer apptId = (Integer) request.getAttribute("apptId");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="page-header card">
    <span class="page-eyebrow">Patient Record</span>
    <h1 class="section-title"><%= patient == null ? "Clinical file" : patient.getFullName() %></h1>
    <p class="section-subtitle">View patient details, history, and new notes.</p>
</section>

<section class="grid-two">
    <div class="card">
        <span class="section-kicker">Overview</span>
        <h2 class="section-title">Patient Details</h2>
        <% if (patient != null) { %>
        <form method="post" action="<%= request.getContextPath() %>/doctor/update-patient-details" class="grid-form compact-form">
            <input type="hidden" name="patientId" value="<%= patient.getPatientId() %>">
            <input type="hidden" name="apptId" value="<%= apptId == null ? 0 : apptId %>">
            <div class="form-group">
                <label>Name</label>
                <input type="text" name="fullName" required value="<%= patient.getFullName() %>">
            </div>
            <div class="form-group">
                <label>DOB</label>
                <input type="date" name="dob" value="<%= patient.getDob() == null ? "" : patient.getDob() %>">
            </div>
            <div class="form-group">
                <label>Gender</label>
                <select name="gender">
                    <option value="">-- Select Gender --</option>
                    <option value="Male" <%= "Male".equals(patient.getGender()) ? "selected" : "" %>>Male</option>
                    <option value="Female" <%= "Female".equals(patient.getGender()) ? "selected" : "" %>>Female</option>
                    <option value="Other" <%= "Other".equals(patient.getGender()) ? "selected" : "" %>>Other</option>
                </select>
            </div>
            <div class="form-group">
                <label>Blood Group</label>
                <input type="text" name="bloodGroup" value="<%= patient.getBloodGroup() == null ? "" : patient.getBloodGroup() %>">
            </div>
            <div class="form-group">
                <label>Emergency Contact</label>
                <input type="text" name="emergencyContact" value="<%= patient.getEmergencyContact() == null ? "" : patient.getEmergencyContact() %>">
            </div>
            <div class="form-group">
                <label>Address</label>
                <input type="text" name="address" value="<%= patient.getAddress() == null ? "" : patient.getAddress() %>">
            </div>
            <div class="full-span">
                <button class="btn btn-primary" type="submit">Update Details</button>
            </div>
        </form>
        <% } else { %>
            <div class="empty-state">Patient details are unavailable for this visit.</div>
        <% } %>
    </div>
    <div class="hero-panel">
        <span class="page-eyebrow">Visit Context</span>
        <h2 class="section-title">Clinical note capture</h2>
        <p class="hero-copy">Add symptoms, treatment, and prescriptions in one screen.</p>
    </div>
</section>

<section class="card">
    <span class="section-kicker">History</span>
    <h2 class="section-title">Medical History</h2>
    <% if (records == null || records.isEmpty()) { %>
        <div class="empty-state">No records found for this patient yet.</div>
    <% } else {
        for (MedicalRecord record : records) { %>
        <article class="record-card">
            <div class="record-card-head">
                <div>
                    <h3><%= record.getDiagnosis() %></h3>
                    <p class="muted-copy"><strong>Date:</strong> <%= record.getRecordDate() %></p>
                </div>
                <form method="post" action="<%= request.getContextPath() %>/doctor/delete-record">
                    <input type="hidden" name="recordId" value="<%= record.getRecordId() %>">
                    <input type="hidden" name="patientId" value="<%= record.getPatientId() %>">
                    <button class="btn btn-danger btn-delete" type="submit">Delete</button>
                </form>
            </div>
            <p class="muted-copy"><strong>Symptoms:</strong> <%= record.getSymptoms() %></p>
            <p class="muted-copy"><strong>Treatment:</strong> <%= record.getTreatment() %></p>
            <% if (record.getPrescriptions() != null && !record.getPrescriptions().isEmpty()) { %>
                <h4>Prescriptions</h4>
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

<section class="card">
    <span class="section-kicker">Documentation</span>
    <h2 class="section-title">Add New Record</h2>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>
    <form method="post" action="<%= request.getContextPath() %>/doctor/add-record" class="stack-form">
        <input type="hidden" name="patientId" value="<%= patient == null ? 0 : patient.getPatientId() %>">
        <input type="hidden" name="apptId" value="<%= apptId == null ? 0 : apptId %>">
        <div class="form-group">
            <label>Diagnosis</label>
            <input type="text" name="diagnosis" required>
        </div>
        <div class="form-group">
            <label>Symptoms</label>
            <textarea name="symptoms" rows="3"></textarea>
        </div>
        <div class="form-group">
            <label>Treatment</label>
            <textarea name="treatment" rows="3" required></textarea>
        </div>
        <h3>Prescriptions</h3>
        <div class="table-wrap">
            <table id="rxTable">
                <thead>
                    <tr>
                        <th>Medicine</th>
                        <th>Dosage</th>
                        <th>Frequency</th>
                        <th>Duration</th>
                        <th>Instructions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><input type="text" name="medicineName"></td>
                        <td><input type="text" name="dosage"></td>
                        <td><input type="text" name="frequency"></td>
                        <td><input type="text" name="duration"></td>
                        <td><input type="text" name="instructions"></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="inline-actions">
            <button id="addRxRow" class="btn btn-secondary" type="button">Add Prescription Row</button>
            <button class="btn btn-primary" type="submit">Save Record</button>
        </div>
    </form>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
