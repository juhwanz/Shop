package SubProject.EShop.controller;

import SubProject.EShop.dto.OrderRequestDto;
import SubProject.EShop.service.OrderFacade;
import SubProject.EShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDto requestDto) throws InterruptedException{
        try {
            Long orderId = orderService.placeOrder(requestDto); // 👈 호출 대상 변경
            return ResponseEntity.ok("주문 성공. 주문 ID : " + orderId);
        } catch (RuntimeException e) {
            // GlobalExceptionHandler가 처리하도록 예외를 그대로 던지는 것이 더 좋습니다.
            throw e;
        }
    }
}
