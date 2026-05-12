<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="model.User"%>
<%
String pageTitle = (String) request.getAttribute("pageTitle");
if (pageTitle == null || pageTitle.isBlank()) {
    pageTitle = "MediCore HMS";
}
String flashType = (String) session.getAttribute("flashType");
String flashMessage = (String) session.getAttribute("flashMessage");
if (flashMessage != null) {
    session.removeAttribute("flashType");
    session.removeAttribute("flashMessage");
}
User layoutUser = (User) session.getAttribute("loggedUser");
boolean authenticated = layoutUser != null;
String layoutRole = authenticated ? layoutUser.getRole() : "guest";
String todayLabel = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %></title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Manrope:wght@500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css?v=20260512-cancel-reason">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css?v=20260511-records-search">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/forms.css">
    <script>
        window.contextPath = "<%= request.getContextPath() %>";
    </script>
</head>
<body class="<%= authenticated ? "app-body role-" + layoutRole.toLowerCase() : "public-body" %>">
    <% if (authenticated) { %>
    <div class="app-shell">
        <jsp:include page="/WEB-INF/views/common/navbar.jsp" />
        <div class="app-frame">
            <header class="app-topbar">
                <div class="topbar-copy">
                    <span class="topbar-kicker"><%= layoutRole.toUpperCase() %> portal</span>
                    <h1 class="topbar-title"><%= pageTitle %></h1>
                    <p class="topbar-meta">Updated <%= todayLabel %></p>
                </div>
                <div class="topbar-user">
                    <div class="user-avatar"><%= layoutUser.getFullName().substring(0, 1).toUpperCase() %></div>
                    <div>
                        <strong><%= layoutUser.getFullName() %></strong>
                        <span><%= layoutRole.substring(0, 1).toUpperCase() + layoutRole.substring(1).toLowerCase() %> account</span>
                    </div>
                </div>
            </header>
            <main class="app-main">
                <% if (flashMessage != null) { %>
                    <div class="alert <%= "success".equals(flashType) ? "alert-success" : "alert-error" %>"><%= flashMessage %></div>
                <% } %>
    <% } else { %>
        <jsp:include page="/WEB-INF/views/common/navbar.jsp" />
        <main class="public-main">
            <div class="container">
                <% if (flashMessage != null) { %>
                    <div class="alert <%= "success".equals(flashType) ? "alert-success" : "alert-error" %>"><%= flashMessage %></div>
                <% } %>
    <% } %>
