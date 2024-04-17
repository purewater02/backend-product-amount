package antigravity.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  PRODUCT_NOT_FOUND("P001", "해당 상품을 찾을 수 없습니다."),
  COUPON_NOT_FOUND("P002", "해당 쿠폰을 찾을 수 없습니다."),
  PROMOTION_NOT_APPLICABLE("P003", "해당 상품에 사용할 수 없는 쿠폰입니다."),
  DATE_NOT_APPLICABLE("P004", "사용 기간을 벗어난 쿠폰입니다."),
  PRICE_RANGE_NOT_APPLICABLE("P004", "지정 가격 범위를 벗어난 상품입니다."),

  UNKNOWN_ERROR("9999", "");

  private final String value;
  private final String message;
}
