package antigravity.controller;

import antigravity.model.exception.BadRequestException;
import antigravity.model.exception.ErrorCode;
import antigravity.model.exception.ErrorResponse;
import antigravity.model.exception.InternalServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InternalServerException.class)
  public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    HttpStatus status = ex.getHttpStatus();

    ErrorResponse errorResponse =
        new ErrorResponse(errorCode.getMessage(), errorCode.getValue(), status.value());
    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    HttpStatus status = ex.getHttpStatus();

    ErrorResponse errorResponse =
        new ErrorResponse(ex.getMessage(), errorCode.getValue(), status.value());
    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    ErrorResponse errorResponse =
        new ErrorResponse(
            ex.getMessage(),
            ErrorCode.UNKNOWN_ERROR.getValue(),
            HttpStatus.INTERNAL_SERVER_ERROR.value());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
