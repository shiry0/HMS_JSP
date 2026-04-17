<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("pageTitle", "404 Not Found");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="center-panel card prose-card">
    <span class="page-eyebrow">404 Error</span>
    <h1 class="section-title">The page could not be found.</h1>
    <p>The route you requested does not exist or may have moved. Use the main homepage to re-enter the correct MediCore flow.</p>
    <a class="btn btn-primary" href="<%= request.getContextPath() %>/index.jsp">Return Home</a>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
