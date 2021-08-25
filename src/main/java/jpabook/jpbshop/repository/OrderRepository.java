package jpabook.jpbshop.repository;

import java.util.List;
import javax.persistence.EntityManager;
import jpabook.jpbshop.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

  private final EntityManager em;

  public OrderRepository(EntityManager em) {
    this.em = em;
  }

  public void save(Order order) {
    em.persist(order);
  }

  public Order find(Long orderId) {
    return em.find(Order.class, orderId);
  }

  // TODO 이름으로 검색이 필요
  public List<Order> findAll() {
    return em.createQuery("select o from Order o", Order.class).getResultList();
  }
}
