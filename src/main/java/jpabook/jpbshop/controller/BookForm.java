package jpabook.jpbshop.controller;

public class BookForm {

  private Long id;

  private String name;
  private int price;
  private int stockQuantity;

  private String author;
  private String isbn;

  public Long getId() {
    return id;
  }

  public BookForm setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public BookForm setName(String name) {
    this.name = name;
    return this;
  }

  public int getPrice() {
    return price;
  }

  public BookForm setPrice(int price) {
    this.price = price;
    return this;
  }

  public int getStockQuantity() {
    return stockQuantity;
  }

  public BookForm setStockQuantity(int stockQuantity) {
    this.stockQuantity = stockQuantity;
    return this;
  }

  public String getAuthor() {
    return author;
  }

  public BookForm setAuthor(String author) {
    this.author = author;
    return this;
  }

  public String getIsbn() {
    return isbn;
  }

  public BookForm setIsbn(String isbn) {
    this.isbn = isbn;
    return this;
  }
}
