package jpabook.jpbshop.domain;

import jpabook.jpbshop.domain.item.Item;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {

  @Id
  @GeneratedValue
  @Column(name = "order_item_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "item_id")
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  private int orderPrice; // 주문 가격
  private int count; // 주문 수량

  public Long getId() {
    return id;
  }

  public OrderItem setId(Long id) {
    this.id = id;
    return this;
  }

  public Item getItem() {
    return item;
  }

  public OrderItem setItem(Item item) {
    this.item = item;
    return this;
  }

  public Order getOrder() {
    return order;
  }

  public OrderItem setOrder(Order order) {
    this.order = order;
    return this;
  }

  public int getOrderPrice() {
    return orderPrice;
  }

  public OrderItem setOrderPrice(int orderPrice) {
    this.orderPrice = orderPrice;
    return this;
  }

  public int getCount() {
    return count;
  }

  public OrderItem setCount(int count) {
    this.count = count;
    return this;
  }
}
