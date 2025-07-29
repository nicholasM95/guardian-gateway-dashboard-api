package be.nicholasmeyers.guardiangatewaydashboardapi.log.adapter.stream;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase.CreateLogUseCase;
import be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase.LogCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LogStreamConsumerTest {

    @InjectMocks
    private LogStreamConsumer consumer;

    @Mock
    private CreateLogUseCase createLogUseCase;

    @Test
    void givenMessage_whenOnMessage_thenVerifyCallCreateLogUseCase() {
        // Given
        MapRecord<String, String, String> message = new MapRecord<>() {
            @Override
            public MapRecord<String, String, String> withId(RecordId id) {
                return null;
            }

            @Override
            public <SK> MapRecord<SK, String, String> withStreamKey(SK key) {
                return null;
            }

            @Override
            public Iterator<Map.Entry<String, String>> iterator() {
                return null;
            }

            @Override
            public String getStream() {
                return "";
            }

            @Override
            public RecordId getId() {
                return RecordId.of("1753739613-0");
            }

            @Override
            public Map<String, String> getValue() {
                Map<String, String> map = new HashMap<>();
                map.put("timestamp", "2025-07-28T21:53:33.894196710Z");
                map.put("method", "POST");
                map.put("path", "/test");
                map.put("client_ip", "1.1.1.1");
                map.put("status_code", "201");
                map.put("duration_ms", "4");
                map.put("user_agent", "Safari");
                map.put("host", "api.test.be");
                return map;
            }
        };

        // When
        consumer.onMessage(message);

        // Then
        ArgumentCaptor<LogCreateRequest> logCreateRequestArgumentCaptor = ArgumentCaptor.forClass(LogCreateRequest.class);
        verify(createLogUseCase).createLog(logCreateRequestArgumentCaptor.capture());

        assertThat(logCreateRequestArgumentCaptor.getValue().timestamp()).isEqualTo("2025-07-28T21:53:33.894196710Z");
        assertThat(logCreateRequestArgumentCaptor.getValue().method()).isEqualTo("POST");
        assertThat(logCreateRequestArgumentCaptor.getValue().path()).isEqualTo("/test");
        assertThat(logCreateRequestArgumentCaptor.getValue().clientIp()).isEqualTo("1.1.1.1");
        assertThat(logCreateRequestArgumentCaptor.getValue().statusCode()).isEqualTo("201");
        assertThat(logCreateRequestArgumentCaptor.getValue().durationMs()).isEqualTo(4L);
        assertThat(logCreateRequestArgumentCaptor.getValue().userAgent()).isEqualTo("Safari");
        assertThat(logCreateRequestArgumentCaptor.getValue().host()).isEqualTo("api.test.be");
    }
}
