package controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/medical-news")
public class MedicalNewsApiController extends BaseController {
    private static final String HEALTH_TOPICS_URL = "https://odphp.health.gov/myhealthfinder/api/v4/itemlist.json?Type=topic";
    private static final String TOPIC_DETAIL_URL = "https://odphp.health.gov/myhealthfinder/api/v4/topicsearch.json?TopicId=";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);
    private static final Pattern ACCESSIBLE_VERSION_PATTERN = Pattern.compile("\"AccessibleVersion\"\\s*:\\s*\"([^\"]+)\"");

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(6))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private volatile long cachedAtMillis;
    private volatile String cachedJson;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String topicId = req.getParameter("topicId");
            if (topicId != null && !topicId.isBlank()) {
                String topicJson = getTopicJson(topicId.trim());
                if ("true".equalsIgnoreCase(req.getParameter("redirect"))) {
                    resp.sendRedirect(extractAccessibleVersion(topicJson));
                } else {
                    resp.getWriter().write(topicJson);
                }
                return;
            }

            String json = getNewsJson();
            resp.getWriter().write(json);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"Unable to load health topics right now\"}");
        }
    }

    private String getNewsJson() throws Exception {
        long now = System.currentTimeMillis();
        String currentCache = cachedJson;
        if (currentCache != null && now - cachedAtMillis < CACHE_TTL.toMillis()) {
            return currentCache;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HEALTH_TOPICS_URL))
                .timeout(Duration.ofSeconds(8))
                .header("Accept", "application/json")
                .header("User-Agent", "MediCore-HMS/1.0")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("MyHealthfinder API returned HTTP " + response.statusCode());
        }

        String json = response.body();
        cachedJson = json;
        cachedAtMillis = now;
        return json;
    }

    private String getTopicJson(String topicId) throws Exception {
        if (!topicId.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid topic id");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TOPIC_DETAIL_URL + topicId))
                .timeout(Duration.ofSeconds(8))
                .header("Accept", "application/json")
                .header("User-Agent", "MediCore-HMS/1.0")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("MyHealthfinder topic API returned HTTP " + response.statusCode());
        }
        return response.body();
    }

    private String extractAccessibleVersion(String topicJson) throws IOException {
        Matcher matcher = ACCESSIBLE_VERSION_PATTERN.matcher(topicJson);
        if (!matcher.find()) {
            throw new IOException("Topic URL not found");
        }
        return matcher.group(1).replace("\\/", "/");
    }
}
