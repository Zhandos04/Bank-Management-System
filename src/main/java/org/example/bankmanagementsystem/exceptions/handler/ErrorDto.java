package org.example.bankmanagementsystem.exceptions.handler;

import lombok.Data;

import java.time.LocalDateTime;

import static org.example.bankmanagementsystem.constants.ValueConstants.ZONE_ID;


@Data
public class ErrorDto {
    private String error;

    private String message;

    private String stackTrace;

    private long timestamp;

    public ErrorDto(String error, String message, String stackTrace) {
        this.error = error;
        this.message = message;
        this.stackTrace = stackTrace;

        this.timestamp = LocalDateTime.now(ZONE_ID).atZone(ZONE_ID).toEpochSecond();
    }
}