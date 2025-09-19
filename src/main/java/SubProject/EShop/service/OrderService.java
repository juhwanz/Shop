// service/OrderService.java
package SubProject.EShop.service;

import SubProject.EShop.domain.Order;
import SubProject.EShop.domain.Product;
import SubProject.EShop.dto.OrderRequestDto;
import SubProject.EShop.repository.OrderRepository;
import SubProject.EShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @CacheEvict(value = "products", key = "#requestDto.productId")
    @Transactional
    public Long placeOrderWithLock(OrderRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        product.decreaseStock(requestDto.getQuantity());

        Order order = new Order(requestDto.getUserId(), product, requestDto.getQuantity());
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }
}