package com.eCom.mibCommerce.mapper;

import com.eCom.mibCommerce.entity.orderAggregator.Order;
import com.eCom.mibCommerce.model.OrderDto;
import com.eCom.mibCommerce.model.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "basketId", target = "basketId")
    @Mapping(source = "shippingAddress", target = "shippingAddress")
    @Mapping(source = "subTotal", target = "subTotal")
    @Mapping(source = "deliveryCharge", target = "deliveryCharge")
    @Mapping(target = "total", expression = "java(order.getSubTotal()+order.getDeliveryCharge())")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "orderStatus", constant = "Pending")
    OrderResponseDto toOrderResponseDto(Order order);

    @Mapping(target = "orderDate", expression = "java(orderDto.getOrderDate())")
    @Mapping(target = "orderStatus", constant = "Pending")
    Order toOrder(OrderDto orderDto);

    List<OrderDto> toOrderDtoList(List<Order> orders);
    void updateOrderFromOrderResponseDto(OrderDto orderDto, @MappingTarget Order order);
}
