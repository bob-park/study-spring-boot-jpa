package jpabook.jpbshop.api;

import jpabook.jpbshop.domain.Address;
import jpabook.jpbshop.domain.Order;
import jpabook.jpbshop.domain.OrderStatus;
import jpabook.jpbshop.repository.OrderRepository;
import jpabook.jpbshop.repository.OrderSearch;
import jpabook.jpbshop.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order
 *
 * <p>연관관계 (~ToOne) - 지연로딩(Lazy)
 *
 * <pre>
 *     Order -> Member
 *     Order -> Delivery
 * </pre>
 */
@RestController
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;

  public OrderSimpleApiController(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  // == V1 ==//

  /**
   * 주문 조회
   *
   * <p>* 문제점
   *
   * <pre>
   *     - Order -> Member 가 있고 반대로 가지고 있기 때문에, entity 를 반환하는 API 이기 떄문에, 무한루프에 빠져버린다.
   *     - @JsonIgnore 할 수 있을 것 같지만, 분명 side effect 가 발생 한다.
   *        - Order -> Member 가 Lazy 로딩이기 때문에, Hibernate Proxy 객체가 우선적으로 들어가 있고, Member 사용 시점에 Member 객체가 초기화 된다.
   *        - 하지만, Jackson Library 는 Member 에 우선적으로 들어있는 Hibernate Proxy 를 json 으로 변환하려고 할 때, Exception 이 발생한다.
   *        - Hibernate5Module 를 Bean 으로 등록하면 되지만, 실무에서 사용하면 안된다. (default : Lazy Loading 시 무조건 null 로 무시된다.)
   *     - API Spec 이 변경될 수 있다.
   *     - 성능 이슈가 발생한다.(필요없는 데이터까지 조회하기 때문)
   * </pre>
   *
   * @return
   */
  @GetMapping(path = "api/v1/simple-orders")
  public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAll(new OrderSearch());

    // 강제로 쿼리 호출하면 정상적으로 출력이 되긴 하지만, 사용 X
    for (Order order : all) {
      order.getMember().getName(); // Lazy 강제 초기화
      order.getDelivery().getAddress(); // Lazy 강제 초기화
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
   *     - V1 과 동일하게, Lazy Loading 으로 인한 DB Query 가 너무 많이 실행된다. (N + 1 문제)
   * </pre>
   *
   * @return
   */
  @GetMapping(path = "api/v2/simple-orders")
  public List<SimpleOrderDto> ordersV2() {

    // N + 1 문제 발생
    // Order 조회로 1번 (2 row) -> Member, Delivery 각각 Order 의 result row 개수 만큼 추가 - 총 1 + (2 + 2)번 실행
    // Lazy 에서 Eager 로 변경하면, SQL Query 가 어떻게 실행될 지 예상되지 않음
    return orderRepository.findAll(new OrderSearch()).stream()
        .map(SimpleOrderDto::new)
        .collect(Collectors.toList());
  }

  // == V3 == //

  /**
   * 주문 조회
   *
   * <p>* 개선점
   *
   * <pre>
   *     - V1, V2 보다 DB Query 가 현저히 줄어든다. (fetch join - 1번 실행)
   * </pre>
   *
   * @return
   */
  @GetMapping(path = "api/v3/simple-orders")
  public List<SimpleOrderDto> ordersV3() {

    return orderRepository.findAllWithMemberDelivery().stream()
        .map(SimpleOrderDto::new)
        .collect(Collectors.toList());
  }

  static class SimpleOrderDto {
    private final Long orderId;
    private final String name;
    private final LocalDateTime orderDate;
    private final OrderStatus orderStatus;
    private final Address address;

    public SimpleOrderDto(Order order) {
      this.orderId = order.getId();
      this.name = order.getMember().getName();
      this.orderDate = order.getOrderDate();
      this.orderStatus = order.getStatus();
      this.address = order.getDelivery().getAddress();
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
}
