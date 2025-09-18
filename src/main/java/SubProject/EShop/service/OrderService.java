package SubProject.EShop.service;

import SubProject.EShop.domain.Order;
import SubProject.EShop.domain.Product;
import SubProject.EShop.dto.OrderRequestDto;
import SubProject.EShop.repository.OrderRepository;
import SubProject.EShop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RedissonClient redissonClient; // Redisson 클라이언트 주입

    public Long placeOrder(OrderRequestDto requestDto) {
        // 락 이름에 상품 ID를 포함하여, 상품별로 락을 다르게 설정
        RLock lock = redissonClient.getLock("product:" + requestDto.getProductId());

        try{
            // 락 흭득 시도 ( 최대 10초 대기, 락 희득 후 1초간 점유)
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if(!available){
                System.out.println("락 흭득 실패");
                throw new RuntimeException("다른 사용자가 주문 중입니다. 잠시 후 다시 시도해주세요");
            }

            // 락 흭득 성공 시 , 트랜잭션이 적용된 핵심 로직 호출
            return placeOrderWithLock(requestDto);
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        } finally {
            // 현재 스레드가 락을 점유하고 있을 때만 unlock을 호출하도록 수정합니다.
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Transactional
    public Long placeOrderWithLock(OrderRequestDto requestDto){
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
