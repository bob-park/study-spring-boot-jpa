package jpabook.jpbshop.service;

import java.util.List;
import jpabook.jpbshop.domain.Delivery;
import jpabook.jpbshop.domain.Member;
import jpabook.jpbshop.domain.Order;
import jpabook.jpbshop.domain.OrderItem;
import jpabook.jpbshop.domain.item.Item;
import jpabook.jpbshop.repository.ItemRepository;
import jpabook.jpbshop.repository.MemberRepository;
import jpabook.jpbshop.repository.OrderRepository;
import jpabook.jpbshop.repository.OrderSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

  private final OrderRepository orderRepository;
  private final MemberRepository memberRepository;
  private final ItemRepository itemRepository;

  public OrderService(
      OrderRepository orderRepository,
      MemberRepository memberRepository,
      ItemRepository itemRepository) {
    this.orderRepository = orderRepository;
    this.memberRepository = memberRepository;
    this.itemRepository = itemRepository;
  }

  /**
   * 주문
   *
   * @param memberId
   * @param itemId
   * @param count
   * @return
   */
  @Transactional
  public Long order(Long memberId, Long itemId, int count) {

    // 엔티티 조회
    Member member = memberRepository.find(memberId);
    Item item = itemRepository.find(itemId);

    // 배송정보 생성
    Delivery delivery = new Delivery();

    delivery.setAddress(member.getAddress());

    // 주문 상품 생성
    OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

    // 주문 생성
    Order order = Order.createOrder(member, delivery, orderItem);

    // 주문 저장
    // ? 왜 order 만 save() 를 호출할까?
    // ! Order entity 에 cascade 가 걸려있기 때문

    // ? 그럼 Cascade 의 범위는 어디까지 해야 되는가?
    // ! Entity 내부 값이 다른 Entity 에서 참조하지 않는 경우, 참조되는 경우라면 Cascade 개별적으로 persist() 하는 것이 좋음

    orderRepository.save(order);

    return order.getId();
  }

  /**
   * 주문 취소
   *
   * @param orderId
   */
  @Transactional
  public void cancelOrder(Long orderId) {

    // 주문 조회
    Order order = orderRepository.find(orderId);

    // 주문 취소
    // ! 핵심 비지니스 로직이 entity 안에 있는 Design Pattern 을 Domain Model Pattern 이라고 한다.
    order.cancel();
  }

  /**
   * 검색
   *
   * <p>TODO 해야함
   *
   * @param orderSearch
   * @return
   */
  public List<Order> findOrders(OrderSearch orderSearch) {
    return orderRepository.findAll(orderSearch);
  }
}
