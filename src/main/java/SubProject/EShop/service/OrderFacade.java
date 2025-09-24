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
    private final OrderService orderService;

    public Long placeOrder(OrderRequestDto requestDto) {
        RLock lock = redissonClient.getLock("product:" + requestDto.getProductId());
        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("다른 사용자가 주문 중입니다. 잠시 후 다시 시도해주세요.");
            }
            return orderService.placeOrder(requestDto);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락을 획득하는 동안 오류가 발생했습니다.", e);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}