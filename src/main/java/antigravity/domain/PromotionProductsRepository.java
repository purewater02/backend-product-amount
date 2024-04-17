package antigravity.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Long> {
  List<PromotionProducts> findAllByProductId(Long productId);
}
