package be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.domain.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateLogUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateLogUseCase.class);
    private final LogRepository logRepository;

    public CreateLogUseCase(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void createLog(LogCreateRequest createLogRequest) {
        log.info("Create log: {}", createLogRequest);
        logRepository.save(LogFactory.createLog(createLogRequest));
    }
}
