<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Doctor"%>
<%@ page import="model.Department"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");
List<Department> departments = (List<Department>) request.getAttribute("departments");
Doctor editingDoctor = (Doctor) request.getAttribute("editingDoctor");
String selectedAvailableDays = editingDoctor == null || editingDoctor.getAvailableDays() == null ? "" : editingDoctor.getAvailableDays().toLowerCase();
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="page-header card">
    <span class="page-eyebrow">Admin Workspace</span>
    <h1 class="section-title">Doctor management</h1>
    <p class="section-subtitle">Add, update, and organize doctor records.</p>
</section>
<section class="card doctor-directory-card">
    <div class="toolbar">
        <div class="toolbar-block">
            <span class="section-kicker">Directory</span>
            <h2 class="section-title">Doctor Directory</h2>
            <p class="section-subtitle">Review staff details before making changes.</p>
        </div>
        <button class="btn btn-primary" type="button" id="openDoctorModal">
            <span class="material-symbols-outlined">add</span>
            Add Doctor
        </button>
    </div>
    <div class="table-wrap doctor-table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Department</th>
                    <th>Specialization</th>
                    <th>Fee</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (doctors == null || doctors.isEmpty()) { %>
                    <tr><td colspan="6">No doctors found.</td></tr>
                <% } else {
                    for (Doctor doctor : doctors) { %>
                    <tr>
                        <td><%= doctor.getFullName() %></td>
                        <td><%= doctor.getDeptName() %></td>
                        <td><%= doctor.getSpecialization() %></td>
                        <td>Rs. <%= String.format("%.2f", doctor.getConsultationFee()) %></td>
                        <td><span class="badge <%= doctor.isActive() ? "badge-confirmed" : "badge-cancelled" %>"><%= doctor.isActive() ? "Active" : "Inactive" %></span></td>
                        <td>
                            <div class="inline-actions">
                                <a class="btn btn-secondary" href="<%= request.getContextPath() %>/admin/manage-doctors?editDoctorId=<%= doctor.getDoctorId() %>">Edit</a>
                                <form method="post" action="<%= request.getContextPath() %>/admin/delete-doctor">
                                    <input type="hidden" name="doctorId" value="<%= doctor.getDoctorId() %>">
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
</section>
<div class="modal-backdrop doctor-modal <%= editingDoctor != null || request.getAttribute("error") != null ? "is-open" : "" %>" id="doctorModal" aria-hidden="<%= editingDoctor != null || request.getAttribute("error") != null ? "false" : "true" %>" <%= editingDoctor != null ? "data-reset-url=\"" + request.getContextPath() + "/admin/manage-doctors\"" : "" %>>
    <div class="modal-panel" role="dialog" aria-modal="true" aria-labelledby="doctorModalTitle">
        <div class="modal-header">
            <div>
                <span class="section-kicker"><%= editingDoctor == null ? "New Doctor" : "Editing Doctor" %></span>
                <h2 class="section-title" id="doctorModalTitle"><%= editingDoctor == null ? "Add Doctor" : "Edit Doctor" %></h2>
            </div>
            <% if (editingDoctor != null) { %>
                <a class="modal-close" href="<%= request.getContextPath() %>/admin/manage-doctors" aria-label="Close doctor form">
                    <span class="material-symbols-outlined">close</span>
                </a>
            <% } else { %>
                <button class="modal-close" type="button" data-modal-close aria-label="Close doctor form">
                    <span class="material-symbols-outlined">close</span>
                </button>
            <% } %>
        </div>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %><%= editingDoctor == null ? "/admin/add-doctor" : "/admin/update-doctor" %>" class="grid-form">
            <% if (editingDoctor != null) { %>
                <input type="hidden" name="doctorId" value="<%= editingDoctor.getDoctorId() %>">
            <% } %>
            <div class="form-group">
                <label>Full Name</label>
                <input type="text" name="fullName" required value="<%= editingDoctor == null ? "" : editingDoctor.getFullName() %>">
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required value="<%= editingDoctor == null ? "" : editingDoctor.getEmail() %>">
            </div>
            <div class="form-group">
                <label>Phone</label>
                <input type="text" name="phone" required value="<%= editingDoctor == null ? "" : editingDoctor.getPhone() %>">
            </div>
            <div class="form-group">
                <label>Department</label>
                <select name="deptId" required>
                    <option value="">-- Select Department --</option>
                    <% for (Department department : departments) { %>
                        <option value="<%= department.getDeptId() %>" <%= editingDoctor != null && department.getDeptId() == editingDoctor.getDeptId() ? "selected" : "" %>>
                            <%= department.getDeptName() %>
                        </option>
                    <% } %>
                </select>
            </div>
            <% if (editingDoctor == null) { %>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            <% } %>
            <div class="form-group">
                <label>Specialization</label>
                <input type="text" name="specialization" required value="<%= editingDoctor == null ? "" : editingDoctor.getSpecialization() %>">
            </div>
            <div class="form-group">
                <label>Qualification</label>
                <input type="text" name="qualification" required value="<%= editingDoctor == null ? "" : editingDoctor.getQualification() %>">
            </div>
            <div class="form-group">
                <label>Experience (Years)</label>
                <input type="number" name="experienceYrs" value="<%= editingDoctor == null ? 0 : editingDoctor.getExperienceYrs() %>">
            </div>
            <div class="form-group">
                <label>Consultation Fee</label>
                <input type="number" step="0.01" name="consultationFee" value="<%= editingDoctor == null ? 0 : editingDoctor.getConsultationFee() %>">
            </div>
            <div class="form-group full-span">
                <label>Available Days</label>
                <input type="hidden" id="availableDaysInput" name="availableDays" value="<%= editingDoctor == null ? "" : editingDoctor.getAvailableDays() %>">
                <div class="day-picker" data-target="availableDaysInput">
                    <label class="day-option"><input type="checkbox" value="Sunday" <%= selectedAvailableDays.contains("sun") ? "checked" : "" %>> Sunday</label>
                    <label class="day-option"><input type="checkbox" value="Monday" <%= selectedAvailableDays.contains("mon") || selectedAvailableDays.contains("weekday") ? "checked" : "" %>> Monday</label>
                    <label class="day-option"><input type="checkbox" value="Tuesday" <%= selectedAvailableDays.contains("tue") || selectedAvailableDays.contains("weekday") ? "checked" : "" %>> Tuesday</label>
                    <label class="day-option"><input type="checkbox" value="Wednesday" <%= selectedAvailableDays.contains("wed") || selectedAvailableDays.contains("weekday") ? "checked" : "" %>> Wednesday</label>
                    <label class="day-option"><input type="checkbox" value="Thursday" <%= selectedAvailableDays.contains("thu") || selectedAvailableDays.contains("weekday") ? "checked" : "" %>> Thursday</label>
                    <label class="day-option"><input type="checkbox" value="Friday" <%= selectedAvailableDays.contains("fri") || selectedAvailableDays.contains("weekday") ? "checked" : "" %>> Friday</label>
                    <label class="day-option"><input type="checkbox" value="Saturday" <%= selectedAvailableDays.contains("sat") ? "checked" : "" %>> Saturday</label>
                </div>
                <p class="form-note" id="availableDaysSummary">Select available days.</p>
            </div>
            <% if (editingDoctor != null) { %>
            <div class="form-group full-span">
                <label><input type="checkbox" name="active" <%= editingDoctor.isActive() ? "checked" : "" %>> Active Account</label>
            </div>
            <% } %>
            <div class="full-span inline-actions">
                <button class="btn btn-primary" type="submit"><%= editingDoctor == null ? "Add Doctor" : "Update Doctor" %></button>
                <% if (editingDoctor != null) { %>
                    <a class="btn btn-secondary" href="<%= request.getContextPath() %>/admin/manage-doctors">Cancel Edit</a>
                <% } %>
            </div>
        </form>
    </div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
