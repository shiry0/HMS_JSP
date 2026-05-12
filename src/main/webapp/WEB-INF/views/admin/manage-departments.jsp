<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Department"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
List<Department> departments = (List<Department>) request.getAttribute("departments");
Department editingDepartment = (Department) request.getAttribute("editingDepartment");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="page-header card">
    <span class="page-eyebrow">Admin Workspace</span>
    <h1 class="section-title">Department management</h1>
    <p class="section-subtitle">Keep departments clear for booking and doctor assignment.</p>
</section>
<section class="card admin-directory-card">
    <div class="toolbar">
        <div class="toolbar-block">
            <span class="section-kicker">Department List</span>
            <h2 class="section-title">Department List</h2>
        </div>
        <button class="btn btn-primary" type="button" id="openDepartmentModal">
            <span class="material-symbols-outlined">add</span>
            Add Department
        </button>
    </div>
    <div class="table-wrap admin-table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Department</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% if (departments == null || departments.isEmpty()) { %>
                    <tr><td colspan="3">No departments found.</td></tr>
                <% } else {
                    for (Department department : departments) { %>
                    <tr>
                        <td><%= department.getDeptName() %></td>
                        <td><%= department.getDescription() %></td>
                        <td>
                            <div class="inline-actions">
                                <a class="btn btn-secondary" href="<%= request.getContextPath() %>/admin/departments?editDeptId=<%= department.getDeptId() %>">Edit</a>
                                <form method="post" action="<%= request.getContextPath() %>/admin/delete-department">
                                    <input type="hidden" name="deptId" value="<%= department.getDeptId() %>">
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

<div class="modal-backdrop admin-modal <%= editingDepartment != null || request.getAttribute("error") != null ? "is-open" : "" %>" id="departmentModal" aria-hidden="<%= editingDepartment != null || request.getAttribute("error") != null ? "false" : "true" %>" <%= editingDepartment != null ? "data-reset-url=\"" + request.getContextPath() + "/admin/departments\"" : "" %>>
    <div class="modal-panel modal-panel-narrow" role="dialog" aria-modal="true" aria-labelledby="departmentModalTitle">
        <div class="modal-header">
            <div>
                <span class="section-kicker"><%= editingDepartment == null ? "New Department" : "Editing Department" %></span>
                <h2 class="section-title" id="departmentModalTitle"><%= editingDepartment == null ? "Add Department" : "Edit Department" %></h2>
            </div>
            <% if (editingDepartment != null) { %>
                <a class="modal-close" href="<%= request.getContextPath() %>/admin/departments" aria-label="Close department form">
                    <span class="material-symbols-outlined">close</span>
                </a>
            <% } else { %>
                <button class="modal-close" type="button" data-modal-close aria-label="Close department form">
                    <span class="material-symbols-outlined">close</span>
                </button>
            <% } %>
        </div>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="<%= request.getContextPath() %><%= editingDepartment == null ? "/admin/add-department" : "/admin/update-department" %>" class="stack-form">
            <% if (editingDepartment != null) { %>
                <input type="hidden" name="deptId" value="<%= editingDepartment.getDeptId() %>">
            <% } %>
            <div class="form-group">
                <label>Department Name</label>
                <input type="text" name="deptName" required value="<%= editingDepartment == null ? "" : editingDepartment.getDeptName() %>">
            </div>
            <div class="form-group">
                <label>Description</label>
                <textarea name="description" rows="4"><%= editingDepartment == null ? "" : editingDepartment.getDescription() %></textarea>
            </div>
            <div class="inline-actions">
                <button class="btn btn-primary" type="submit"><%= editingDepartment == null ? "Add Department" : "Update Department" %></button>
                <% if (editingDepartment != null) { %>
                    <a class="btn btn-secondary" href="<%= request.getContextPath() %>/admin/departments">Cancel Edit</a>
                <% } %>
            </div>
        </form>
    </div>
</div>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
