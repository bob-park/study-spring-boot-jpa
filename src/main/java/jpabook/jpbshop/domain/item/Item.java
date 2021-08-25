package jpabook.jpbshop.domain.item;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import jpabook.jpbshop.domain.Category;
import jpabook.jpbshop.exception.NotEnoughStockException;

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

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public int getStockQuantity() {
    return stockQuantity;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public void setStockQuantity(int stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  //==비지니스 로직 ==//
  // stockQuantity 는 Item 이 가지고 있기 때문에, stockQuantity 관련 비지니스 로직은 entity 에서 관리하는것이 좋음
  // setter 로 하는게 data 를 set 하는 것이 아니라 비지니스 메서드를 통해 data 를 control 해야 한다.

  /**
   * stock 증가
   *
   * @param quantity
   */
  public void addStock(int quantity) {
    this.stockQuantity += quantity;
  }

  /**
   * stock 감소
   *
   * @param quantity
   */
  public void removeStock(int quantity) {

    // 0 보다 작을 경우 확인
    int restStock = this.stockQuantity - quantity;

    if (restStock < 0) {
      throw new NotEnoughStockException("need more stock");
    }

    this.stockQuantity -= quantity;
  }
}
