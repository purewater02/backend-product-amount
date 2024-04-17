package antigravity.service;

import antigravity.domain.*;
import antigravity.model.exception.BadRequestException;
import antigravity.model.exception.ErrorCode;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final PromotionRepository promotionRepository;
  private final PromotionProductsRepository promotionProductsRepository;

  public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
    Product product =
        productRepository
            .findById(request.getProductId())
            .orElseThrow(() -> new BadRequestException(ErrorCode.PRODUCT_NOT_FOUND));

    int originalPrice = product.getPrice();

    // 지정 가격 범위 벗어난 상품 익셉션 처리
    if (originalPrice < 10000 || originalPrice > 10000000) {
      throw new BadRequestException(ErrorCode.PRICE_RANGE_NOT_APPLICABLE);
    }

    // 쿠폰이 없는 경우
    if (request.getCouponIds().length == 0) {
      return ProductAmountResponse.builder()
          .name(product.getName())
          .originPrice(originalPrice)
          .discountPrice(0)
          .finalPrice(originalPrice)
          .build();
    }

    // 쿠폰이 1개 이상인 경우
    List<Long> promotionIdList =
        Arrays.stream(request.getCouponIds()).mapToObj(id -> (long) id).toList();
    List<Promotion> promotionList =
        promotionIdList.stream()
            .map(
                id ->
                    promotionRepository
                        .findById(id)
                        .orElseThrow(() -> new BadRequestException(ErrorCode.COUPON_NOT_FOUND)))
            .toList();

    List<PromotionProducts> promotionProductList =
        promotionProductsRepository.findAllByProductId(product.getId());

    int discountPrice = 0;

    // 해당 상품에 적용가능한 프로모션인지 확인
    for (Promotion promotion : promotionList) {
      isPromotionApplicableForProduct(promotionProductList, promotion);
      // 통과하여 모두 적용가능한 쿠폰이라면 가격 계산 실행
      discountPrice = calculateTotalDiscountAmount(originalPrice, promotion);
    }

    // TODO: 사용가능날짜 제약 추가해야 함.

    return ProductAmountResponse.builder()
        .name(product.getName())
        .originPrice(originalPrice)
        .discountPrice(discountPrice)
        .finalPrice(originalPrice - discountPrice)
        .build();
  }

  private int calculateTotalDiscountAmount(long originalPrice, Promotion promotion) {
    long discountAmount = 0L;
    if (promotion.getPromotionType().equals(Promotion.PromotionType.COUPON)) {
      discountAmount += promotion.getDiscountValue();
    } else {
      BigDecimal originalPriceBd = BigDecimal.valueOf(originalPrice);
      BigDecimal discountRate = BigDecimal.valueOf(promotion.getDiscountValue());
      BigDecimal discountValue =
          originalPriceBd
              .multiply(discountRate)
              .divide(BigDecimal.valueOf(100L), RoundingMode.FLOOR); // 부동소수점 연산 손실 방지. 소수점 이하는 버림.
      discountAmount += discountValue.longValue();
    }
    return (int) discountAmount; // 제약사항의 상품 가격이 10,000 ~ 10,000,000 까지기 때문에 캐스팅 해도 됨.
  }

  private void isPromotionApplicableForProduct(
      List<PromotionProducts> promotionProductList, Promotion promotion) {
    boolean isApplicable =
        promotionProductList.stream()
            .anyMatch(
                promotionProduct -> promotionProduct.getPromotionId().equals(promotion.getId()));
    if (!isApplicable) {
      throw new BadRequestException(ErrorCode.PROMOTION_NOT_APPLICABLE);
    }
  }
}
