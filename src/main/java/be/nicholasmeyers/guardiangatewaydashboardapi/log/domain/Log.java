package be.nicholasmeyers.guardiangatewaydashboardapi.log.domain;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase.LogCreateRequest;

import java.time.ZonedDateTime;

public class Log {

    private final Long timestamp;
    private final String method;
    private final String path;
    private final String clientIp;
    private final String statusCode;
    private final Long durationMs;
    private final String userAgent;
    private final String host;

    protected Log(LogCreateRequest logCreateRequest) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(logCreateRequest.timestamp());
        this.timestamp = zonedDateTime.toInstant().getEpochSecond();
        this.method = logCreateRequest.method();
        this.path = logCreateRequest.path();
        this.clientIp = logCreateRequest.clientIp();
        this.statusCode = logCreateRequest.statusCode();
        this.durationMs = logCreateRequest.durationMs();
        this.userAgent = logCreateRequest.userAgent();
        this.host = logCreateRequest.host();
    }

    protected void validate() {
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getHost() {
        return host;
    }
}
