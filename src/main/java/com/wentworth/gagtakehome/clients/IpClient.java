package com.wentworth.gagtakehome.clients;

import com.wentworth.gagtakehome.clients.response.IpResponse;

public interface IpClient {
    IpResponse getIpDetail(String ip);
}
