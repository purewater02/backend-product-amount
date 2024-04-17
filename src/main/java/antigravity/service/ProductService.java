package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.ProductRepository;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
  private final ProductRepository repository;

  public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
    System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

    Product product =
        repository.findById(request.getProductId()).orElseThrow(IllegalArgumentException::new);

    return null;
  }
}
