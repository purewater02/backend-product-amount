package antigravity.domain.entity;

import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Promotion extends DefaultEntity {
  private PromotionType promotion_type; // 쿠폰 타입 (쿠폰, 코드)
  private String name;
  private DiscountType discount_type; // WON : 금액 할인, PERCENT : %할인
  private int discount_value; // 할인 금액 or 할인 %
  private LocalDate use_started_at; // 쿠폰 사용가능 시작 기간
  private LocalDate use_ended_at; // 쿠폰 사용가능 종료 기간

  public enum PromotionType {
    COUPON,
    CODE
  }

  public enum DiscountType {
    WON,
    PERCENT
  }
}
