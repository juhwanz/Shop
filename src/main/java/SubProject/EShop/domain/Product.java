package SubProject.EShop.domain;

import SubProject.EShop.exception.SoldOutException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    public Product(String name, int price, int stockQuantity){
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    //재고 감소 로직 추가 : '상품'스스로 자신의 재고를 줄이는 책임(메서드)를 갖게 함.
    public void decreaseStock(int quantity){
        if(this.stockQuantity - quantity <0) throw new SoldOutException("재고가 부족하여 주문에 실패했습니다.");
        this.stockQuantity -= quantity;
    }
}
