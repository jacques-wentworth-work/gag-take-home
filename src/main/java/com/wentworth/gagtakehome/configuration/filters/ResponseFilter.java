package com.wentworth.gagtakehome.configuration.filters;

import com.wentworth.gagtakehome.services.LogService;
import com.wentworth.gagtakehome.services.dto.ResponseLogDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class ResponseFilter implements Filter {

    private final LogService logService;

    public ResponseFilter(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        logService.updateResponse(new ResponseLogDTO(
                httpServletResponse.getHeader("X-request-id"),
                httpServletResponse.getStatus()));
    }
}
