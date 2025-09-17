package SubProject.EShop.controller;

import SubProject.EShop.dto.OrderRequestDto;
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

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDto requestDto){
        Long orderId = orderService.placeOrder(requestDto);
        return ResponseEntity.ok("주문 성공. 주문 ID : " + orderId);
    }
}
