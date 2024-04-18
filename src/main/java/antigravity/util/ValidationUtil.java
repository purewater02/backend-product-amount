package antigravity.util;

import antigravity.model.exception.BadRequestException;
import antigravity.model.exception.ErrorCode;
import java.time.LocalDate;

public final class ValidationUtil {
  private ValidationUtil() {
    throw new IllegalStateException("유틸 클래스 입니다.");
  }

  public static void isDateValid(LocalDate targetDate, LocalDate start, LocalDate end) {
    boolean isValid =
        (targetDate.isEqual(start) || targetDate.isAfter(start))
            && (targetDate.isEqual(end) || targetDate.isBefore(end));
    if (!isValid) throw new BadRequestException(ErrorCode.DATE_NOT_APPLICABLE);
  }

  public static void isPriceRangeApplicable(int price) {
    if (price < 10000 || price > 10000000) {
      throw new BadRequestException(ErrorCode.PRICE_RANGE_NOT_APPLICABLE);
    }
  }
}
