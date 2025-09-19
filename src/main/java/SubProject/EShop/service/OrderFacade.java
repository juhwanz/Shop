// service/OrderFacade.java
package SubProject.EShop.service;

import SubProject.EShop.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final RedissonClient redissonClient;
    private final OrderService orderService; // 👈 실제 로직을 담은 서비스를 주입받음

    public Long placeOrder(OrderRequestDto requestDto) throws InterruptedException {
        RLock lock = redissonClient.getLock("product:" + requestDto.getProductId());

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("락 획득에 실패했습니다.");
            }
            // '대리인'을 통해 핵심 로직을 호출
            return orderService.placeOrderWithLock(requestDto);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}