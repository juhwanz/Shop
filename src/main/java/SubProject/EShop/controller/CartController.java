package SubProject.EShop.controller;

import SubProject.EShop.dto.CartItemRequestDto;
import SubProject.EShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<String> addProductToCart(
            @PathVariable Long userId, @RequestBody CartItemRequestDto requestDto

    ) {
        cartService.addProductToCart(userId, requestDto);
        return ResponseEntity.ok("장바구니에 상품이 추가되었습니다.");
    }
}
