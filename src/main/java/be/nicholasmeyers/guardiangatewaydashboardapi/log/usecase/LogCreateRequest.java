package be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase;

public record LogCreateRequest(String timestamp, String method, String path, String schema, Long port,
                               String clientIp, String statusCode,
                               Long durationMs, String userAgent, String host, String requestStatus) {
}
