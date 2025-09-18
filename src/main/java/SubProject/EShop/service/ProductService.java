package SubProject.EShop.service;

import SubProject.EShop.domain.Product;
import SubProject.EShop.dto.ProductRequestDto;
import SubProject.EShop.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Long registerProduct(ProductRequestDto requestDto){
        Product product = new Product(requestDto.getName(), requestDto.getPrice(), requestDto.getStockQuantity());
        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    @Cacheable(value = "products", key = "#productId")
    public Product getProductById(Long productId) {
        System.out.println("DB에서 상품 정보를 조회합니다...");
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
    }
}
