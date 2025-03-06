package com.backend.domicare.utils;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.backend.domicare.dto.response.RestResponse;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@SuppressWarnings("null") MethodParameter returnType, @SuppressWarnings({ "null", "rawtypes" }) Class converterType) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body, @SuppressWarnings("null") MethodParameter returnType, @SuppressWarnings("null") MediaType selectedContentType,
            @SuppressWarnings({ "null", "rawtypes" }) Class selectedConverterType, @SuppressWarnings("null") ServerHttpRequest request, @SuppressWarnings("null") ServerHttpResponse response) {
        
            // getStatus code
            HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
            int status = httpServletResponse.getStatus();
        
        @SuppressWarnings("rawtypes")
        RestResponse restResponse = new RestResponse();

        if( body instanceof String){
            return body;
        }

        else

        if( status >=400){
           return body;
        }

        else{
            restResponse.setStatus(status);
            restResponse.setMessage("Success");
            restResponse.setData(body);
        }

        return restResponse;
    }
    
}