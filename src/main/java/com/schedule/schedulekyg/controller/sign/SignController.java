package com.schedule.schedulekyg.controller.sign;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignController {

  @GetMapping("/login")
  public String showLoginPage() {
    return "/login";
  }
}
