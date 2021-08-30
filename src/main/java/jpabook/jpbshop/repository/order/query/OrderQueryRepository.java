package jpabook.jpbshop.repository.order.query;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

  public List<OrderQueryDto> findAllByDtoOptimization() {

    List<OrderQueryDto> orders = findOrders();

    List<Long> orderIds =
        orders.stream().map(OrderQueryDto::getOrderId).collect(Collectors.toList());

    Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

    orders.forEach(order -> order.setOrderItems(orderItemMap.get(order.getOrderId())));

    return orders;
  }

  private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {

    List<OrderItemQueryDto> orderItems =
        em.createQuery(
                "select new jpabook.jpbshop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi "
                    + "join oi.item i "
                    + "where oi.order.id in :orderIds",
                OrderItemQueryDto.class)
            .setParameter("orderIds", orderIds)
            .getResultList();

    return orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
  }

  public List<OrderFlatDto> findAllByDtoFlat() {

    return em.createQuery(
            "select new jpabook.jpbshop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) from Order o "
                + "join o.member m "
                + "join o.delivery d "
                + "join o.orderItems oi "
                + "join oi.item i",
            OrderFlatDto.class)
        .getResultList();
  }
}
