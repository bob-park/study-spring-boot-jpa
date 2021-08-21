package jpabook.jpbshop.domain;

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
