package jpabook.jpbshop.domain;

import jpabook.jpbshop.domain.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

  @Id
  @GeneratedValue
  @Column(name = "category_id")
  private Long id;

  private String name;

  @ManyToMany
  @JoinTable(
      name = "category_item",
      joinColumns = @JoinColumn(name = "category_id"),
      inverseJoinColumns = @JoinColumn(name = "item_id"))
  private List<Item> items = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent")
  private List<Category> child = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public Category setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Category setName(String name) {
    this.name = name;
    return this;
  }

  public List<Item> getItems() {
    return items;
  }

  public Category setItems(List<Item> items) {
    this.items = items;
    return this;
  }

  public Category getParent() {
    return parent;
  }

  public Category setParent(Category parent) {
    this.parent = parent;
    return this;
  }

  public List<Category> getChild() {
    return child;
  }

  public Category setChild(List<Category> child) {
    this.child = child;
    return this;
  }
}
