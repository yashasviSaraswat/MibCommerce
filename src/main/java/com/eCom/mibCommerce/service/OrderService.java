package com.eCom.mibCommerce.service;

import com.eCom.mibCommerce.model.OrderDto;
import com.eCom.mibCommerce.model.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponseDto getOrderById(Integer orderId);
    List<OrderResponseDto> getAllOrders();
    Page<OrderResponseDto> getAllOrders(Pageable pageable);
    Integer createOrder(OrderDto orderDto);
    void deleteOrder(Integer orderId);
}
