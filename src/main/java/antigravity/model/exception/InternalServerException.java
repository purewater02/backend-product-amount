package antigravity.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalServerException extends RuntimeException {
  private final ErrorCode errorCode;
  private final HttpStatus httpStatus;

  public InternalServerException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public InternalServerException(Throwable cause) {
    super(cause.getMessage(), cause);
    this.errorCode = ErrorCode.UNKNOWN_ERROR;
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
