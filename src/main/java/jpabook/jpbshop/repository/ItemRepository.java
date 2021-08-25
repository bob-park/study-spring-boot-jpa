package jpabook.jpbshop.repository;

import java.util.List;
import javax.persistence.EntityManager;
import jpabook.jpbshop.domain.item.Item;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {

  private final EntityManager em;

  public ItemRepository(EntityManager em) {
    this.em = em;
  }

  public void save(Item item) {
    if (item.getId() == null) {
      em.persist(item); // 새로 생성한 객체이므로 Persistence Context 에 등록 및 DB insert
    } else {
      em.merge(item); // 기존에 있는 경우 Persistence Context 에 등록 및 DB update 와 비슷
    }
  }

  public Item find(Long id) {
    return em.find(Item.class, id);
  }

  public List<Item> findAll() {
    return em.createQuery("select i from Item i", Item.class).getResultList();
  }
}
