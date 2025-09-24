package SubProject.EShop.service;

import SubProject.EShop.domain.Product;
import SubProject.EShop.dto.ProductRequestDto;
import SubProject.EShop.dto.ProductUpdateRequestDto;
import SubProject.EShop.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @CacheEvict(value = "products", key = "#productId")
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequestDto requestDto){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        product.update(requestDto.getName(), requestDto.getPrice(), requestDto.getStockQuantity());
    }

    @CacheEvict(value = "products", key = "#productId")
    @Transactional
    public void deleteProduct(Long productId){
        productRepository.deleteById(productId);
    }
}
