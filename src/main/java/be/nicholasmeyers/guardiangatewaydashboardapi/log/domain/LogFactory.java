package be.nicholasmeyers.guardiangatewaydashboardapi.log.domain;

import be.nicholasmeyers.guardiangatewaydashboardapi.log.usecase.LogCreateRequest;

public class LogFactory {

    private LogFactory() {
    }

    public static Log createLog(LogCreateRequest logCreateRequest) {
        Log log = new Log(logCreateRequest);
        log.validate();
        return log;
    }
}
