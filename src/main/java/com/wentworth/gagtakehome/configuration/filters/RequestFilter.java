package com.wentworth.gagtakehome.configuration.filters;

import com.wentworth.gagtakehome.services.IpService;
import com.wentworth.gagtakehome.services.LogService;
import com.wentworth.gagtakehome.services.dto.IpDTO;
import com.wentworth.gagtakehome.services.dto.RequestLogDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import java.io.IOException;
import java.util.*;

@Component
@Order(1)
public class RequestFilter implements Filter {
    private final IpService ipService;
    private final LogService logService;
    private final MultipartResolver multipartResolver;

    private static final List<String> blockedCountries = List.of("china", "spain", "usa");
    private static final List<String> blockedISPs = List.of("aws", "gcp", "azure");

    public RequestFilter(IpService ipService, LogService logService, MultipartResolver multipartResolver) {
        this.ipService = ipService;
        this.logService = logService;
        this.multipartResolver = multipartResolver;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        IpDTO ipDTO = ipService.getIpDetails(servletRequest.getRemoteAddr());

        if (isValidIp(ipDTO)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

            String requestId = logService.createRequest(new RequestLogDTO(
                    ((HttpServletRequest) servletRequest).getRequestURI(),
                    servletRequest.getRemoteAddr(),
                    ipDTO.countryCode(),
                    ipDTO.isp()));

            HttpServletRequestWrapper wrappedRequest = getHttpServletRequestWrapper(httpServletRequest, requestId);

            if (multipartResolver.isMultipart(wrappedRequest)) {
                MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart(wrappedRequest);
                filterChain.doFilter(multipartRequest, servletResponse);
            } else {
                filterChain.doFilter(wrappedRequest, servletResponse);
            }
        } else {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private static HttpServletRequestWrapper getHttpServletRequestWrapper(HttpServletRequest httpServletRequest, String requestId) {
        return new HttpServletRequestWrapper(httpServletRequest) {
            private final Map<String, String> addHeaders = Map.of("X-request-id", requestId);

            @Override
            public String getHeader(String name) {
                String headerValue = addHeaders.get(name);
                if (headerValue != null) {
                    return headerValue;
                }
                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if (addHeaders.containsKey(name)) {
                    return Collections.enumeration(Arrays.asList(addHeaders.get(name)));
                }
                return super.getHeaders(name);
            }
        };
    }

    private boolean isValidIp(IpDTO ipDTO) {
        if (blockedCountries.contains(ipDTO.country().toLowerCase())) {
            return false;
        }
        if (blockedISPs.contains(ipDTO.isp().toLowerCase())) {
            return false;
        }

        return true;
    }
}
