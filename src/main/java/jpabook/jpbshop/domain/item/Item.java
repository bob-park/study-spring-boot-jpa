package jpabook.jpbshop.domain.item;

import jpabook.jpbshop.domain.Category;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public abstract class Item {

  @Id
  @GeneratedValue
  @Column(name = "item_id")
  private Long id;

  private String name;
  private int price;
  private int stockQuantity;

  @ManyToMany(mappedBy = "items")
  private List<Category> categories = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public Item setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Item setName(String name) {
    this.name = name;
    return this;
  }

  public int getPrice() {
    return price;
  }

  public Item setPrice(int price) {
    this.price = price;
    return this;
  }

  public int getStockQuantity() {
    return stockQuantity;
  }

  public Item setStockQuantity(int stockQuantity) {
    this.stockQuantity = stockQuantity;
    return this;
  }
}
