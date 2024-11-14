package com.eCom.mibCommerce.service;

import com.eCom.mibCommerce.entity.orderAggregator.Order;
import com.eCom.mibCommerce.entity.orderAggregator.OrderItem;
import com.eCom.mibCommerce.entity.orderAggregator.OrderStatus;
import com.eCom.mibCommerce.entity.orderAggregator.ShippingAddress;
import com.eCom.mibCommerce.mapper.OrderMapper;
import com.eCom.mibCommerce.model.OrderResponseDto;
import com.eCom.mibCommerce.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderServiceImpl orderService;

    private static Order order;
    private static Order order1;
    private static Order order2;
    private static OrderResponseDto orderResponseDto;
    private static OrderResponseDto orderResponseDto1;
    private static OrderResponseDto orderResponseDto2;
    private static List<Order> orders;
    private static Integer orderId = 1;


    @BeforeAll
    static void setUp() {
        System.out.println("beforeAll activated...");
        //given
        ShippingAddress shippingAddress = new ShippingAddress(
                "John Doe",
                "123 Main St",
                "New York",
                "NY",
                "10001",
                "USA",
                "1234567890"
        );
        List<OrderItem> orderItemList = Collections.emptyList();
        order1 = Order.builder()
                .orderId(1)
                .basketId("basket123")
                .shippingAddress(shippingAddress)
                .orderDate(LocalDateTime.now())
                .orderItems(orderItemList)
                .subTotal(100.0)
                .deliveryCharge(100L)
                .orderStatus(OrderStatus.Pending)
                .build();
        order2 = Order.builder()
                .orderId(2)
                .basketId("basket1")
                .shippingAddress(shippingAddress)
                .orderDate(LocalDateTime.now())
                .orderItems(orderItemList)
                .subTotal(100.0)
                .deliveryCharge(100L)
                .orderStatus(OrderStatus.Pending)
                .build();
        orderResponseDto1 = OrderResponseDto.builder()
                .orderId(1)
                .basketId("basket123")
                .shippingAddress(shippingAddress)
                .subTotal(100L)
                .deliveryCharge(100L)
                .total(200.00)
                .orderDate(order1.getOrderDate())
                .orderStatus(OrderStatus.Pending)
                .build();
        orderResponseDto2 = OrderResponseDto.builder()
                .orderId(2)
                .basketId("basket123")
                .shippingAddress(shippingAddress)
                .subTotal(100L)
                .deliveryCharge(100L)
                .total(200.00)
                .orderDate(order2.getOrderDate())
                .orderStatus(OrderStatus.Pending)
                .build();
        orders = Arrays.asList(order1,order2);
    }

    @BeforeEach
    void init() {
        System.out.println("BeforeEach Activated now...");
        order = Order.builder()
                .orderId(3)
                .basketId("basket123")
                .shippingAddress(new ShippingAddress())
                .orderDate(LocalDateTime.now())
                .orderItems(Collections.emptyList())
                .subTotal(100.0)
                .deliveryCharge(100L)
                .orderStatus(OrderStatus.Pending)
                .build();
        orderResponseDto = OrderResponseDto.builder()
                .orderId(3)
                .basketId("basket123")
                .shippingAddress(new ShippingAddress())
                .subTotal(100L)
                .deliveryCharge(100L)
                .total(200.00)
                .orderDate(order.getOrderDate())
                .orderStatus(OrderStatus.Pending)
                .build();
    }

    @AfterEach
    void tearDown() {
        System.out.println("afterEach activated...");
        order = order1 = order2 = null;
        orderResponseDto = orderResponseDto1 = orderResponseDto2 = null;
        orders = null;
        orderId = null;
    }

    @Test
    void getOrderByIdShouldReturnOrderById() {

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order1));
        Mockito.when(orderMapper.toOrderResponseDto(order1)).thenReturn(orderResponseDto1);

        OrderResponseDto orderResponseDto = orderService.getOrderById(orderId);

        assertEquals(orderResponseDto1, orderResponseDto);
        assertEquals(1,orderResponseDto1.getOrderId());
        assertEquals(1,order1.getOrderId());
        assertNotNull(orderResponseDto);
        assertEquals(orderId, orderResponseDto.getOrderId());
        assertEquals(order1.getBasketId(), orderResponseDto.getBasketId());
        assertEquals(order1.getShippingAddress(), orderResponseDto.getShippingAddress());
        assertEquals(order1.getOrderDate(), orderResponseDto.getOrderDate());
        assertEquals(order1.getDeliveryCharge(), orderResponseDto.getDeliveryCharge());
        assertEquals(order1.getOrderStatus(), orderResponseDto.getOrderStatus());
        assertEquals(order1.getTotal(), orderResponseDto.getTotal());
        Mockito.verify(orderRepository, Mockito.times(1)).findById(orderId);
        Mockito.verify(orderMapper, Mockito.times(1)).toOrderResponseDto(order1);
    }

    @Test
    void getAllOrdersShouldReturnListOfAllOrders() {

        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        Mockito.when(orderMapper.toOrderResponseDto(order1)).thenReturn(orderResponseDto1);
        Mockito.when(orderMapper.toOrderResponseDto(order2)).thenReturn(orderResponseDto2);

        //when
        List<OrderResponseDto> orderResponseDtos = orderService.getAllOrders();
        //then
        assertEquals(2, orderResponseDtos.size());
        assertEquals(orderResponseDto1, orderResponseDtos.getFirst());
        assertEquals(orderResponseDto2, orderResponseDtos.get(1));

        Mockito.verify(orderRepository, Mockito.times(1)).findAll();
        Mockito.verify(orderMapper, Mockito.times(1)).toOrderResponseDto(order1);
        Mockito.verify(orderMapper, Mockito.times(1)).toOrderResponseDto(order2);
    }

    @Test
    void testGetAllOrdersShouldReturnPageableResult() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        Mockito.when(orderRepository.findAll(pageRequest)).thenReturn(orderPage);
        Mockito.when(orderMapper.toOrderResponseDto(order)).thenReturn(orderResponseDto);
        //when
        Page<OrderResponseDto> result = orderService.getAllOrders(pageRequest);
        //then
        assertEquals(1, result.getTotalElements());
        assertEquals(orderResponseDto, result.getContent().getFirst());
        assertEquals(1, result.getTotalPages());
        Mockito.verify(orderRepository, Mockito.times(1)).findAll(pageRequest);
        Mockito.verify(orderMapper, Mockito.times(1)).toOrderResponseDto(order);

    }

    @Test
    void deleteOrder() {
    }

    @Test
    void createOrder() {
    }
}