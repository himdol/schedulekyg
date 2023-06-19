package com.schedule.schedulekyg.controller.menu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @GetMapping("/dashboard")
    public String showDashboardPage() {
        return "/index";
    }

    @GetMapping("/buttons")
    public String showButtonsPage() {
        return "/buttons";
    }

    @GetMapping("/cards")
    public String showCardsPage() {
        return "/cards";
    }

    @GetMapping("/color")
    public String showColorPage() {
        return "/utilities-color";
    }

    @GetMapping("/border")
    public String showBorderPage() {
        return "/utilities-border";
    }

    @GetMapping("/animation")
    public String showAnimationPage() {
        return "/utilities-animation";
    }

    @GetMapping("/other")
    public String showOtherPage() {
        return "/utilities-other";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "/register";
    }

    @GetMapping("/forgot-password")
    public String showForgetPasswordPage() {
        return "/forgot-password";
    }

    @GetMapping("/404")
    public String show404Page() {
        return "/404";
    }

    @GetMapping("/blank")
    public String showBlankPage() {
        return "/blank";
    }

    @GetMapping("/charts")
    public String showChartsPage() {
        return "/charts";
    }

    @GetMapping("/tables")
    public String showTablesPage() {
        return "/tables";
    }
}
