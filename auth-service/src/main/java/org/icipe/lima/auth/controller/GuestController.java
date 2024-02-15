package org.icipe.lima.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuestController {
    @GetMapping("/")
    public String home() {
        return "redirect:/auth/login";
    }
}
