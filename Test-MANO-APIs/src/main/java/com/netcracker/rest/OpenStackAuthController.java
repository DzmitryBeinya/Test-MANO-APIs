package com.netcracker.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v3")
public class OpenStackAuthController {
    @RequestMapping(value = "/auth/tokens",method = RequestMethod.GET)
    public String getAuthTokens() {
        System.out.println("/auth/tokens");
        return "/auth/tokens";
    }
}
