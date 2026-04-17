<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Patient"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
List<Patient> patients = (List<Patient>) request.getAttribute("patients");
Patient editingPatient = (Patient) request.getAttribute("editingPatient");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="card">
    <div class="toolbar">
        <div class="toolbar-block">
            <span class="page-eyebrow">Admin Workspace</span>
            <h1 class="section-title">Manage Patients</h1>
            <p class="section-subtitle">Search and update patient records.</p>
        </div>
        <form method="get" action="<%= request.getContextPath() %>/admin/manage-patients" class="search-row">
            <input type="text" name="q" placeholder="Search patients" value="<%= request.getParameter("q") == null ? "" : request.getParameter("q") %>">
            <button class="btn btn-secondary" type="submit">Search</button>
        </form>
    </div>
</section>

<section class="grid-two">
    <div class="card">
        <span class="section-kicker"><%= editingPatient == null ? "New Patient" : "Editing Patient" %></span>
        <h2 class="section-title"><%= editingPatient == null ? "Add Patient" : "Edit Patient" %></h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %><%= editingPatient == null ? "/admin/add-patient" : "/admin/update-patient" %>" class="grid-form">
            <% if (editingPatient != null) { %>
                <input type="hidden" name="patientId" value="<%= editingPatient.getPatientId() %>">
            <% } %>
            <div class="form-group">
                <label>Full Name</label>
                <input type="text" name="fullName" required value="<%= editingPatient == null ? "" : editingPatient.getFullName() %>">
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required value="<%= editingPatient == null ? "" : editingPatient.getEmail() %>">
            </div>
            <div class="form-group">
                <label>Phone</label>
                <input type="text" name="phone" required value="<%= editingPatient == null ? "" : editingPatient.getPhone() %>">
            </div>
            <% if (editingPatient == null) { %>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            <% } %>
            <div class="form-group">
                <label>Date of Birth</label>
                <input type="date" name="dob" value="<%= editingPatient == null || editingPatient.getDob() == null ? "" : editingPatient.getDob() %>">
            </div>
            <div class="form-group">
                <label>Gender</label>
                <select name="gender">
                    <option value="">-- Select Gender --</option>
                    <option value="Male" <%= editingPatient != null && "Male".equals(editingPatient.getGender()) ? "selected" : "" %>>Male</option>
                    <option value="Female" <%= editingPatient != null && "Female".equals(editingPatient.getGender()) ? "selected" : "" %>>Female</option>
                    <option value="Other" <%= editingPatient != null && "Other".equals(editingPatient.getGender()) ? "selected" : "" %>>Other</option>
                </select>
            </div>
            <div class="form-group">
                <label>Blood Group</label>
                <input type="text" name="bloodGroup" value="<%= editingPatient == null ? "" : editingPatient.getBloodGroup() %>">
            </div>
            <div class="form-group">
                <label>Emergency Contact</label>
                <input type="text" name="emergencyContact" value="<%= editingPatient == null ? "" : editingPatient.getEmergencyContact() %>">
            </div>
            <div class="form-group full-span">
                <label>Address</label>
                <textarea name="address" rows="3"><%= editingPatient == null ? "" : editingPatient.getAddress() %></textarea>
            </div>
            <% if (editingPatient != null) { %>
            <div class="form-group full-span">
                <label><input type="checkbox" name="active" <%= editingPatient.isActive() ? "checked" : "" %>> Active Account</label>
            </div>
            <% } %>
            <div class="full-span inline-actions">
                <button class="btn btn-primary" type="submit"><%= editingPatient == null ? "Add Patient" : "Update Patient" %></button>
                <% if (editingPatient != null) { %>
                    <a class="btn btn-secondary" href="<%= request.getContextPath() %>/admin/manage-patients">Cancel Edit</a>
                <% } %>
            </div>
        </form>
    </div>

    <div class="card">
        <span class="section-kicker">Patient Directory</span>
        <h2 class="section-title">Patient List</h2>
        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Blood Group</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (patients == null || patients.isEmpty()) { %>
                        <tr><td colspan="6">No patients found.</td></tr>
                    <% } else {
                        for (Patient patient : patients) { %>
                        <tr>
                            <td><%= patient.getFullName() %></td>
                            <td><%= patient.getEmail() %></td>
                            <td><%= patient.getPhone() %></td>
                            <td><%= patient.getBloodGroup() == null ? "-" : patient.getBloodGroup() %></td>
                            <td><span class="badge <%= patient.isActive() ? "badge-confirmed" : "badge-cancelled" %>"><%= patient.isActive() ? "Active" : "Inactive" %></span></td>
                            <td>
                                <div class="inline-actions">
                                    <a class="btn btn-secondary" href="<%= request.getContextPath() %>/admin/manage-patients?editPatientId=<%= patient.getPatientId() %>">Edit</a>
                                    <form method="post" action="<%= request.getContextPath() %>/admin/delete-patient">
                                        <input type="hidden" name="patientId" value="<%= patient.getPatientId() %>">
                                        <button class="btn btn-danger btn-delete" type="submit">Delete</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    <%  }
                    } %>
                </tbody>
            </table>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
