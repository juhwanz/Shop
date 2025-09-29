package SubProject.EShop.controller;

import SubProject.EShop.domain.Product;
import SubProject.EShop.dto.ProductRequestDto;
import SubProject.EShop.dto.ProductUpdateRequestDto;
import SubProject.EShop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product API", description = "상품(Product)관련 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "새로운 상품을 시스템에 등록합니다.")
    @PostMapping
    public ResponseEntity<String> registerProduct(@RequestBody ProductRequestDto requestDto){
        Long productId = productService.registerProduct(requestDto);
        return ResponseEntity.ok("상품 등록 완료. 상품 ID: " + productId);
    }

    @Operation(summary = "상품 단건 조회", description = "상품 ID를 이용하여 특정 상품의 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable Long productId){
        return productService.getProductById(productId);
    }

    @Operation(summary = "상품 전체 조회", description = "시스템에 등록된 모든 상품 목록을 조회합니다.")
    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @Operation(summary = "상품 정보 수정", description = "상품 ID를 이용하여 기존 상품의 정보를 수정합니다.")
    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequestDto requestDto){
        productService.updateProduct(productId, requestDto);
        return ResponseEntity.ok("상품 정보가 수정되었습니다. 상품 ID:" + productId);
    }

    @Operation(summary = "상품 삭제", description = "상품 ID를 이용하여 특정 상품을 시스템에서 삭제합니다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.ok("상품이 삭제되었습니다. 상품 ID:" + productId);
    }
}
