package com.example.spacepeople.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "spaceClient", url = "http://api.open-notify.org")
public interface SpaceClient {

    @GetMapping("/astros.json")
    ResponseEntity<String> getAstros();
}