<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("pageTitle", "403 Forbidden");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="center-panel card prose-card">
    <span class="page-eyebrow">403 Error</span>
    <h1 class="section-title">Access denied for this route.</h1>
    <p>You do not have permission to access this page. Return to a route available for your current account or head back to the public homepage.</p>
    <a class="btn btn-primary" href="<%= request.getContextPath() %>/index.jsp">Return Home</a>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
