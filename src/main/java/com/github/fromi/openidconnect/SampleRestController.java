package com.github.fromi.openidconnect;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class SampleRestController {
    @RequestMapping("/test")
    public String getTestResult() {
        return "{\n  \"status\": \"OK\"\n}";
    }
    @RequestMapping("/admin")
    public String getAdminResult() {
        return "{\n  \"status\": \"OK\"\n}";
    }
}
