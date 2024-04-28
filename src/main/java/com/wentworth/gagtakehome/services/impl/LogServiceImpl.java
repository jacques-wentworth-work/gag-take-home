package com.wentworth.gagtakehome.services.impl;

import com.wentworth.gagtakehome.jpa.RequestLogRepository;
import com.wentworth.gagtakehome.jpa.domain.RequestLog;
import com.wentworth.gagtakehome.services.LogService;
import com.wentworth.gagtakehome.services.dto.RequestLogDTO;
import com.wentworth.gagtakehome.services.dto.ResponseLogDTO;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static java.time.LocalDateTime.now;


@Service
public class LogServiceImpl implements LogService {
    private final RequestLogRepository requestLogRepository;

    public LogServiceImpl(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @Override
    public String createRequest(RequestLogDTO requestLogDTO) {
        RequestLog save = requestLogRepository.save(new RequestLog(requestLogDTO));
        return save.getId();
    }

    @Override
    public void updateResponse(ResponseLogDTO responseLogDTO) {
        requestLogRepository
                .findById(responseLogDTO.id())
                .map(requestLog -> {
                    requestLog.setResponseCode(responseLogDTO.responseCode());
                    Duration duration = Duration.between(requestLog.getRequestTimeStamp(), now());
                    requestLog.setDuration(duration.toMillis());
                    return requestLog;
                }).map(requestLogRepository::save);
    }
}
