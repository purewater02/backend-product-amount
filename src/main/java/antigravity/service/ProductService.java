package antigravity.service;

import antigravity.domain.*;
import antigravity.model.exception.BadRequestException;
import antigravity.model.exception.ErrorCode;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.util.CalculationUtil;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final PromotionRepository promotionRepository;
  private final PromotionProductsRepository promotionProductsRepository;

  public ProductAmountResponse getProductAmount(ProductInfoRequest request, LocalDate date) {
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
      isPromotionApplicableForProduct(promotionProductList, promotion, date);
      // 통과하여 모두 적용가능한 쿠폰이라면 가격 계산 실행
      discountPrice += calculateTotalDiscountAmount(originalPrice, promotion);
    }

    int finalPrice = CalculationUtil.truncateToThousand(originalPrice - discountPrice);

    return ProductAmountResponse.builder()
        .name(product.getName())
        .originPrice(originalPrice)
        .discountPrice(discountPrice)
        .finalPrice(finalPrice)
        .build();
  }

  private int calculateTotalDiscountAmount(int originalPrice, Promotion promotion) {
    int discountAmount = 0;
    if (promotion.getPromotionType().equals(Promotion.PromotionType.COUPON)) {
      discountAmount += promotion.getDiscountValue();
    } else {
      // int 형이라 BigDecimal 쓸 필요가 전혀 없었음.
      int discountValue = originalPrice * promotion.getDiscountValue() / 100;
      discountAmount += discountValue;
    }
    return discountAmount;
  }

  private void isPromotionApplicableForProduct(
      List<PromotionProducts> promotionProductList, Promotion promotion, LocalDate date) {
    boolean isApplicable =
        promotionProductList.stream()
            .anyMatch(
                promotionProduct -> promotionProduct.getPromotionId().equals(promotion.getId()));

    boolean isDateValid =
        (date.isEqual(promotion.getUseStartedAt()) || date.isAfter(promotion.getUseStartedAt()))
            && (date.isEqual(promotion.getUseEndAt()) || date.isBefore(promotion.getUseEndAt()));

    if (!isApplicable) {
      throw new BadRequestException(ErrorCode.PROMOTION_NOT_APPLICABLE);
    }
    if (!isDateValid) {
      throw new BadRequestException(ErrorCode.DATE_NOT_APPLICABLE);
    }
  }
}
