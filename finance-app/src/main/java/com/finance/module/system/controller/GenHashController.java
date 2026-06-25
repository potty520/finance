package com.finance.module.system.controller;

import com.finance.common.response.Result;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class GenHashController {
    @Resource
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/encode")
    public Result<String> encode(@RequestBody Map<String, String> body) {
        return Result.success(passwordEncoder.encode(body.get("password")));
    }
}
