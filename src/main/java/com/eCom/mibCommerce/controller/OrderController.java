package com.eCom.mibCommerce.controller;

import com.eCom.mibCommerce.model.OrderDto;
import com.eCom.mibCommerce.model.OrderResponseDto;
import com.eCom.mibCommerce.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Integer orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrderById(orderId);
        if(orderResponseDto != null) {
            return ResponseEntity.ok(orderResponseDto);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orderResponseDto = orderService.getAllOrders();
        return ResponseEntity.ok(orderResponseDto);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<OrderResponseDto>> getPagedOrders(Pageable pageable) {
        Page<OrderResponseDto> orderResponseDtoPage = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orderResponseDtoPage);
    }

    @PostMapping
    public ResponseEntity<Integer> createOrder(@RequestBody OrderDto orderDto) {
        Integer orderId = orderService.createOrder(orderDto);
        if(orderId != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Integer> deleteOrderById(@PathVariable Integer orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
