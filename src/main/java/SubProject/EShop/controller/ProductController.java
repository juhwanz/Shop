package SubProject.EShop.controller;

import SubProject.EShop.domain.Product;
import SubProject.EShop.dto.ProductRequestDto;
import SubProject.EShop.dto.ProductUpdateRequestDto;
import SubProject.EShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> registerProduct(@RequestBody ProductRequestDto requestDto){
        Long productId = productService.registerProduct(requestDto);
        return ResponseEntity.ok("상품 등록 완료. 상품 ID: " + productId);
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable Long productId){
        return productService.getProductById(productId);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequestDto requestDto){
        productService.updateProduct(productId, requestDto);
        return ResponseEntity.ok("상품 정보가 수정되었습니다. 상품 ID:" + productId);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.ok("상품이 삭제되었습니다. 상품 ID:" + productId);
    }
}
