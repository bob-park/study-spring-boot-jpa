package jpabook.jpbshop.api;

import jpabook.jpbshop.domain.Address;
import jpabook.jpbshop.domain.Order;
import jpabook.jpbshop.domain.OrderItem;
import jpabook.jpbshop.domain.OrderStatus;
import jpabook.jpbshop.repository.OrderRepository;
import jpabook.jpbshop.repository.OrderSearch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderApiController {

  private final OrderRepository orderRepository;

  public OrderApiController(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  // == V1 == //

  /**
   * 주문 조회
   *
   * <p>! 실무에서 사용 X
   *
   * @return
   */
  @GetMapping(path = "api/v1/orders")
  public List<Order> ordersV1() {

    List<Order> all = orderRepository.findAll(new OrderSearch());

    // Lazy Loading 강제 초기화
    for (Order order : all) {
      order.getMember().getName();
      order.getDelivery().getAddress();

      order.getOrderItems().forEach(orderItem -> orderItem.getItem().getName());
    }

    return all;
  }

  // == V2 == //

  /**
   * 주문 조회
   *
   * <p>* 문제점
   *
   * <pre>
   *     - DTO 안에 entity 가 있으면 안됨 (entity 에 대한 의존성을 완전히 끊어야 된다.) - OrderDto - OrderItemDto...
   *     - Lazy Loading 으로 인한 DB Query 가 너무 많이 실행된다. (member, delivery, orderItem, item...)
   * </pre>
   *
   * @return
   */
  @GetMapping(path = "api/v2/orders")
  public List<OrderDto> ordersV2() {
    List<Order> orders = orderRepository.findAll(new OrderSearch());

    return orders.stream().map(OrderDto::new).collect(Collectors.toList());
  }

  // == V3 == //

  /**
   * 주문 조회
   *
   * <p>* 문제점
   *
   * <pre>
   *     - DB Query 가 실행되는 수는 현저히 적어졌으나, 결과가 실제 데이터 보다 더 많이 나온다. (distinct 로 하여 중복된 entity 를 제거할 수 있다.)
   *        - 허나, 실제 DB Query 에 distinct 를 넣어주지만, 전체 컬럼에 대한 값이 모두 같아야 없어지므로, 원하는 대로 데이터가 나오지 않을 가능성이 높다.
   *        - JPA 에서는 entity 의 식별자가 같을 경우 중복을 제거시켜준다.
*        - 페이징 불가능 (가능은 하나, JPA 에서 DB Query (페이징 쿼리 없음) 결과를 메모리에 모두 올려서 페이징 처리해버린다. - 매우 위험)
   *     - 1:N - Collection Fetch join 은 1개만 사용해야한다. (JPA 에서 한개밖에 사용 못함 - 경고 내보냄)
   * </pre>
   *
   * @return
   */
  @GetMapping(path = "api/v3/orders")
  public List<OrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithItem();

    return orders.stream().map(OrderDto::new).collect(Collectors.toList());
  }

  // == V4 == //

  // == V5 == //

  // == V6 == //

  private class OrderDto {

    private final Long orderId;
    private final String name;
    private final LocalDateTime orderDate;
    private final OrderStatus orderStatus;
    private final Address address;
    private final List<OrderItemDto> orderItems;

    public OrderDto(Order order) {

      this(
          order.getId(),
          order.getMember().getName(),
          order.getOrderDate(),
          order.getStatus(),
          order.getDelivery().getAddress(),
          order.getOrderItems().stream().map(OrderItemDto::new).collect(Collectors.toList()));

      order.getOrderItems().forEach(orderItem -> orderItem.getItem().getName());
    }

    public OrderDto(
        Long orderId,
        String name,
        LocalDateTime orderDate,
        OrderStatus orderStatus,
        Address address,
        List<OrderItemDto> orderItems) {
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

    public List<OrderItemDto> getOrderItems() {
      return orderItems;
    }
  }

  private class OrderItemDto {

    private final String itemName;
    private final int orderPrice;
    private final int count;

    public OrderItemDto(OrderItem orderItem) {
      this(orderItem.getItem().getName(), orderItem.getOrderPrice(), orderItem.getCount());
    }

    public OrderItemDto(String itemName, int orderPrice, int count) {
      this.itemName = itemName;
      this.orderPrice = orderPrice;
      this.count = count;
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
}
