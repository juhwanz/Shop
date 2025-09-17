package SubProject.EShop.dto;

import lombok.Getter;

@Getter
public class ProductRequestDto {
    private String name;
    private int price;
    private int stockQuantity;
}
