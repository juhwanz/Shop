package SubProject.EShop.dto;

import lombok.Getter;

@Getter
public class OrderRequestDto {
    private Long userId;    // 주문하는 사용자 ID
    private Long productId; // 주문할 상품 ID
    private int quantity;   // 주문 수량
}
