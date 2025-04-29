package com.backend.domicare.security.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.backend.domicare.dto.response.Message;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.security.service.CustomOAuth2SuccessHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private String clientId = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    private String clientSecret= "xxxxx";
    private String redirectUri= "http://localhost:8080/auth/callback";
    @GetMapping("/auth/callback")
    public ResponseEntity<?> exchangeAuthorizationCode(@RequestParam("code") String authorizationCode) {
        // Tạo tham số request body dưới dạng x-www-form-urlencoded
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");
    
        // Tạo các header cho yêu cầu POST
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
        // Tạo đối tượng HttpEntity chứa body và header
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
    
        try {
            // Gửi yêu cầu POST đến Google để nhận access token
            ResponseEntity<String> response = restTemplate.exchange(GOOGLE_TOKEN_URL, HttpMethod.POST, entity, String.class);
    
            // Lấy dữ liệu từ response
            try {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                String accessToken = jsonResponse.get("access_token").asText();
    
                // Dùng access token để lấy thông tin người dùng từ Google
                HttpHeaders userInfoHeaders = new HttpHeaders();
                userInfoHeaders.set("Authorization", "Bearer " + accessToken);
                HttpEntity<String> userInfoEntity = new HttpEntity<>(userInfoHeaders);
    
                // Lấy thông tin người dùng từ Google
                ResponseEntity<String> userInfoResponse = restTemplate.exchange(GOOGLE_USERINFO_URL, HttpMethod.GET, userInfoEntity, String.class);
                JsonNode userInfo = objectMapper.readTree(userInfoResponse.getBody());
    
                // Lấy thông tin người dùng (kiểm tra sự tồn tại của trường)
                String email = userInfo.has("email") ? userInfo.get("email").asText() : "Email not available";
                String name = userInfo.has("name") ? userInfo.get("name").asText() : "Name not available";
                String picture = userInfo.has("picture") ? userInfo.get("picture").asText() : "Picture not available";
                String locale = userInfo.has("locale") ? userInfo.get("locale").asText() : "Locale not available";
                String sub = userInfo.has("sub") ? userInfo.get("sub").asText() : "Sub not available";
    
                // Tạo LoginResponse và trả về kết quả
                LoginResponse loginResponse = customOAuth2SuccessHandler.handleOAuth2(email, name, picture, locale, sub);
                return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response: " + e.getMessage());
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new Message("Error exchanging authorization code: " + e.getResponseBodyAsString()));
        }
    }
    
}
