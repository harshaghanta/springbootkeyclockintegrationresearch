package com.sharshag.springbootkeyclockintegrationresearch;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    @PreAuthorize("hasRole('DemoClientAdminRole')")
    public String hello() {
        return "Hello from spring boot & keycloak";
    }

    @GetMapping("/hello2")
    // @PreAuthorize("hasRole('openid')")
    public String hello2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Hello from Admin";
    }

}
