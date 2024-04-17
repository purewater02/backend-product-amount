package antigravity.domain;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class PromotionProducts extends DefaultEntity {
  private Long promotionId;
  private Long productId;
}
