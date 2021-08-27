package jpabook.jpbshop.repository;

import jpabook.jpbshop.domain.item.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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

      // ! 파라미터로 넘긴 객체는 영속상태가 아님 - 반환 객체가 영속상태임
      // ! 모든 데이터를 변경하므로 주의해야한다. - 실무에서 사용하지 않는다.
      // null 인 것은 null 로 변경해버림

      // ! 실무에서는 변경감지를 사용해야한다.
      // 1. find 로 조회 후 영속상태로 만듬
      // 2. 변경감지 이용하여 개별적으로 데이터 수정
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
