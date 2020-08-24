package com.example.demo.exception.error;

import com.example.demo.exception.UnprocessableEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Order
@RestControllerAdvice
public class WebRestControllerError {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebRestControllerError.class);

    @ExceptionHandler({HttpMessageNotReadableException.class})
    protected ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                    WebRequest request) {
        LOGGER.error("> handleUnprocessableEntityException");
        LOGGER.error("- Exception: ", ex);
        ApiError apiError = createApiError(UNPROCESSABLE_ENTITY, "Malformed JSON request", ex, request);

        LOGGER.error("< handleUnprocessableEntityException");

        return new ResponseEntity<>(apiError, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(NOT_ACCEPTABLE)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                    WebRequest request) {

        LOGGER.error("> handleMethodArgumentNotValid");
        LOGGER.error("- Exception: ", ex);

        ApiError apiError = createApiError(NOT_ACCEPTABLE, ex.getMessage(), ex, request);

        LOGGER.error("< handleMethodArgumentNotValid");

        return new ResponseEntity<>(apiError, NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ResponseEntity<ApiError> handleUnprocessableEntityException(UnprocessableEntityException ex,
                                                                       WebRequest request) {
        LOGGER.error("> handleUnprocessableEntityException");
        LOGGER.error("- Exception: ", ex);

        ApiError apiError = createApiError(UNPROCESSABLE_ENTITY, ex.getMessage(), ex, request);

        LOGGER.error("< handleUnprocessableEntityException");

        return new ResponseEntity<>(apiError, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(PRECONDITION_FAILED)
    public ResponseEntity<ApiError> handleArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                    WebRequest request) {
        LOGGER.error("> handleArgumentNotValidException");
        LOGGER.error("- Exception: ", ex);

        ApiError apiError = createApiError(PRECONDITION_FAILED, "Validation Error", ex, request);

        LOGGER.error("< handleArgumentNotValidException");

        return new ResponseEntity<>(apiError, PRECONDITION_FAILED);
    }

    private ApiError createApiError(HttpStatus status, String message, Exception ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ApiErrorBuilder builder = new ApiErrorBuilder();
        builder.setStatus(status).setCode(status.value()).setMessage(message).setDebugMessage(ex.getLocalizedMessage())
                .setPath(path);
        if (ex instanceof MethodArgumentNotValidException) {
            Errors result = ((MethodArgumentNotValidException) ex).getBindingResult();
            builder.setErrors(fromBindingErrors(result));
        } else if (ex instanceof DataIntegrityViolationException) {
            List<String> validErrors = new ArrayList<>();
            validErrors.add(Objects.requireNonNull(((DataIntegrityViolationException) ex).getRootCause())
                    .getLocalizedMessage());

            builder.setErrors(validErrors);
        }

        return builder.build();
    }

    private List<String> fromBindingErrors(Errors errors) {
        List<String> validErrors = new ArrayList<>();
        errors.getFieldErrors().forEach(error -> validErrors.add(error.getField() + ": " + error.getDefaultMessage()));

        return validErrors;
    }
}
