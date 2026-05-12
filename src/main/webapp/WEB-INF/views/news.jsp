<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<section class="medical-news-section">
    <div class="toolbar">
        <div class="toolbar-block">
            <span class="section-kicker">Live Medical News</span>
            <h2 class="section-title">Current health research and care updates.</h2>
        </div>
    </div>
    <div id="medicalNewsGrid" class="medical-news-grid" aria-live="polite">
        <article class="news-card news-card-loading">
            <span class="feature-icon"><span class="material-symbols-outlined">hourglass_empty</span></span>
            <strong>Loading health topics...</strong>
            <p>Fetching preventive care and wellness guidance.</p>
        </article>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />
