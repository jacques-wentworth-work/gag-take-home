package com.wentworth.gagtakehome.controllers;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.wentworth.gagtakehome.AbstractTest;
import com.wentworth.gagtakehome.controllers.resources.TransportResource;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;

import java.nio.file.Path;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 18080)
class HttpRequestTest extends AbstractTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPingPong() throws Exception {
        String ip = "127.0.0.1";
        stubFor(get(urlEqualTo("/json/" + ip))
                .willReturn(ok().withHeader("Content-Type", "application/json")
                        .withBody(aSuccessBody())));

        assertThat(this.restTemplate.getForObject("/files/ping",
                String.class)).isEqualTo("pong");
    }

    @Test
    void testParseSuccess() throws Exception {
        String ip = "127.0.0.1";
        stubFor(get(urlEqualTo("/json/" + ip))
                .willReturn(ok().withHeader("Content-Type", "application/json")
                        .withBody(aSuccessBody())));

        LinkedMultiValueMap<Object, Object> multipart = new LinkedMultiValueMap<>();
        multipart.add("file", file("good.txt"));

        ResponseEntity<List<TransportResource>> parseResult = restTemplate.exchange(
                "/files/parse",
                HttpMethod.POST,
                new HttpEntity<>(multipart, buildMultipartHeaders()),
                new ParameterizedTypeReference<List<TransportResource>>() {}
        );

        assertEquals(HttpStatusCode.valueOf(200), parseResult.getStatusCode());
        assertEquals(3, parseResult.getBody().size());

        assertEquals("John Smith", parseResult.getBody().get(0).name());
        assertEquals("Rides A Bike", parseResult.getBody().get(0).transport());
        assertEquals(12.1, parseResult.getBody().get(0).topSpeed());

        assertEquals("Mike Smith", parseResult.getBody().get(1).name());
        assertEquals("Drives an SUV", parseResult.getBody().get(1).transport());
        assertEquals(95.5, parseResult.getBody().get(1).topSpeed());

        assertEquals("Jenny Walters", parseResult.getBody().get(2).name());
        assertEquals("Rides A Scooter", parseResult.getBody().get(2).transport());
        assertEquals(15.3, parseResult.getBody().get(2).topSpeed());
    }

    @Test
    void testParseUnauthorised() throws Exception {
        String ip = "127.0.0.1";
        stubFor(get(urlEqualTo("/json/" + ip))
                .willReturn(ok().withHeader("Content-Type", "application/json")
                        .withBody(aSuccessUnauthorisedBody())));

        LinkedMultiValueMap<Object, Object> multipart = new LinkedMultiValueMap<>();
        multipart.add("file", file("good.txt"));

        ResponseEntity<String> parse = restTemplate.postForEntity("/files/parse",
                new HttpEntity<>(multipart, buildMultipartHeaders()),
                String.class);
        assertEquals(HttpStatusCode.valueOf(403), parse.getStatusCode());
    }

    @Test
    void testParseBadFile() throws Exception {
        String ip = "127.0.0.1";
        stubFor(get(urlEqualTo("/json/" + ip))
                .willReturn(ok().withHeader("Content-Type", "application/json")
                        .withBody(aSuccessBody())));

        LinkedMultiValueMap<Object, Object> multipart = new LinkedMultiValueMap<>();
        multipart.add("file", file("bad.txt"));

        ResponseEntity<String> parseResult = restTemplate.postForEntity("/files/parse",
                new HttpEntity<>(multipart, buildMultipartHeaders()),
                String.class);
        assertEquals(HttpStatusCode.valueOf(400), parseResult.getStatusCode());
    }

    @Test
    void testParseBadFileSkipValidation() throws Exception {
        String ip = "127.0.0.1";
        stubFor(get(urlEqualTo("/json/" + ip))
                .willReturn(ok().withHeader("Content-Type", "application/json")
                        .withBody(aSuccessBody())));

        LinkedMultiValueMap<Object, Object> multipart = new LinkedMultiValueMap<>();
        multipart.add("file", file("bad.txt"));

        ResponseEntity<String> parseResult = restTemplate.postForEntity("/files/parse?skipValidation=true",
                new HttpEntity<>(multipart, buildMultipartHeaders()),
                String.class);
        assertEquals(HttpStatusCode.valueOf(500), parseResult.getStatusCode());
    }


    private FileSystemResource file(String fileName) {
        return new FileSystemResource(Path.of("src", "test", "resources", fileName));
    }

    private HttpHeaders buildMultipartHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
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
                    "query": "127.0.0.1"
                }
                """;
    }

    private static @NotNull String aSuccessUnauthorisedBody() {
        return """
                {
                    "status": "success",
                    "country": "USA",
                    "countryCode": "USA",
                    "region": "USA",
                    "regionName": "America",
                    "city": "Camberley",
                    "zip": "GU16",
                    "lat": 51.3037,
                    "lon": -0.7371,
                    "timezone": "America/East",
                    "isp": "TalkTalk",
                    "org": "TalkTalk Communications Limited",
                    "as": "AS13285 TalkTalk Communications Limited",
                    "query": "127.0.0.1"
                }
                """;
    }
}
