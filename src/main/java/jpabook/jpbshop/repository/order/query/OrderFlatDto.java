package jpabook.jpbshop.repository.order.query;

import jpabook.jpbshop.domain.Address;
import jpabook.jpbshop.domain.OrderStatus;

import java.time.LocalDateTime;

public class OrderFlatDto {

    private final Long orderId;
    private final String name;
    private final LocalDateTime orderDate;
    private final OrderStatus orderStatus;
    private final Address address;

    private final String itemName;
    private final int orderPrice;
    private final int count;

    public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
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

    public String getItemName() {
        return itemName;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public int getCount() {
        return count;
    }
}
