package jpabook.jpbshop.domain;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

  @Id
  @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  private String name;

  @Embedded private Address address;

  /*
   * hibernate 에서 컬렉션 변경에 대해서 감지해야되기 때문에,
   * 영속성 컨텍스트에 종속될 때, hibernate 내부적으로 컬렉션을 감싸버리기 때문에, setter 를 가급적 사용하지 말자.
   *
   * 따라서, setter 를 사용할 경우 hibernate 내부 매커니즘이 깨질 수 있기 때문에 사용하지 말자.
   */
  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public Member setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Member setName(String name) {
    this.name = name;
    return this;
  }

  public Address getAddress() {
    return address;
  }

  public Member setAddress(Address address) {
    this.address = address;
    return this;
  }

  public List<Order> getOrders() {
    return orders;
  }

  /**
   * entity 편의 메소드
   *
   * @param order
   */
  public void addOrder(Order order) {
    order.setMember(this);
    orders.add(order);
  }
}
