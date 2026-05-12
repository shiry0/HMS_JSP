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
    <div class="modal-backdrop confirm-modal" id="confirmModal" aria-hidden="true">
        <div class="modal-panel modal-panel-confirm" role="dialog" aria-modal="true" aria-labelledby="confirmModalTitle">
            <div class="modal-header">
                <div>
                    <span class="section-kicker confirm-kicker">Warning</span>
                    <h2 class="section-title" id="confirmModalTitle">Confirm action</h2>
                </div>
                <button class="modal-close" type="button" id="cancelConfirmIcon" aria-label="Close warning">
                    <span class="material-symbols-outlined">close</span>
                </button>
            </div>
            <p class="modal-message" id="confirmModalMessage">This action cannot be undone.</p>
            <div class="form-group confirm-reason-field" id="confirmReasonField" hidden>
                <label for="confirmReasonInput">Cancellation Reason</label>
                <textarea id="confirmReasonInput" rows="3" placeholder="Tell the patient why this appointment is being cancelled."></textarea>
            </div>
            <div class="inline-actions modal-actions">
                <button class="btn btn-secondary" type="button" id="cancelConfirm">Cancel</button>
                <button class="btn btn-danger" type="button" id="acceptConfirm">Delete</button>
            </div>
        </div>
    </div>
    <script src="<%= request.getContextPath() %>/js/main.js?v=20260512-department-doctors"></script>
</body>
</html>
