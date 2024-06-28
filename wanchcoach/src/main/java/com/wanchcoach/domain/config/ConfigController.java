package com.wanchcoach.domain.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfigController {

    @Value("${config.name}")
    String name;
    @Value("${info.description}")
    String info;

    @GetMapping("/config")
    public ResponseEntity<String> config() {
        System.out.println(name);
        return ResponseEntity.ok(name+info);
    }
}
