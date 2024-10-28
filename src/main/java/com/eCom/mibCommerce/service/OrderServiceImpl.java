package com.eCom.mibCommerce.service;

import com.eCom.mibCommerce.entity.orderAggregator.Order;
import com.eCom.mibCommerce.entity.orderAggregator.OrderItem;
import com.eCom.mibCommerce.entity.orderAggregator.ProductItemOrdered;
import com.eCom.mibCommerce.mapper.OrderMapper;
import com.eCom.mibCommerce.model.BasketItemResponseDto;
import com.eCom.mibCommerce.model.BasketResponseDto;
import com.eCom.mibCommerce.model.OrderDto;
import com.eCom.mibCommerce.model.OrderResponseDto;
import com.eCom.mibCommerce.repository.BrandRepository;
import com.eCom.mibCommerce.repository.OrderRepository;
import com.eCom.mibCommerce.repository.TypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final BrandRepository brandRepository;
    private final TypeRepository typeRepository;
    private final BasketService basketService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, BrandRepository brandRepository, TypeRepository typeRepository, BasketService basketService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.brandRepository = brandRepository;
        this.typeRepository = typeRepository;
        this.basketService = basketService;
    }

    @Override
    public OrderResponseDto getOrderById(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(orderMapper::toOrderResponseDto).orElse(null);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::toOrderResponseDto).collect(Collectors.toList());
    }

    @Override
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toOrderResponseDto);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public Integer createOrder(OrderDto orderDto) {

        //fetch basket details
        BasketResponseDto basketRes = basketService.getBasketById(orderDto.getBasketId());
        if(basketRes == null) {
            log.error("basket with ID {} not found", orderDto.getBasketId());
            return null;
        }

        //map basket item to order item
        List<OrderItem> orderItems = basketRes.getItemResponses()
                .stream()
                .map(this::mapBasketItemtoOrderItem)
                .collect(Collectors.toList());

        //calculate subTotal
        Double subTotal = basketRes.getItemResponses()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        //set order details
        Order order = orderMapper.toOrder(orderDto);
        order.setOrderItems(orderItems);
        order.setSubTotal(subTotal);

        //save the order
        Order savedOrder = orderRepository.save(order);
        basketService.deleteBasketById(orderDto.getBasketId());

        //return response
        return savedOrder.getOrderId();
    }

    private OrderItem mapBasketItemtoOrderItem(BasketItemResponseDto basketItemResponseDto) {
        if(basketItemResponseDto != null) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductItemOrdered(mapBasketItemToProduct(basketItemResponseDto));
            return orderItem;
        }else{
            return null;
        }
    }

    private ProductItemOrdered mapBasketItemToProduct(BasketItemResponseDto basketItemResponseDto) {
        ProductItemOrdered productItemOrdered = new ProductItemOrdered();
        productItemOrdered.setName(basketItemResponseDto.getName());
        productItemOrdered.setPictureUrl(basketItemResponseDto.getPictureUrl());
        productItemOrdered.setProductId(basketItemResponseDto.getId());
        return productItemOrdered;
    }
}
