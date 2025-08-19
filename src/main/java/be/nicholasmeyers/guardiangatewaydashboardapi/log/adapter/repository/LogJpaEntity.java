package be.nicholasmeyers.guardiangatewaydashboardapi.log.adapter.repository;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.domain.Log;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "log")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class LogJpaEntity {

    protected LogJpaEntity() {
    }

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private UUID id;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private Long timestamp;

    @Column(name = "method", nullable = false, updatable = false)
    private String method;

    @Column(name = "path", nullable = false, updatable = false)
    private String path;

    @Column(name = "client_ip", nullable = false, updatable = false)
    private String clientIp;

    @Column(name = "status_code", nullable = false, updatable = false)
    private String statusCode;

    @Column(name = "duration_ms", nullable = false, updatable = false)
    private Long durationMs;

    @Column(name = "user_agent", nullable = false, updatable = false)
    private String userAgent;

    @Column(name = "host", nullable = false, updatable = false)
    private String host;

    @Column(name = "request_status", nullable = false, updatable = false)
    private String requestStatus;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;

    protected LogJpaEntity(Log log) {
        this.id = UUID.randomUUID();
        this.timestamp = log.getTimestamp();
        this.method = log.getMethod();
        this.path = log.getPath();
        this.clientIp = log.getClientIp();
        this.statusCode = log.getStatusCode();
        this.durationMs = log.getDurationMs();
        this.userAgent = log.getUserAgent();
        this.host = log.getHost();
        this.requestStatus = log.getRequestStatus();
    }
}
