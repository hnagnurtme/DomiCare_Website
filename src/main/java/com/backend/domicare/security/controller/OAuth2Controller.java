package com.backend.domicare.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OAuth2Controller {
    
    @GetMapping("/oauth2/success")
    public String oauth2Success(@RequestParam("accessToken") String accessToken,
                                @RequestParam("refreshToken") String refreshToken,
                                Model model) {
        model.addAttribute("accessToken", accessToken);
        model.addAttribute("refreshToken", refreshToken);

        return "oauth2_success";
    }


}
