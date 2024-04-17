package antigravity.service;

import antigravity.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class ProductServiceTest {
  @Autowired private ProductRepository productRepository;

  @Test
  @DisplayName("쿠폰이 모두 적용된 최종 가격이 정상 응답한다.")
  void getProductAmountTest() {}
}
