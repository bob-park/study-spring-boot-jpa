package jpabook.jpbshop.controller;

import jpabook.jpbshop.domain.item.Book;
import jpabook.jpbshop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ItemController {

  private final ItemService itemService;

  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping("/items/new")
  public String createForm(Model model) {
    model.addAttribute("form", new BookForm());

    return "items/createItemForm";
  }

  @PostMapping("/items/new")
  public String create(BookForm bookForm) {
    Book book = new Book();

    book.setName(bookForm.getName());
    book.setPrice(bookForm.getPrice());
    book.setStockQuantity(bookForm.getStockQuantity());
    book.setAuthor(bookForm.getAuthor());
    book.setIsbn(bookForm.getIsbn());

    itemService.saveItem(book);

    return "redirect:/";
  }

  @GetMapping("/items")
  public String list(Model model) {

    model.addAttribute("items", itemService.findItems());

    return "items/itemList";
  }

  @GetMapping("/items/{itemId}/edit")
  public String updateItemForm(@PathVariable Long itemId, Model model) {
    Book item = (Book) itemService.findItem(itemId);

    BookForm form = new BookForm();

    form.setId(item.getId());
    form.setName(item.getName());
    form.setPrice(item.getPrice());
    form.setStockQuantity(item.getStockQuantity());
    form.setAuthor(item.getAuthor());
    form.setIsbn(item.getIsbn());

    model.addAttribute("form", form);

    return "items/updateItemForm";
  }

  @PostMapping("/items/{itemId}/edit")
  public String updateItem(@PathVariable Long itemId, @ModelAttribute BookForm form) {

    Book book = new Book();

    book.setId(form.getId()); // JPA 가 식별할 수 있는 ID 가 존재하는 경우 - 준영속 상태 객체가 된다.
    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());

    itemService.saveItem(book);

    return "redirect:/items";

  }
}
