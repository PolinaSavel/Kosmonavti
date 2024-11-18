package com.example.spacepeople.model;

public class RequestResult {
    private String clientName;
    private long executionTime;

    public RequestResult(String clientName, long executionTime) {
        this.clientName = clientName;
        this.executionTime = executionTime;
    }

    public String getClientName() {
        return clientName;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}