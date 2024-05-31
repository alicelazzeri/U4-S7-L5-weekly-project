package it.epicode.U4_S7_L5_weekly_project.payloads;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponseDTO (
        String message,
        HttpStatus httpStatus,
        LocalDateTime createdAt

        ) {
    public ExceptionResponseDTO(String message, HttpStatus httpStatus, LocalDateTime createdAt) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.createdAt = createdAt;
    }
}
