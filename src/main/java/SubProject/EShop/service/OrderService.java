package SubProject.EShop.service;

import SubProject.EShop.domain.Order;
import SubProject.EShop.domain.Product;
import SubProject.EShop.dto.OrderRequestDto;
import SubProject.EShop.repository.OrderRepository;
import SubProject.EShop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long placeOrder(OrderRequestDto requestDto){
        // 1. 상품 조회
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        // 2. 재고 감소 ( 문제 코드!!!!!)
        product.decreaseStock(requestDto.getQuantity());

        // 3. 주문 생성 및 저장
        Order order = new Order(requestDto.getUserId(), product, requestDto.getQuantity());
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }
}
