package SubProject.EShop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProductRequestDto {

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String name;

    @Min(value = 1, message = "가격은 0보다 커야 합니다.")
    private int price;

    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    private int stockQuantity;
}
