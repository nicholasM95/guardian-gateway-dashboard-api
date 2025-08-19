package be.nicholasmeyers.guardiangatewaydashboardapi.log.adapter.repository;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.domain.Log;
import be.nicholasmeyers.guardiangatewaydashboardapi.log.domain.LogFactory;
import be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase.LogCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseTest
public class LogPersistenceFacadeTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LogPersistenceFacade logPersistenceFacade;

    @Test
    void givenLog_whenSave_thenLogIsSaved() {
        // Given
        LogCreateRequest logCreateRequest = new LogCreateRequest("2025-07-28T21:53:33.894196710Z", "POST", "/test",
                "https", 443L,
                "1.1.1.1", "201", 4L, "Safari", "api.test.be", "ALLOWED");
        Log log = LogFactory.createLog(logCreateRequest);

        // When
        logPersistenceFacade.save(log);

        // Then
        String sql = """
                    SELECT id, timestamp, method, path, schema, port, client_ip, status_code, duration_ms, user_agent, host, request_status, created_date, last_modified_date
                    FROM log WHERE client_ip = ?
                    """;
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, "1.1.1.1");

        assertThat(result).isNotNull();
        assertThat(result.get("id")).isNotNull();
        assertThat(result.get("timestamp")).isEqualTo(1753739613L);
        assertThat(result.get("method")).isEqualTo("POST");
        assertThat(result.get("path")).isEqualTo("/test");
        assertThat(result.get("schema")).isEqualTo("https");
        assertThat(result.get("port")).isEqualTo(443L);
        assertThat(result.get("client_ip")).isEqualTo("1.1.1.1");
        assertThat(result.get("status_code")).isEqualTo("201");
        assertThat(result.get("duration_ms")).isEqualTo(4L);
        assertThat(result.get("user_agent")).isEqualTo("Safari");
        assertThat(result.get("host")).isEqualTo("api.test.be");
        assertThat(result.get("request_status")).isEqualTo("ALLOWED");
        assertThat(result.get("created_date")).isNotNull();
        assertThat(result.get("last_modified_date")).isNotNull();
    }
}
