package com.example.service.me;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;

@Controller
@ResponseBody
class MeController {
    
    @GetMapping ("/me")
    Map<String,String> me (Principal principal) {
        return Map.of("name" , principal.getName());
    }
}
