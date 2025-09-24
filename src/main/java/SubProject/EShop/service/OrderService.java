// service/OrderService.java
package SubProject.EShop.service;

import SubProject.EShop.domain.Order;
import SubProject.EShop.domain.Product;
import SubProject.EShop.dto.OrderRequestDto;
import SubProject.EShop.repository.OrderRepository;
import SubProject.EShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long placeOrder(OrderRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        // Race Condition 유발을 위한 함정 코드
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) { /* 무시 */ }

        product.decreaseStock(requestDto.getQuantity());

        Order order = new Order(requestDto.getUserId(), product, requestDto.getQuantity());
        orderRepository.save(order);

        return order.getId();
    }
}