package jpabook.jpbshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.EntityManager;
import jpabook.jpbshop.domain.Address;
import jpabook.jpbshop.domain.Member;
import jpabook.jpbshop.domain.Order;
import jpabook.jpbshop.domain.OrderStatus;
import jpabook.jpbshop.domain.item.Book;
import jpabook.jpbshop.exception.NotEnoughStockException;
import jpabook.jpbshop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

  @Autowired private EntityManager em;

  @Autowired private OrderService orderService;

  @Autowired private OrderRepository orderRepository;

  @Test
  @DisplayName("상품 주문")
  void orderItem() throws Exception {
    // given
    Member member = createMember();
    em.persist(member);

    Book book = createBook("JPA_1", 10000, 10);
    em.persist(book);

    int orderCount = 2;

    // when
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    // then
    Order getOrder = orderRepository.find(orderId);

    assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
    assertThat(getOrder.getOrderItems().size()).isEqualTo(1);
    assertThat(getOrder.getTotalPrice()).isEqualTo(10000 * orderCount);
    assertThat(book.getStockQuantity()).isEqualTo(8);
  }

  @Test
  @DisplayName("상품 주문 - 수량 초과")
  void orderItemThrowNotEnoughException() throws Exception {

    // given
    Member member = createMember();
    em.persist(member);

    Book book = createBook("JPA_1", 10_000, 1);
    em.persist(book);

    int orderCount = 2;

    // then
    assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount))
        .isInstanceOf(NotEnoughStockException.class);
  }

  @Test
  @DisplayName("상품 주문 취소")
  void cancelOrder() throws Exception {
    // given
    Member member = createMember();
    em.persist(member);

    Book book = createBook("JPA_1", 10_000, 10);
    em.persist(book);

    int orderCount = 2;

    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    // when
    orderService.cancelOrder(orderId);

    // then
    Order getOrder = orderRepository.find(orderId);

    assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
    assertThat(book.getStockQuantity()).isEqualTo(10);
  }

  private Member createMember() {

    Member member = new Member();
    member.setName("member1");
    member.setAddress(new Address("Seoul", "ㅋㅋ", "123-123"));

    return member;
  }

  private Book createBook(String name, int price, int quantity) {

    Book book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(quantity);

    return book;
  }
}
