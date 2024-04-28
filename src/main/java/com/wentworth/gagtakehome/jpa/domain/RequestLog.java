package com.wentworth.gagtakehome.jpa.domain;

import com.wentworth.gagtakehome.services.dto.RequestLogDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String uri;
    private LocalDateTime requestTimeStamp;
    private Integer responseCode;
    private String ipAddress;
    private String countryCode;
    private String isp;
    private Long duration;

    public RequestLog() {
    }

    public RequestLog(RequestLogDTO requestLogDTO) {
        this();
        this.setRequestTimeStamp(LocalDateTime.now());
        this.setUri(requestLogDTO.uri());
        this.setIpAddress(requestLogDTO.ipAddress());
        this.setCountryCode(requestLogDTO.countryCode());
        this.setIsp(requestLogDTO.isp());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public LocalDateTime getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public void setRequestTimeStamp(LocalDateTime requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
