package antigravity.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends RuntimeException {
  private final ErrorCode errorCode;
  private final HttpStatus httpStatus;

  public BadRequestException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.httpStatus = HttpStatus.BAD_REQUEST;
  }
}
