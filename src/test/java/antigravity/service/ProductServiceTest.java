package antigravity.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import antigravity.domain.*;
import antigravity.model.exception.BadRequestException;
import antigravity.model.exception.ErrorCode;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class ProductServiceTest {
  @Autowired private ProductRepository productRepository;
  @Autowired private PromotionRepository promotionRepository;
  @Autowired private PromotionProductsRepository promotionProductsRepository;
  @Autowired private ProductService productService;

  @Test
  @DisplayName("지정가격을 벗어난 상품은 예외 처리된다.")
  void getProductAmountTest1() {
    // given
    Product cheapOne = Product.builder().name("10000원 미만 상품").price(9999).build();
    Product savedProduct1 = this.productRepository.save(cheapOne);

    Product expensiveOne = Product.builder().name("10000000 초과 상품").price(10000001).build();
    Product savedProduct2 = this.productRepository.save(expensiveOne);

    int[] couponIds = {1, 2};
    ProductInfoRequest request1 =
        ProductInfoRequest.builder().productId(savedProduct1.getId()).couponIds(couponIds).build();

    ProductInfoRequest request2 =
        ProductInfoRequest.builder().productId(savedProduct2.getId()).couponIds(couponIds).build();

    LocalDate now = LocalDate.now();

    // when & then
    BadRequestException result1 =
        assertThrows(
            BadRequestException.class,
            () -> {
              productService.getProductAmount(request1, now);
            });

    BadRequestException result2 =
        assertThrows(
            BadRequestException.class,
            () -> {
              productService.getProductAmount(request2, now);
            });

    assertThat(result1.getErrorCode()).isEqualTo(ErrorCode.PRICE_RANGE_NOT_APPLICABLE);
    assertThat(result2.getErrorCode()).isEqualTo(ErrorCode.PRICE_RANGE_NOT_APPLICABLE);
  }

  @Test
  @DisplayName("요청 상품에 적용되는 쿠폰이 아닌 경우 예외처리 한다.")
  void getProductAmountTest2() {
    // given
    LocalDate now = LocalDate.now();

    Promotion newPromotion =
        Promotion.builder()
            .name("테스트 쿠폰1")
            .promotionType(Promotion.PromotionType.COUPON)
            .discountType(Promotion.DiscountType.WON)
            .discountValue(10000)
            .useStartedAt(now)
            .useEndAt(now.plusDays(1))
            .build();
    Promotion savedPromotion = this.promotionRepository.save(newPromotion);

    int[] couponIds = {savedPromotion.getId().intValue()};
    ProductInfoRequest request1 =
        ProductInfoRequest.builder().productId(1).couponIds(couponIds).build();

    // when & then
    BadRequestException result1 =
        assertThrows(
            BadRequestException.class,
            () -> {
              productService.getProductAmount(request1, now);
            });

    assertThat(result1.getErrorCode()).isEqualTo(ErrorCode.PROMOTION_NOT_APPLICABLE);
  }

  @Test
  @DisplayName("사용 기한을 벗어난 쿠폰인 경우 예외처리 한다.")
  void getProductAmountTest3() {
    // given
    LocalDate now = LocalDate.now();

    int[] couponIds = {1, 2}; // 둘 다 오늘날짜로 하면 사용기한을 벗어난 쿠폰들임.
    ProductInfoRequest request1 =
        ProductInfoRequest.builder().productId(1).couponIds(couponIds).build();

    // when & then
    BadRequestException result1 =
        assertThrows(
            BadRequestException.class,
            () -> {
              productService.getProductAmount(request1, now);
            });

    assertThat(result1.getErrorCode()).isEqualTo(ErrorCode.DATE_NOT_APPLICABLE);
  }

  @Test
  @DisplayName("쿠폰이 적용된 최종 상품 가격이 정상 응답한다.")
  void getProductAmountTest4() {
    // given
    LocalDate now = LocalDate.of(2023, 1, 1); // 테스트 케이스 쿠폰 적용 가능 기간 내 날짜.

    int[] couponIds = {1, 2};
    ProductInfoRequest request1 =
        ProductInfoRequest.builder().productId(1).couponIds(couponIds).build();

    // when
    ProductAmountResponse response = productService.getProductAmount(request1, now);

    assertThat(response.getName()).isEqualTo("피팅노드상품");
    // 쿠폰 1  -30000원, 쿠폰 2 -32250원 , 기존가격 215000 - 62250 = 152750, 천단위 절사 -> 152000
    assertThat(response.getOriginPrice()).isEqualTo(215000);
    assertThat(response.getDiscountPrice()).isEqualTo(62250);
    assertThat(response.getFinalPrice()).isEqualTo(152000);
  }
}
