package be.nicholasmeyers.guardiangatewaydashboardapi.log.adapter.repository;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.domain.Log;
import be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase.LogRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LogPersistenceFacade implements LogRepository {

    private final LogJpaRepository jpaRepository;

    public LogPersistenceFacade(LogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Log log) {
        this.jpaRepository.saveAndFlush(new LogJpaEntity(log));
    }
}
