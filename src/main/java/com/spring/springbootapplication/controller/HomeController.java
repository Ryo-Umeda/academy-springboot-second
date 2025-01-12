package com.spring.springbootapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class HomeController {  

    @GetMapping("/home")
    public String showHomePage(Model model) {
        model.addAttribute("message", "Hello, World!");
        return "home";  // home.html を返す
    }
}
