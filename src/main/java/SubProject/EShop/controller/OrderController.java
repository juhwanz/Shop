// controller/OrderController.java
package SubProject.EShop.controller;

import SubProject.EShop.dto.OrderRequestDto;
import SubProject.EShop.service.OrderFacade;
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

    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDto requestDto) {
        // GlobalExceptionHandler가 예외를 처리하므로 try-catch 제거 가능
        Long orderId = orderFacade.placeOrder(requestDto);
        return ResponseEntity.ok("주문 성공. 주문 ID : " + orderId);
    }
}