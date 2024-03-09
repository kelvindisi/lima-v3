package org.icipe.lima.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/email")
public class EmailTestController {
    @GetMapping("/send_html")
    public String sendHtmlMail(Model model) {
        model.addAttribute("name", "Kelvin");
        model.addAttribute("verificationLink", "https://example.com/verification");

        return "email/user_email_verification";
    }
}
