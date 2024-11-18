package com.example.spacepeople.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MyRestTemplateClient {

    private final RestTemplate restTemplate;

    public MyRestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getPeopleInSpace(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}