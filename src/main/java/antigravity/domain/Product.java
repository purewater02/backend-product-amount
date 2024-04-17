package antigravity.domain;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
@Entity
public class Product extends DefaultEntity {
  private String name;
  private int price;
}
