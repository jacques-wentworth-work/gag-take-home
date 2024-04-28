package com.wentworth.gagtakehome.configuration;

import com.wentworth.gagtakehome.configuration.filters.RequestFilter;
import com.wentworth.gagtakehome.configuration.filters.ResponseFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final RequestFilter requestFilter;
    private final ResponseFilter responseFilter;

    public FilterConfig(RequestFilter requestFilter, ResponseFilter responseFilter) {
        this.requestFilter = requestFilter;
        this.responseFilter = responseFilter;
    }

    @Bean
    public FilterRegistrationBean<RequestFilter> registerRequestFilter() {
        FilterRegistrationBean<RequestFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(requestFilter);
        filterRegistrationBean.addUrlPatterns("/files/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<ResponseFilter> registerResponseFilter() {
        FilterRegistrationBean<ResponseFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(responseFilter);
        filterRegistrationBean.addUrlPatterns("/files/*");
        return filterRegistrationBean;
    }

}
