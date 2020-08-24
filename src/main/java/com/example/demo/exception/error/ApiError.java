package com.example.demo.exception.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {

    @Schema(name = "Http status", example = "status")
    private HttpStatus status;

    @Schema(name = "Http status code")
    private int code;

    @Schema(name = "Server local date")
    private LocalDateTime timestamp;

    @Schema(name = "Error message")
    private String message;

    @Schema(name = "Error debug message")
    @JsonIgnore
    private String debugMessage;

    @Schema(name = "Error endpoint path")
    private String path;

    @Schema(name = "List of validation errors")
    private List<String> errors;
}
