package antigravity.controller;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

  private final ProductService service;

  // 상품 가격 추출 api
  @GetMapping("/amount")
  public ResponseEntity<ProductAmountResponse> getProductAmount() {

    // 사용 가능 기간 테스트를 위해 LocalDate 추가. ProductInfoRequest는 고치면 안되는 제약 조건.
    ProductAmountResponse response = service.getProductAmount(getParam(), LocalDate.now());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private ProductInfoRequest getParam() {
    int[] couponIds = {1, 2};

    return ProductInfoRequest.builder().productId(1).couponIds(couponIds).build();
  }
}
