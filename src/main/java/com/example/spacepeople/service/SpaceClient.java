package com.example.spacepeople.service;

import feign.RequestLine;

public interface SpaceClient {
    @RequestLine("GET /astros.json")
    String getAstros();
}