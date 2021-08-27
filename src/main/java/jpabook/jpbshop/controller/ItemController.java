package jpabook.jpbshop.controller;

import jpabook.jpbshop.domain.item.Book;
import jpabook.jpbshop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}
