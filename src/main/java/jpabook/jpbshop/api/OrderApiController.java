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
}
