package com.backend.domicare.configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        Logger logger = LoggerFactory.getLogger(CorsConfiguration.class);
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                

                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4000", "http://localhost:4001")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
                        logger.info("CORS configuration applied for allowed origins: http://localhost:4000, http://localhost:4001");
            }

           
        };
    }
}