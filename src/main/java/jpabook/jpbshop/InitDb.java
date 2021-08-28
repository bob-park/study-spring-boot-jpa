package jpabook.jpbshop;

import jpabook.jpbshop.domain.*;
import jpabook.jpbshop.domain.item.Book;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
public class InitDb {

  private final InitService initService;

  public InitDb(InitService initService) {
    this.initService = initService;
  }

  @PostConstruct
  public void init() {
    initService.dbInit1();
    initService.dbInit2();
  }

  @Component
  @Transactional
  static class InitService {

    private final EntityManager em;

    public InitService(EntityManager em) {
      this.em = em;
    }

    public void dbInit1() {
      Member member = createMember("userA", "Seoul", "1", "11111");

      em.persist(member);

      Book book1 = createBook("JPA 1 Book", 10_000, 100);

      em.persist(book1);

      Book book2 = createBook("JPA 2 Book", 10_000, 200);

      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10_000, 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40_000, 2);

      Delivery delivery = createDelivery(member);

      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

      em.persist(order);
    }

    public void dbInit2() {
      Member member = createMember("userB", "Seoul", "1", "11111");

      em.persist(member);

      Book book1 = createBook("Spring 1 Book", 10_000, 100);

      em.persist(book1);

      Book book2 = createBook("Spring 2 Book", 20_000, 100);

      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10_000, 3);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20_000, 4);

      Delivery delivery = createDelivery(member);

      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

      em.persist(order);
    }

    private Delivery createDelivery(Member member) {
      Delivery delivery = new Delivery();
      delivery.setAddress(member.getAddress());
      return delivery;
    }

    private Book createBook(String name, int price, int stockQuantity) {
      Book book1 = new Book();

      book1.setName(name);
      book1.setPrice(price);
      book1.setStockQuantity(stockQuantity);
      return book1;
    }

    private Member createMember(String name, String city, String street, String zipcode) {
      Member member = new Member();

      member.setName(name);
      member.setAddress(new Address(city, street, zipcode));
      return member;
    }
  }
}
