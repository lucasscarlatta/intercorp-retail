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

    @Schema(description = "Http status", example = "status")
    private HttpStatus status;

    @Schema(description = "Http status code")
    private int code;

    @Schema(description = "Server local date")
    private LocalDateTime timestamp;

    @Schema(description = "Error message")
    private String message;

    @Schema(description = "Error debug message")
    @JsonIgnore
    private String debugMessage;

    @Schema(description = "Error endpoint path")
    private String path;

    @Schema(description = "List of validation errors")
    private List<String> errors;
}
