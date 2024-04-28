package com.wentworth.gagtakehome.clients.impl;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.wentworth.gagtakehome.AbstractTest;
import com.wentworth.gagtakehome.clients.response.IpResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WireMockTest(httpPort = 18080)
class IpClientImplTest extends AbstractTest {

    private IpClientImpl ipClient;

    @BeforeEach
    void setup() {
        ipClient = new IpClientImpl(WebClient.builder().baseUrl("http://localhost:18080").build());
    }

    @Test
    void testIpApiSuccess() {
        String ip = "10.11.12.13";
        stubFor(get(urlEqualTo("/json/" + ip))
                .willReturn(ok().withHeader("Content-Type", "application/json")
                        .withBody(aSuccessBody())));
        IpResponse ipDetail = ipClient.getIpDetail(ip);
        assertEquals("success", ipDetail.status());
        assertEquals("GB", ipDetail.countryCode());
        assertEquals("United Kingdom", ipDetail.country());
        assertEquals("TalkTalk", ipDetail.isp());
        assertNull(ipDetail.message());
    }

    @Test
    void testIpApiFailure() {
        String ip = "10.11.12.13";
        stubFor(get(urlEqualTo("/json/" + ip))
                .willReturn(ok().withHeader("Content-Type", "application/json")
                        .withBody(aFailureBody())));
        IpResponse ipDetail = ipClient.getIpDetail(ip);
        assertEquals("fail", ipDetail.status());
    }

    private static @NotNull String aSuccessBody() {
        return """
                {
                    "status": "success",
                    "country": "United Kingdom",
                    "countryCode": "GB",
                    "region": "ENG",
                    "regionName": "England",
                    "city": "Camberley",
                    "zip": "GU16",
                    "lat": 51.3037,
                    "lon": -0.7371,
                    "timezone": "Europe/London",
                    "isp": "TalkTalk",
                    "org": "TalkTalk Communications Limited",
                    "as": "AS13285 TalkTalk Communications Limited",
                    "query": "10.11.12.13"
                }
                """;
    }

    private static @NotNull String aFailureBody() {
        return """
                {
                    "status": "fail",
                    "message": "private range",
                    "query": "192.168.1.177"
                }
                """;
    }
}
