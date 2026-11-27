package com.template.spring.common.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {
    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/")
    public ApplicationInfo info() {
        return new ApplicationInfo(applicationName, "/actuator/health");
    }

    public record ApplicationInfo(String application, String health) {
    }

}
