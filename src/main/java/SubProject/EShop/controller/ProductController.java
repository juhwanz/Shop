package SubProject.EShop.controller;

import SubProject.EShop.dto.ProductRequestDto;
import SubProject.EShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
