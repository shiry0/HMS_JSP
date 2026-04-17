<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("pageTitle", "500 Server Error");
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="center-panel card prose-card">
    <span class="page-eyebrow">500 Error</span>
    <h1 class="section-title">Something went wrong while processing your request.</h1>
    <p>The server hit an issue before the page could load completely. Return home and try again from a stable route.</p>
    <a class="btn btn-primary" href="<%= request.getContextPath() %>/index.jsp">Return Home</a>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
