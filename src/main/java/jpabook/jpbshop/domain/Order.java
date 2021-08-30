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
  
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  private LocalDateTime orderDate;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  protected Order() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Member getMember() {
    return member;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public Delivery getDelivery() {
    return delivery;
  }

  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  // == 연관관계 편의 메소드
  // * 연관관계 편의 메소드는 연관관계를 control 하는 entity 에서 만드는게 좋음
  public void setMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItem.setOrder(this);
    this.orderItems.add(orderItem);
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrder(this);
  }

  // == 생성 메서드 ==//

  /**
   * 주문 생성
   *
   * @param member
   * @param delivery
   * @param orderItems
   * @return
   */
  public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
    Order order = new Order();
    order.setMember(member);
    order.setDelivery(delivery);

    for (OrderItem orderItem : orderItems) {
      order.addOrderItem(orderItem);
    }

    order.setStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());

    return order;
  }

  // == 비지니스 로직 == //
  /**
   * 주문 취소
   *
   * @return
   */
  public void cancel() {
    if (delivery.getStatus() == DeliveryStatus.COMP) {
      throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
    }

    this.setStatus(OrderStatus.CANCEL);

    for (OrderItem orderItem : orderItems) {

      orderItem.cancel();
    }
  }

  // == 조회 로직 == //

  /**
   * 전체 주문 가격 조회
   *
   * @return
   */
  public int getTotalPrice() {
    return getOrderItems().stream().mapToInt(OrderItem::getTotalPrice).sum();
  }
}
