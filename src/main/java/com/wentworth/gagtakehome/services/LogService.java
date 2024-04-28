package com.wentworth.gagtakehome.services;

import com.wentworth.gagtakehome.services.dto.RequestLogDTO;
import com.wentworth.gagtakehome.services.dto.ResponseLogDTO;

public interface LogService {
    String createRequest(RequestLogDTO requestLogDTO);
    void updateResponse(ResponseLogDTO responseLogDTO);
}
