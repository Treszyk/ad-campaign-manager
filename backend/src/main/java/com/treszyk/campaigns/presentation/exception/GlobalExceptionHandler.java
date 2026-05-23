package com.treszyk.campaigns.presentation.exception;

import com.treszyk.campaigns.domain.exception.InsufficientFundsException;
import com.treszyk.campaigns.domain.exception.ProductAlreadyCampaignedException;
import com.treszyk.campaigns.domain.exception.ResourceNotFoundException;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  public record ErrorResponse(int status, String error, String message, Instant timestamp) {}

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    // in a multi-tenant SaaS, returning descriptive not found errors can leak information (user
    // enumeration).
    // A generic 404/403 would be safer there, but returning the exact message here is chosen for
    // easier debugging and UX.
    // I'm working under the assumption that this is a single-tenant in-house application.
    ErrorResponse body =
        new ErrorResponse(
            HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), Instant.now());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler({
    IllegalArgumentException.class,
    InsufficientFundsException.class,
    ProductAlreadyCampaignedException.class
  })
  public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
    ErrorResponse body =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage(), Instant.now());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex) {
    String message = "Invalid JSON request body or parameter format.";
    Throwable cause = ex.getCause();
    if (cause instanceof tools.jackson.databind.exc.InvalidFormatException ife) {
      if (ife.getPath() != null && !ife.getPath().isEmpty()) {
        String fieldName = ife.getPath().getFirst().getPropertyName();
        message = "Invalid value provided for field: " + fieldName;
      }
    }
    ErrorResponse body =
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", message, Instant.now());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleFallback(Exception ex) {
    log.error("An unexpected error occurred: ", ex);

    ErrorResponse body =
        new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred. Please contact system support.",
            Instant.now());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}
