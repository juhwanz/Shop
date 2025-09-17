package SubProject.EShop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders") // 'order'은 DB 예약어 인 경우가 많아 orders
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 한 명의 유저 - 여러 주문 가능 (1:N) - 현재는 아이디만 저장
    private Long userId;

    // 하나의 상품은 여러 주문에 포함 가능 ( 1: N)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private LocalDateTime orderDate;

    public Order(Long userId, Product product, int quantity){
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.orderDate = LocalDateTime.now();
    }
}
