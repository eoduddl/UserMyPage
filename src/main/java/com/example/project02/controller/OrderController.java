package com.example.project02.controller;

import com.example.project02.entity.Cart;
import com.example.project02.repository.CartRepository;
import com.example.project02.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CartRepository cartRepository;

    @PostMapping("/{userId}/payments")
    public ResponseEntity<Map<String, String>> order(@PathVariable("userId") Long userId) {

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            throw new RuntimeException("장바구니에 상품을 먼저 추가해주세요");
        }

        int count = cart.getTotalCount();
        double price = cart.getTotalPrice();

        String response = String.format("총 %d개의 상품과 함께 %.1f원 결제되었습니다.", count, price);

        Map<String, String> message = new HashMap<>();
        message.put("message", response);

        orderService.order(userId);

        return ResponseEntity.ok().body(message);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
