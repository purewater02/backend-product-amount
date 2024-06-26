package antigravity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Promotion extends DefaultEntity {
  @Column(name = "promotion_type")
  @Enumerated(EnumType.STRING)
  private PromotionType promotionType; // 쿠폰 타입 (쿠폰, 코드)

  @Column(name = "name")
  private String name;

  @Column(name = "discount_type")
  @Enumerated(EnumType.STRING)
  private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인

  @Column(name = "discount_value")
  private int discountValue; // 할인 금액 or 할인 %

  @Column(name = "use_started_at")
  private LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간

  @Column(name = "use_end_at")
  private LocalDate useEndAt; // 쿠폰 사용가능 종료 기간

  public enum PromotionType {
    COUPON,
    CODE
  }

  public enum DiscountType {
    WON,
    PERCENT
  }
}
