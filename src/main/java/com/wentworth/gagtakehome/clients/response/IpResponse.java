package com.wentworth.gagtakehome.clients.response;

public record IpResponse(
        String status,
        String countryCode,
        String country,
        String isp,
        String message
) {
}
