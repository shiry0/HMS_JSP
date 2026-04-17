<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Bill"%>
<%
if (session.getAttribute("loggedUser") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
List<Bill> bills = (List<Bill>) request.getAttribute("bills");
double outstandingAmount = 0.0;
if (bills != null) {
    for (Bill bill : bills) {
        outstandingAmount += Math.max(0, bill.getTotalAmount() - bill.getPaidAmount());
    }
}
%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="grid-two">
    <div class="metric-card">
        <span class="metric-card-icon"><span class="material-symbols-outlined">receipt_long</span></span>
        <div class="stat-value"><%= bills == null ? 0 : bills.size() %></div>
        <div class="stat-label">Bills Issued</div>
    </div>
    <div class="metric-card accent">
        <span class="metric-card-icon"><span class="material-symbols-outlined">payments</span></span>
        <div class="stat-value">Rs. <%= String.format("%.2f", outstandingAmount) %></div>
        <div class="stat-label">Outstanding Amount</div>
    </div>
</section>

<section class="card">
    <span class="page-eyebrow">Patient Workspace</span>
    <h1 class="section-title">Billing</h1>
    <p class="section-subtitle">Review charges and pay remaining balances.</p>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>
    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Bill Date</th>
                    <th>Consultation</th>
                    <th>Medicine</th>
                    <th>Test</th>
                    <th>Late Fee</th>
                    <th>Total</th>
                    <th>Paid</th>
                    <th>Status</th>
                    <th>Pay</th>
                </tr>
            </thead>
            <tbody>
                <% if (bills == null || bills.isEmpty()) { %>
                    <tr><td colspan="9">No bills available.</td></tr>
                <% } else {
                    for (Bill bill : bills) { %>
                    <tr>
                        <td><%= bill.getBillDate() %></td>
                        <td>Rs. <%= String.format("%.2f", bill.getConsultationFee()) %></td>
                        <td>Rs. <%= String.format("%.2f", bill.getMedicineFee()) %></td>
                        <td>Rs. <%= String.format("%.2f", bill.getTestFee()) %></td>
                        <td>Rs. <%= String.format("%.2f", bill.getLateFee()) %></td>
                        <td>Rs. <%= String.format("%.2f", bill.getTotalAmount()) %></td>
                        <td>Rs. <%= String.format("%.2f", bill.getPaidAmount()) %></td>
                        <td><span class="badge badge-<%= bill.getStatus().toLowerCase() %>"><%= bill.getStatus() %></span></td>
                        <td>
                            <% if (!"Paid".equalsIgnoreCase(bill.getStatus())) { %>
                            <form method="post" action="<%= request.getContextPath() %>/patient/pay" class="inline-actions">
                                <input type="hidden" name="billId" value="<%= bill.getBillId() %>">
                                <input type="number" step="0.01" min="0.01" name="amount" placeholder="Amount" required>
                                <button class="btn btn-primary" type="submit">Pay</button>
                            </form>
                            <% } %>
                        </td>
                    </tr>
                <%  }
                } %>
            </tbody>
        </table>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
