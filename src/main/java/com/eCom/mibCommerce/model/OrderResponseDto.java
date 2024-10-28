package com.eCom.mibCommerce.model;

import com.eCom.mibCommerce.entity.orderAggregator.OrderStatus;
import com.eCom.mibCommerce.entity.orderAggregator.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private Integer orderId;
    private String basketId;
    private ShippingAddress shippingAddress;
    private Long subTotal;
    private Long deliveryCharge;
    private Double total;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
}
