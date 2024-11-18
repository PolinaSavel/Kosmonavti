package com.example.spacepeople.service;

import com.example.spacepeople.model.RequestResult;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SpacePeopleService {

    private final String url = "http://api.open-notify.org/astros.json";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<RequestResult> getPeopleInSpace() {
        List<CompletableFuture<RequestResult>> futures = new ArrayList<>();

        futures.add(executeWithWebClient());          // Запуск запросов параллельно
        futures.add(executeWithRestTemplate());
        futures.add(executeWithHttpClient());
        futures.add(executeWithOpenFeign());

        return futures.stream()                     // Ожидание завершения всех запросов
                .map(CompletableFuture::join)
                .toList();
    }

    private CompletableFuture<RequestResult> executeWithRestTemplate() {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            restTemplate.getForObject(url, String.class);
            long executionTime = System.currentTimeMillis() - startTime;
            return new RequestResult("RestTemplate", executionTime);
        });
    }

    private CompletableFuture<RequestResult> executeWithWebClient() {
        WebClient webClient = webClientBuilder.build();

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            long executionTime = System.currentTimeMillis() - startTime;
            return new RequestResult("WebClient", executionTime);
        });
    }

    private CompletableFuture<RequestResult> executeWithHttpClient() {           //HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            long executionTime = System.currentTimeMillis() - startTime;
            return new RequestResult("HttpClient", executionTime);
        });
    }

    private CompletableFuture<RequestResult> executeWithOpenFeign() {              //OpenFeign
        SpaceClient client = Feign.builder()
                .decoder(new GsonDecoder())
                .target(SpaceClient.class, url);

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            client.getAstros();                                                  // Вызов метода Feign клиента
            long executionTime = System.currentTimeMillis() - startTime;
            return new RequestResult("OpenFeign", executionTime);
        });
    }
}