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

  @ManyToOne(fetch = FetchType.LAZY)
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

  public void setId(Long id) {
    this.id = id;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public int getOrderPrice() {
    return orderPrice;
  }

  public void setOrderPrice(int orderPrice) {
    this.orderPrice = orderPrice;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  protected OrderItem() {
  }

  // == 생성 메서드 ==//

  public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
    OrderItem orderItem = new OrderItem();

    orderItem.setItem(item);
    orderItem.setOrderPrice(orderPrice);
    orderItem.setCount(count);

    item.removeStock(count);

    return orderItem;
  }

  // == 비지니스 로직 == //

  /**
   * 주문 취소
   *
   * <pre>
   *   재고 수량은 원복시킨다.
   * </pre>
   *
   * @return
   */
  public void cancel() {
    getItem().addStock(count);
  }

  // == 조회 로직 == //
  /**
   * 전체 주문 가격
   *
   * @return
   */
  public int getTotalPrice() {
    return getOrderPrice() * getCount();
  }
}
