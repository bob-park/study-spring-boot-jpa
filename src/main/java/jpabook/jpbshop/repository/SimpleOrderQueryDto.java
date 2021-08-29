package jpabook.jpbshop.repository;

import jpabook.jpbshop.domain.Address;
import jpabook.jpbshop.domain.Order;
import jpabook.jpbshop.domain.OrderStatus;

import java.time.LocalDateTime;

public class SimpleOrderQueryDto {

  private final Long orderId;
  private final String name;
  private final LocalDateTime orderDate;
  private final OrderStatus orderStatus;
  private final Address address;

  public SimpleOrderQueryDto(Order order) {
    this(
        order.getId(),
        order.getMember().getName(),
        order.getOrderDate(),
        order.getStatus(),
        order.getDelivery().getAddress());
  }

  public SimpleOrderQueryDto(
      Long orderId,
      String name,
      LocalDateTime orderDate,
      OrderStatus orderStatus,
      Address address) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
  }

  public Long getOrderId() {
    return orderId;
  }

  public String getName() {
    return name;
  }

  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public Address getAddress() {
    return address;
  }
}
