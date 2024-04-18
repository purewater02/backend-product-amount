package antigravity.config;

import antigravity.domain.*;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
  private final ProductRepository productRepository;
  private final PromotionRepository promotionRepository;
  private final PromotionProductsRepository promotionProductsRepository;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {
    productRepository.deleteAll();
    promotionRepository.deleteAll();
    promotionProductsRepository.deleteAll();

    Product product = Product.builder().name("피팅노드상품").price(215000).build();
    productRepository.save(product);

    Promotion promotion1 =
        Promotion.builder()
            .promotionType(Promotion.PromotionType.COUPON)
            .name("30000원 할인쿠폰")
            .discountType(Promotion.DiscountType.WON)
            .discountValue(30000)
            .useStartedAt(LocalDate.of(2022, 11, 1))
            .useEndAt(LocalDate.of(2023, 3, 1))
            .build();

    Promotion promotion2 =
        Promotion.builder()
            .promotionType(Promotion.PromotionType.CODE)
            .name("15% 할인코드")
            .discountType(Promotion.DiscountType.PERCENT)
            .discountValue(15)
            .useStartedAt(LocalDate.of(2022, 11, 1))
            .useEndAt(LocalDate.of(2023, 3, 1))
            .build();

    promotionRepository.save(promotion1);
    promotionRepository.save(promotion2);

    PromotionProducts promotionProducts1 =
        PromotionProducts.builder().promotionId(1L).productId(1L).build();

    PromotionProducts promotionProducts2 =
        PromotionProducts.builder().promotionId(2L).productId(1L).build();

    promotionProductsRepository.save(promotionProducts1);
    promotionProductsRepository.save(promotionProducts2);
  }
}
