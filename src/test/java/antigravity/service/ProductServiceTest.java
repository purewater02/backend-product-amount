package antigravity.service;

import antigravity.domain.entity.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class ProductServiceTest {
  @Autowired private ProductRepository productRepository;

  @Test
  void getProductAmountTest() {}
}
