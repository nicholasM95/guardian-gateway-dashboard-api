package be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.domain.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateLogUseCase {

    private final LogRepository logRepository;

    public CreateLogUseCase(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void createLog(LogCreateRequest createLogRequest) {
        logRepository.save(LogFactory.createLog(createLogRequest));
    }
}
