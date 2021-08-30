package jpabook.jpbshop.repository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jpabook.jpbshop.api.OrderSimpleApiController;
import jpabook.jpbshop.domain.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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

  public List<Order> findAll(OrderSearch orderSearch) {
    String jpql = "select o from Order o join o.member m";

    boolean isFirstCondition = true;

    // 주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {

      if (isFirstCondition) {
        jpql += " where";
        isFirstCondition = false;
      } else {
        jpql += " and";
      }

      jpql += " o.status = :status";
    }

    if (StringUtils.hasText(orderSearch.getMemberName())) {
      if (isFirstCondition) {
        jpql += " where";
        isFirstCondition = false;
      } else {
        jpql += " and";
      }

      jpql += " m.name = :name";
    }

    TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1_000);

    if (orderSearch.getOrderStatus() != null) {
      query = query.setParameter("status", orderSearch.getOrderStatus());
    }

    if (StringUtils.hasText(orderSearch.getMemberName())) {
      query = query.setParameter("name", orderSearch.getMemberName());
    }

    return query.getResultList();
  }

  /**
   * JPA Criteria
   *
   * <p>! 코드를 보고 DB query 가 예상되지 않는다. - 사용 X
   *
   * @param orderSearch
   * @return
   */
  public List<Order> findAllCriteria(OrderSearch orderSearch) {

    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<Order> cq = cb.createQuery(Order.class);

    Root<Order> o = cq.from(Order.class);

    Join<Object, Object> m = o.join("member", JoinType.INNER);

    List<Predicate> criteria = new ArrayList<>();

    if (orderSearch.getOrderStatus() != null) {
      Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());

      criteria.add(status);
    }

    if (StringUtils.hasText(orderSearch.getMemberName())) {
      Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");

      criteria.add(name);
    }

    cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));

    return em.createQuery(cq).setMaxResults(1_000).getResultList();
  }

  public List<Order> findAllWithMemberDelivery() {
    return em.createQuery(
            "select o from Order o "
                + "join fetch o.member m " // ManyToOne
                + "join fetch o.delivery d", // OneToOne
            Order.class)
        .getResultList();
  }

  public List<Order> findAllWithMemberDelivery(int offset, int limit) {
    return em.createQuery(
            "select o from Order o "
                + "join fetch o.member m " // ManyToOne
                + "join fetch o.delivery d", // OneToOne
            Order.class)
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultList();
  }

  public List<SimpleOrderQueryDto> findOrderDtos() {

    // 기본 적으로 entity 가 아닌 DTO 는 반환되지 않는다.
    // JPQL 에 DTO 생성자에 entity 를 바로 넘길 수 없다.(entity 의 식별자로 넘기기 때문에 DTO 는 식별자가 없어 애매한 상황이 되버림)
    return em.createQuery(
            "select new jpabook.jpbshop.repository.SimpleOrderQueryDto(o.id, o.member.name, o.orderDate, o.status, o.delivery.address) from Order o "
                + "join o.member m "
                + "join o.delivery d",
            SimpleOrderQueryDto.class)
        .getResultList();
  }

  public List<Order> findAllWithItem() {

    return em.createQuery(
            "select distinct o from Order o "
                + "join fetch o.member m " // ManyToOne
                + "join fetch o.delivery d " // OneToOne
                + "join fetch o.orderItems io " // OneToMany - Collection
                + "join fetch io.item i", // OneToOne
            Order.class)
        .getResultList();
  }
}
