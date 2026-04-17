<%@ page import="model.User"%>
<%
User footerUser = (User) session.getAttribute("loggedUser");
boolean appLayout = footerUser != null;
%>
    <% if (appLayout) { %>
            </main>
            <footer class="app-footer">
                <p>MediCore HMS</p>
                <span>Editorial hospital operations interface for appointments, records, and billing.</span>
            </footer>
        </div>
    </div>
    <% } else { %>
            </div>
        </main>
        <footer class="site-footer">
            <div class="site-footer-inner">
                <div>
                    <p class="footer-brand">MediCore HMS</p>
                    <p class="footer-copy">A calmer, sharper interface for patient care, scheduling, and hospital coordination.</p>
                </div>
                <div class="footer-links">
                    <a href="<%= request.getContextPath() %>/about.jsp">About</a>
                    <a href="<%= request.getContextPath() %>/contact">Contact</a>
                    <a href="<%= request.getContextPath() %>/login">Portal Access</a>
                </div>
            </div>
        </footer>
    <% } %>
    <script src="<%= request.getContextPath() %>/js/main.js"></script>
</body>
</html>
