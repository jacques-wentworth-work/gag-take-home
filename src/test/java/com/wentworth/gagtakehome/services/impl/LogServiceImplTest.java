package com.wentworth.gagtakehome.services.impl;

import com.wentworth.gagtakehome.AbstractTest;
import com.wentworth.gagtakehome.jpa.RequestLogRepository;
import com.wentworth.gagtakehome.jpa.domain.RequestLog;
import com.wentworth.gagtakehome.services.LogService;
import com.wentworth.gagtakehome.services.dto.RequestLogDTO;
import com.wentworth.gagtakehome.services.dto.ResponseLogDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LogServiceImplTest extends AbstractTest {

    @Autowired
    LogService logService;

    @Autowired
    RequestLogRepository requestLogRepository;

    @Test
    void testLogRequest() {
        String uri = "/uri";
        String ipAddress = "11.22.33.44";
        String countryCode = "GB";
        String isp = "isp1";
        String requestId = logService.createRequest(new RequestLogDTO(uri, ipAddress, countryCode, isp));

        Optional<RequestLog> optionalRequestLog = requestLogRepository.findById(requestId);
        assertTrue(optionalRequestLog.isPresent());
        RequestLog requestLog = optionalRequestLog.get();
        assertEquals(uri, requestLog.getUri());
        assertEquals(ipAddress, requestLog.getIpAddress());
        assertEquals(countryCode, requestLog.getCountryCode());
        assertEquals(isp, requestLog.getIsp());
        assertNotNull(requestLog.getRequestTimeStamp());
        assertNull(requestLog.getResponseCode());
        assertNull(requestLog.getDuration());

        logService.updateResponse(new ResponseLogDTO(requestId, 200));
        optionalRequestLog = requestLogRepository.findById(requestId);
        assertTrue(optionalRequestLog.isPresent());
        requestLog = optionalRequestLog.get();
        assertEquals(200, requestLog.getResponseCode());
        assertNotNull(requestLog.getDuration());
    }
}
