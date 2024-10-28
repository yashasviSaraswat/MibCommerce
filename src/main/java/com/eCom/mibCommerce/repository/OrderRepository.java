package com.eCom.mibCommerce.repository;

import com.eCom.mibCommerce.entity.orderAggregator.Order;
import com.eCom.mibCommerce.entity.orderAggregator.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order>  findByBasketId(String basketID);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("select o from Order o join o.orderItems oi where oi.productItemOrdered.name like %:productName%")
    List<Order> findByProductNameInOrderItems(@Param("productName") String productNames);

    @Query("select o from Order o where o.shippingAddress.city = :city")
    List<Order> findByShippingAddressCity(@Param("city") String city);

}
