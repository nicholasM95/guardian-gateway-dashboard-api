package be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.domain.Log;

public interface LogRepository {
    void save(Log log);
}
