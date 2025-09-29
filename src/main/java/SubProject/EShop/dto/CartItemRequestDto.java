package SubProject.EShop.dto;

import lombok.Getter;

@Getter
public class CartItemRequestDto {
    private Long productId;
    private int quantity;
}
