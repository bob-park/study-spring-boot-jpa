package jpabook.jpbshop.repository;

import jpabook.jpbshop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MemberRepositoryOld {

  private final EntityManager em;

  // * spring boot data jpa 가 entity manager 를 초기화 해준다.
  public MemberRepositoryOld(EntityManager em) {
    this.em = em;
  }

  public void save(Member member) {
    em.persist(member);
  }

  public Member find(Long id) {

    return em.find(Member.class, id);
  }

  /**
   * 전체 결과를 반환해주는 메소드
   *
   * <pre>
   * !List 로 반환해야할 경우 JPQL 로 해야한다.
   * </pre>
   *
   * @return {@link List}
   */
  public List<Member> findAll() {
    return em.createQuery("select m from Member m", Member.class).getResultList();
  }

  public List<Member> findByName(String name) {
    return em.createQuery("select m from Member m where m.name = :name", Member.class)
        .setParameter("name", name)
        .getResultList();
  }
}
