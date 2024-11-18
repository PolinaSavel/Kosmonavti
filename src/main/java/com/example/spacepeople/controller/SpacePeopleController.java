package com.example.spacepeople.controller;

import com.example.spacepeople.model.RequestResult;
import com.example.spacepeople.service.SpacePeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpacePeopleController {

    @Autowired
    private SpacePeopleService spacePeopleService;

    @GetMapping("/people-in-space")
    public void fetchPeople() {
        List<RequestResult> results = spacePeopleService.getPeopleInSpace();

        System.out.println("Результаты запросов:");
        results.forEach(result -> System.out.println(result.getClientName() + ": " + result.getExecutionTime() + " ms"));

        RequestResult fastest = results.stream().min((r1, r2) -> Long.compare(r1.getExecutionTime(), r2.getExecutionTime())).orElse(null);
        RequestResult slowest = results.stream().max((r1, r2) -> Long.compare(r1.getExecutionTime(), r2.getExecutionTime())).orElse(null);

        System.out.println("Самый долгий запрос: " + slowest.getClientName() + " (" + slowest.getExecutionTime() + " ms)");
        System.out.println("Самый быстрый запрос: " + fastest.getClientName() + " (" + fastest.getExecutionTime() + " ms)");
    }
}