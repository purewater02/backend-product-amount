package antigravity.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class PromotionProducts extends DefaultEntity {
  private int promotionId;
  private int productId;
}
