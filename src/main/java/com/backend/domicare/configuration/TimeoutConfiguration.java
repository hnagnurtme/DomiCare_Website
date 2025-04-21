package com.backend.domicare.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class TimeoutConfiguration implements WebMvcConfigurer {
    
    private static final Logger logger = LoggerFactory.getLogger(TimeoutConfiguration.class);
    
    @Value("${spring.mvc.async.request-timeout:10000}")
    private long requestTimeout;
    
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(requestTimeout);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TimeoutInterceptor())
                .addPathPatterns("/api/**");
    }
    
    // Interceptor to handle long-running requests
    private static class TimeoutInterceptor implements HandlerInterceptor {
        
        private static final Logger interceptorLogger = LoggerFactory.getLogger(TimeoutInterceptor.class);
        
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            request.setAttribute("requestStartTime", System.currentTimeMillis());
            return true;
        }
        
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                   Object handler, Exception ex) {
            Long startTime = (Long) request.getAttribute("requestStartTime");
            if (startTime != null) {
                long executionTime = System.currentTimeMillis() - startTime;
                if (executionTime > 3000) { // Log slow requests (over 3 seconds)
                    interceptorLogger.warn("Slow API request detected: {} {} - execution time: {} ms", 
                            request.getMethod(), request.getRequestURI(), executionTime);
                }
            }
        }
    }
}