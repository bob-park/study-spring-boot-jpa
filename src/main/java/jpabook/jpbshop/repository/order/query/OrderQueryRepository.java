package jpabook.jpbshop.repository.order.query;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class OrderQueryRepository {

  private final EntityManager em;

  public OrderQueryRepository(EntityManager em) {
    this.em = em;
  }

  public List<OrderQueryDto> findOrderQueryDtos() {

    List<OrderQueryDto> result = findOrders();

    result.forEach(item -> item.setOrderItems(findOrderItems(item.getOrderId())));

    return result;
  }

  private List<OrderItemQueryDto> findOrderItems(Long orderId) {
    return em.createQuery(
            "select new jpabook.jpbshop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi "
                + "join oi.item i "
                + "where oi.order.id = :orderId",
            OrderItemQueryDto.class)
        .setParameter("orderId", orderId)
        .getResultList();
  }

  private List<OrderQueryDto> findOrders() {
    return em.createQuery(
            "select new jpabook.jpbshop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o "
                + "join o.member m "
                + "join o.delivery d",
            OrderQueryDto.class)
        .getResultList();
  }
}
