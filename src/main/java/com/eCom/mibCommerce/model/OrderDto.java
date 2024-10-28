package com.eCom.mibCommerce.model;

import com.eCom.mibCommerce.entity.orderAggregator.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private String basketId;
    private ShippingAddress shippingAddress;
    private Long subtotal;
    private Long deliveryCharge;
    private LocalDate orderDate;
}
