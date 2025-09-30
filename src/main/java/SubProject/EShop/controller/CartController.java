package SubProject.EShop.controller;

import SubProject.EShop.domain.User;
import SubProject.EShop.dto.CartItemRequestDto;
import SubProject.EShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //@PostMapping("/user/{userId}")
    @PostMapping("/items")
    public ResponseEntity<String> addProductToCart(
            //@PathVariable 대신
            @AuthenticationPrincipal User user, @RequestBody CartItemRequestDto requestDto

    ) {
        cartService.addProductToCart(user, requestDto);
        return ResponseEntity.ok("장바구니에 상품이 추가되었습니다.");
    }
}
