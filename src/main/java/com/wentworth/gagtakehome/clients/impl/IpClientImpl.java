package com.wentworth.gagtakehome.clients.impl;

import com.wentworth.gagtakehome.clients.IpClient;
import com.wentworth.gagtakehome.clients.response.IpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class IpClientImpl implements IpClient {
    private final WebClient ipWebClient;

    private static final String IP_PATH = "/json/{ip}";

    public IpClientImpl(WebClient ipWebClient) {
        this.ipWebClient = ipWebClient;
    }

    @Override
    public IpResponse getIpDetail(String ip) {
        return ipWebClient.get()
                .uri(IP_PATH, ip)
                .retrieve()
                .bodyToMono(IpResponse.class)
                .block();
    }
}
