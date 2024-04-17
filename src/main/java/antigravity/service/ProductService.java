package antigravity.service;

import antigravity.domain.Product;
import antigravity.domain.ProductRepository;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
  private final ProductRepository repository;

  public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
    Product product =
        repository.findById(request.getProductId()).orElseThrow(IllegalArgumentException::new);

    return null;
  }
}
