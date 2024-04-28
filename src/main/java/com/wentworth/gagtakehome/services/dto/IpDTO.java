package com.wentworth.gagtakehome.services.dto;

import com.wentworth.gagtakehome.clients.response.IpResponse;

public record IpDTO (
        String status,
        String countryCode,
        String country,
        String isp,
        String message
) {

    public IpDTO(IpResponse ipResponse) {
        this(ipResponse.status(), ipResponse.countryCode(), ipResponse.country(), ipResponse.isp(), ipResponse.message());
    }
}
