package com.ihrm.common.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-06 14:26:48
 */
@Configuration
public class FeignConfiguration
{
    //配置feign拦截器，解决请求头问题
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                final HttpServletRequest request = attributes.getRequest();
                final Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        final String name = headerNames.nextElement();
                        final String values = request.getHeader(name);
                        requestTemplate.header(name, values);
                    }
                }
            }
        };
    }
}
