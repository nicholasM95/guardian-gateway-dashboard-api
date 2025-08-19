package be.nicholasmeyers.guardiangatewaydashboardapi.log.domain;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase.LogCreateRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LogFactoryTest {

    @Test
    void givenLogCreateRequest_whenCreateLog_thenLogCreated() {
        // Given
        LogCreateRequest logCreateRequest = new LogCreateRequest("2025-07-28T21:53:33.894196710Z", "POST", "/test",
                "1.1.1.1", "201", 4L, "Safari", "api.test.be", "ALLOWED");

        // When
        Log log = LogFactory.createLog(logCreateRequest);

        // Then
        assertThat(log.getTimestamp()).isEqualTo(1753739613L);
        assertThat(log.getMethod()).isEqualTo("POST");
        assertThat(log.getPath()).isEqualTo("/test");
        assertThat(log.getClientIp()).isEqualTo("1.1.1.1");
        assertThat(log.getStatusCode()).isEqualTo("201");
        assertThat(log.getDurationMs()).isEqualTo(4L);
        assertThat(log.getUserAgent()).isEqualTo("Safari");
        assertThat(log.getHost()).isEqualTo("api.test.be");
        assertThat(log.getRequestStatus()).isEqualTo("ALLOWED");
    }
}
