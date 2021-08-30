package jpabook.jpbshop.repository;

import jpabook.jpbshop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * * Spring Data JPA 에서 기본적으로 CRUD 가 내장되어 있다.
 *
 * * Spring Data JPA 장점
 *
 * <pre>
 *     - 구현체의 Method 의 signature 를 보고 DB 조회 Query 를 생성해버린다.
 *     - 개발자는 구현체만 생성하면됨
 * </pre>
 *
 *
 * ! Spring DATA JPA 주의점
 *
 * <pre>
 *     - Spring Data JPA 는 JPA 를 단지 구현한 것 뿐
 *     - JPA 를 정확히 알고 써야됨 (장애나 이슈 발생 시 어려움이 있음)
 * </pre>
 *
 *
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByName(String name);
}
