package jpabook.jpbshop.repository.order.query;

import jpabook.jpbshop.domain.Address;
import jpabook.jpbshop.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    this(orderId, name, orderDate, orderStatus, address, null);
  }

  public OrderQueryDto(
      Long orderId,
      String name,
      LocalDateTime orderDate,
      OrderStatus orderStatus,
      Address address,
      List<OrderItemQueryDto> orderItems) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
    this.orderItems = orderItems;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderQueryDto that = (OrderQueryDto) o;
    return Objects.equals(getOrderId(), that.getOrderId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getOrderId());
  }
}
