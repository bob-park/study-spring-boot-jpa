package jpabook.jpbshop.api;

import jpabook.jpbshop.domain.Address;
import jpabook.jpbshop.domain.Order;
import jpabook.jpbshop.domain.OrderItem;
import jpabook.jpbshop.domain.OrderStatus;
import jpabook.jpbshop.repository.OrderRepository;
import jpabook.jpbshop.repository.OrderSearch;
import jpabook.jpbshop.repository.order.query.OrderQueryDto;
import jpabook.jpbshop.repository.order.query.OrderQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderApiController {

  private final OrderRepository orderRepository;

  private final OrderQueryRepository orderQueryRepository;

  public OrderApiController(
      OrderRepository orderRepository, OrderQueryRepository orderQueryRepository) {
    this.orderRepository = orderRepository;
    this.orderQueryRepository = orderQueryRepository;
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

  // == V3.1 == //

  /**
   * 주문 조회
   *
   * <p>* 개선한 것
   *
   * <pre>
   *     - root entity 기준 ToOne 연관관계는 모두 fetch join 을 한다. (ToOne 관계는 row 를 늘리진 않기 때문에)
   *     - Collection 은 Lazy loading 으로 조회한다. (ToMany 관계 - loop 돌면서 가져온다.)
   *     - Lazy loading 조회시 성능 최적화를 위해 {hibernate.default_batch_fetch_size} 또는 @BatchSize 를 적용한다. (최대개수는 1000개) - 1000 추천
   *
   * </pre>
   *
   * @return
   */
  @GetMapping(path = "api/v3.1/orders")
  public List<OrderDto> ordersV3_page(
      @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "100") int limit) {
    List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

    return orders.stream().map(OrderDto::new).collect(Collectors.toList());
  }

  // == V4 == //

  /**
   * 주문 조회
   *
   * <p>* 문제점
   *
   * <pre>
   *     - repository 에서 바로 DTO 컬렉션을 조회하지만, N + 1 번 DB Query 가 실행되는 문제가 발생한다.
   * </pre>
   *
   * @return
   */
  @GetMapping(path = "api/v4/orders")
  public List<OrderQueryDto> orderV4() {
    return orderQueryRepository.findOrderQueryDtos();
  }

  // == V5 == //

  /**
   * 주문 조회
   *
   * <p>개선한 점
   *
   * <pre>
   *     - order item 을 가져올 때 ~ in 으로하여 한번에 order items 를 가져옴
   * </pre>
   *
   * * 문제점
   *
   * <pre>
   *     - 상당 부분이 코드를 직접 작성하여 처리하였다.
   * </pre>
   *
   * @return
   */
  @GetMapping(path = "api/v5/orders")
  public List<OrderQueryDto> orderV5() {
    return orderQueryRepository.findAllBoyDtoOptimization();
  }

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
