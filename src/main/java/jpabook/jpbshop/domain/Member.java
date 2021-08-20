package jpabook.jpbshop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member {

  @Id @GeneratedValue private Long id;

  private String username;

  public Long getId() {
    return id;
  }

  public Member setId(Long id) {
    this.id = id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public Member setUsername(String username) {
    this.username = username;
    return this;
  }
}
