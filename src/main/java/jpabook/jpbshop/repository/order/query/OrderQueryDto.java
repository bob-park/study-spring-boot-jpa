package jpabook.jpbshop.repository.order.query;

import jpabook.jpbshop.domain.Address;
import jpabook.jpbshop.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderQueryDto {

  private final Long orderId;
  private final String name;
  private final LocalDateTime orderDate;
  private final OrderStatus orderStatus;
  private final Address address;

  private List<OrderItemQueryDto> orderItems;

  public OrderQueryDto(
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

  public List<OrderItemQueryDto> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItemQueryDto> orderItems) {
    this.orderItems = orderItems;
  }
}
