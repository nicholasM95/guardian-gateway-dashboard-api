package be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.domain.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateLogUseCaseTest {

    @InjectMocks
    private CreateLogUseCase createLogUseCase;

    @Mock
    private LogRepository logRepository;

    @Test
    void givenLogCreateRequest_whenCreateLog_thenVerifyLogSaveIsCalled() {
        // Given
        LogCreateRequest logCreateRequest = new LogCreateRequest("2025-07-28T21:53:33.894196710Z", "POST", "/test",
                "1.1.1.1", "201", 4L, "Safari", "api.test.be");

        // When
        createLogUseCase.createLog(logCreateRequest);

        // Then
        ArgumentCaptor<Log> logCaptor = ArgumentCaptor.forClass(Log.class);
        verify(logRepository).save(logCaptor.capture());

        assertThat(logCaptor.getValue().getTimestamp()).isEqualTo(1753739613L);
        assertThat(logCaptor.getValue().getMethod()).isEqualTo("POST");
        assertThat(logCaptor.getValue().getPath()).isEqualTo("/test");
        assertThat(logCaptor.getValue().getClientIp()).isEqualTo("1.1.1.1");
        assertThat(logCaptor.getValue().getStatusCode()).isEqualTo("201");
        assertThat(logCaptor.getValue().getDurationMs()).isEqualTo(4L);
        assertThat(logCaptor.getValue().getUserAgent()).isEqualTo("Safari");
        assertThat(logCaptor.getValue().getHost()).isEqualTo("api.test.be");
    }
}
