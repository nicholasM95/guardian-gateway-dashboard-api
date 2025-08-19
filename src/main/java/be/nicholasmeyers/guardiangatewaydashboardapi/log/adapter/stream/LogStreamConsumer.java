package be.nicholasmeyers.guardiangatewaydashboardapi.log.adapter.stream;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase.CreateLogUseCase;
import be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase.LogCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class LogStreamConsumer implements StreamListener<String, MapRecord<String, String, String>> {

    private static final Logger log = LoggerFactory.getLogger(LogStreamConsumer.class);

    private final CreateLogUseCase createLogUseCase;

    private static final String STREAM_KEY = "access_logs";
    private static final String GROUP_NAME = "access_log_group";
    private static final String CONSUMER_NAME = "consumer_1";

    public LogStreamConsumer(CreateLogUseCase createLogUseCase) {
        this.createLogUseCase = createLogUseCase;
    }

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        try {
            String messageId = message.getId().getValue();
            Map<String, String> data = message.getValue();

            log.info("Received new message from stream '{}' met ID '{}'.",
                    message.getStream(),
                    messageId);

            String timestamp = data.get("timestamp");
            String method = data.get("method");
            String path = data.get("path");
            String clientIp = data.get("client_ip");
            String statusCode = data.get("status_code");
            Long durationMs = Long.valueOf(data.get("duration_ms"));
            String userAgent = data.get("user_agent");
            String host = data.get("host");
            String requestStatus = data.get("request_status");

            LogCreateRequest logCreateRequest = new LogCreateRequest(timestamp, method, path, clientIp, statusCode, durationMs, userAgent, host, requestStatus);
            createLogUseCase.createLog(logCreateRequest);

        } catch (Exception e) {
            log.error("Something went wrong with Redis Stream message with ID {}: {}", message.getId().getValue(), e.getMessage(), e);
        }
    }

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory) {

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofMillis(100))
                        .build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(redisConnectionFactory, options);

        try {
            redisConnectionFactory.getConnection().xGroupCreate(
                    STREAM_KEY.getBytes(),
                    GROUP_NAME,
                    ReadOffset.from("0-0"),
                    true
            );
            log.info("Redis consumer group '{}' for stream '{}' created.", GROUP_NAME, STREAM_KEY);
        } catch (Exception e) {
            log.warn("Can't create Redis consumer group '{}' for stream '{}'. Reason: {}.", GROUP_NAME, STREAM_KEY, e.getMessage());
        }
        Subscription subscription = container.receive(
                Consumer.from(GROUP_NAME, CONSUMER_NAME),
                StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed()), // Lees vanaf het laatst geconsumeerde bericht door deze groep
                this);
        container.start();

        log.info("Redis Stream Listener started for stream '{}' with consumer group '{}' and consumer '{}'", STREAM_KEY, GROUP_NAME, CONSUMER_NAME);
        return container;
    }
}
