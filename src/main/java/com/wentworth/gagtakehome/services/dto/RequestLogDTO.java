package com.wentworth.gagtakehome.services.dto;

public record RequestLogDTO(
        String uri,
        String ipAddress,
        String countryCode,
        String isp) {
}
