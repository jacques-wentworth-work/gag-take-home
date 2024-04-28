package com.wentworth.gagtakehome.services.impl;

import com.wentworth.gagtakehome.clients.IpClient;
import com.wentworth.gagtakehome.clients.response.IpResponse;
import com.wentworth.gagtakehome.services.IpService;
import com.wentworth.gagtakehome.services.dto.IpDTO;
import org.springframework.stereotype.Service;

@Service
public class IpServiceImpl implements IpService {

    private final IpClient ipClient;

    public IpServiceImpl(IpClient ipClient) {
        this.ipClient = ipClient;
    }

    @Override
    public IpDTO getIpDetails(String ip) {
        IpResponse ipDetail = ipClient.getIpDetail(ip);
        if (ipDetail.status().equalsIgnoreCase("success")) {
            return new IpDTO(ipClient.getIpDetail(ip));
        } else {
            return new IpDTO(
                    ipDetail.status(),
                    "UKN",
                    "Unknown",
                    "Unknown",
                    ipDetail.message()
            );
        }
    }
}
