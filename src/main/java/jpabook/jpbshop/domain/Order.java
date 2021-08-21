package jpabook.jpbshop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order {

  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "order")
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  private LocalDateTime orderDate;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public Long getId() {
    return id;
  }

  public Order setId(Long id) {
    this.id = id;
    return this;
  }

  public Member getMember() {
    return member;
  }

  public Order setMember(Member member) {
    this.member = member;
    return this;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public Order setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
    return this;
  }

  public Delivery getDelivery() {
    return delivery;
  }

  public Order setDelivery(Delivery delivery) {
    this.delivery = delivery;
    return this;
  }

  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  public Order setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
    return this;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public Order setStatus(OrderStatus status) {
    this.status = status;
    return this;
  }
}
