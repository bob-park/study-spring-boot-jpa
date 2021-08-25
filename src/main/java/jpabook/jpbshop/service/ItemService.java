package jpabook.jpbshop.service;

import java.util.List;
import jpabook.jpbshop.domain.item.Item;
import jpabook.jpbshop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItemService {

  private final ItemRepository itemRepository;

  public ItemService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  @Transactional
  public void saveItem(Item item) {
    itemRepository.save(item);
  }

  public List<Item> findItems() {
    return itemRepository.findAll();
  }

  public Item findItem(Long itemId) {
    return itemRepository.find(itemId);
  }
}
